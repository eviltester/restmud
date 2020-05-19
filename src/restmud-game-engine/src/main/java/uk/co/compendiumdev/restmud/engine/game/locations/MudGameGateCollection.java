package uk.co.compendiumdev.restmud.engine.game.locations;

import uk.co.compendiumdev.restmud.engine.game.Locations;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MudGameGateCollection {

    private final Map<String, MudLocationDirectionGate> gates = new HashMap<>();

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

        // if nowhere then no gates
        if(location==null){
            return new ArrayList<MudLocationDirectionGate>();
        }

        List<String> gateNames = location.getExitGateNames();
        return getGatesNamed(gateNames);

        // TODO: remove old approach for finding gates
// List<MudLocationDirectionGate> foundGates = new ArrayList<>();
//        for (MudLocationDirectionGate aGate : gates.values()) {
//            if (location == aGate.fromLocation) {
//                foundGates.add(aGate);
//            }
//
//            if (location == aGate.toLocation && aGate.getGateDirection() == GateDirection.BOTH_WAYS) {
//                foundGates.add(aGate);
//            }
//        }
//        return foundGates;
    }

    private List<MudLocationDirectionGate> getGatesNamed(final List<String> gateNames) {
        List<MudLocationDirectionGate> foundGates = new ArrayList<>();
        for (String aGateName : gateNames){
            MudLocationDirectionGate gate =gates.get(aGateName);
            if(gate!=null) {
                foundGates.add(gate);
            }else{
                System.out.println("Warning went looking for non-existant gate in getGatesNamed: " + aGateName);
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
        if (gateBetween == null || !gateBetween.isVisible()) {
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

    public void addAllGatesToLocations(final Locations gameLocations) {

        for(MudLocationDirectionGate gate : gates.values()){
            MudLocationExit fromExit = gameLocations.get(gate.fromLocation.getLocationId()).
                                                    exitFor(gate.getFromDirection());

            if(fromExit==null){
                // if from exit is null then we may have been relying on the old gate definition to create it
                fromExit = new MudLocationExit(gate.getFromLocationId(), gate.getFromDirection(), gate.getToLocationId());
                // if gate is secret then this exit should also be secret
                fromExit.setSecretRoute(!gate.isVisible());
                gameLocations.get(gate.getFromLocationId()).addExit(fromExit);
                // TODO: amend all games to remove this warning, then change the code to remove this creation and throw an error instead
                System.out.println("WARNING LEGACY GAME CREATION APPROACH: creating from exits from gates is legacy and will be removed in a future version: " + gate.getGateName());
            }
            // todo: handle and report possible syntax error no from location
            fromExit.setGateName(gate.getGateName());

            MudLocationExit toExit = gameLocations.get(gate.toLocation.getLocationId()).
                    exitFor(gate.getToDirection());

            if(toExit!=null) {
                toExit.setGateName(gate.getGateName());
            }else{
                // may have been a one way gate
                if(gate.getWhichWayDirection()!=GateDirection.ONE_WAY){
                    // todo: handle and report possible syntax error no to location
                    System.out.println("Found a gate without a way back that is not defined as one way: " + gate.getGateName());
                }else{
                    // handle legacy exit creation
                    toExit = new MudLocationExit(gate.getToLocationId(), gate.getToDirection(), gate.getFromLocationId());
                    // if gate is secret then this exit should also be secret
                    toExit.setSecretRoute(!gate.isVisible());
                    gameLocations.get(gate.getToLocationId()).addExit(toExit);
                    toExit.setGateName(gate.getGateName());

                    // not a one way gate, need to create as using old gate definition approach
                    // TODO: amend all games to remove this warning, then change the code to remove this creation and throw an error instead
                    System.out.println("WARNING LEGACY GAME CREATION APPROACH: creating to exits from gates is legacy and will be removed in a future version: " + gate.getGateName());
                }
            }
        }
    }
}
