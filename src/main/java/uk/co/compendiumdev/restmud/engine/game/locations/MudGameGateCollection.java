package uk.co.compendiumdev.restmud.engine.game.locations;

import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MudGameGateCollection {

    Map<String, MudLocationDirectionGate> gates = new HashMap<>();

    public void addGate(MudLocationDirectionGate gate) {
        gates.put(gate.getGateName(), gate);
    }

    public MudLocationDirectionGate getGateBetween(MudLocation location, MudLocation destination) {

        for (MudLocationDirectionGate aGate : gates.values()) {
            if (location == aGate.fromLocation && destination == aGate.toLocation) {
                return aGate;
            }

            if (location == aGate.toLocation && destination == aGate.fromLocation) {
                return aGate;
            }
        }

        return null;
    }

    public List<MudLocationDirectionGate> getGatesHere(MudLocation location) {
        List<MudLocationDirectionGate> foundGates = new ArrayList<>();

        for (MudLocationDirectionGate aGate : gates.values()) {
            if (location == aGate.fromLocation) {
                foundGates.add(aGate);
            }

            if (location == aGate.toLocation && aGate.getGateDirection() == GateDirection.BOTH_WAYS) {
                foundGates.add(aGate);
            }
        }

        return foundGates;
    }

    public MudLocationDirectionGate getGateGoingFromHereInThatDirection(MudLocation location, String baseDirection) {
        for (MudLocationDirectionGate aGate : gates.values()) {
            if (location == aGate.fromLocation && aGate.getFromDirection().toLowerCase().contentEquals(baseDirection.toLowerCase())) {
                return aGate;
            }

            if (location == aGate.toLocation && aGate.getGateDirection() == GateDirection.BOTH_WAYS) {
                if (aGate.getToDirection().toLowerCase().contentEquals(baseDirection.toLowerCase())) {
                    return aGate;
                }
            }
        }

        return null;
    }

    public List<IdDescriptionPair> getGatesAsIdDescriptionPairs() {
        List<IdDescriptionPair> pairs = new ArrayList<IdDescriptionPair>();

        for (MudLocationDirectionGate gate : gates.values()) {

            String id = "?location=" + gate.fromLocation.getLocationId() + "&" + "toLocation=" + gate.toLocation.getLocationId();
            String state = "closed";
            if (gate.isOpen()) {
                state = "open";
            }
            String description = gate.shortDescription() + " From " + gate.fromLocation.getLocationId() + " To " + gate.toLocation.getLocationId() + " is " + state;
            pairs.add(new IdDescriptionPair(id, description));
        }
        return pairs;
    }

    public MudLocationDirectionGate getGateNamed(String gateName) {
        return gates.get(gateName);
    }


    public LastAction openGate(MudLocation location, String baseDirection) {
        return swingGateInDirection(true, location, baseDirection);
    }

    public LastAction closeGate(MudLocation location, String baseDirection) {
        return swingGateInDirection(false, location, baseDirection);
    }

    private LastAction swingGateInDirection(boolean open, MudLocation location, String baseDirection) {


        String openClose = "open";

        if (!open) {
            openClose = "close";
        }


        MudLocationDirectionGate gateBetween = getGateGoingFromHereInThatDirection(location, baseDirection);

        // cannot open a non-existant gate or one that is hidden normally
        if (gateBetween == null || gateBetween.isVisible()==false) {
            // There is no gate
            // if it is a direction then can I go that way?
            if (!location.canGo(baseDirection)) {
                return LastAction.createError("But I can't even go that way, how could I " + openClose + " something there?");
            } else {
                return LastAction.createError("There is nothing to " + openClose + " that way");
            }
        }

        // is the gate already in the state we want?
        if (gateBetween.isOpen() && open) {
            return LastAction.createError("But the way is already open!");
        }

        if (!open && !gateBetween.isOpen()) {
            return LastAction.createError("But the way is already closed!");
        }


        // it must be closed, so open it
        if (open) {
            if (gateBetween.getCanPlayerOpen()) {
                gateBetween.open();
            } else {
                return LastAction.createError("No, you can't open " + gateBetween.shortDescription() + "!");
            }
        } else {
            if (gateBetween.getCanPlayerClose()) {
                gateBetween.close();
            } else {
                return LastAction.createError("No, you can't close " + gateBetween.shortDescription() + "!");
            }
        }

        return LastAction.createSuccess("OK, you " + openClose + " " + gateBetween.shortDescription());
    }

    public Map<String, MudLocationDirectionGate> getGates() {
        return gates;
    }

    public void setFrom(Map<String,MudLocationDirectionGate> from) {
        this.gates.putAll(from);
    }
}
