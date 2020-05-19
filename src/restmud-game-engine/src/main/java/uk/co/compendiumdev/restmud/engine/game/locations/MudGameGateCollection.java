package uk.co.compendiumdev.restmud.engine.game.locations;

import uk.co.compendiumdev.restmud.engine.game.Locations;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.VisibleGate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MudGameGateCollection {

    private final Map<String, MudLocationDirectionGate> gates = new HashMap<>();

    public void addGate(MudLocationDirectionGate gate) {
        gates.put(gate.getGateName(), gate);
    }

    public List<MudLocationDirectionGate> getGatesHere(MudLocation location) {

        // if nowhere then no gates
        if(location==null){
            return new ArrayList<MudLocationDirectionGate>();
        }

        List<String> gateNames = location.getExitGateNames();
        return getGatesNamed(gateNames);
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

    // TODO: this is only used by the wizCommands and creating wiz page so probably should not be in the gate collection
    public List<IdDescriptionPair> getGatesAsIdDescriptionPairs() {
        List<IdDescriptionPair> pairs = new ArrayList<IdDescriptionPair>();

        for (MudLocationDirectionGate gate : gates.values()) {

            String id = "?gatename=" + gate.getGateName();
            String state = "closed";
            if (gate.isOpen()) {
                state = "open";
            }
            String description = gate.shortDescription() + " named " + gate.getGateName() + " is " + state;
            pairs.add(new IdDescriptionPair(id, description));
        }
        return pairs;
    }

    public MudLocationDirectionGate getGateNamed(String gateName) {
        return gates.get(gateName.toLowerCase());
    }

    public LastAction openGate(final String gateName) {
        return swingGate(true, gateName);
    }

    public LastAction closeGate(final String gateName) {
        return swingGate(false, gateName);
    }

    private LastAction swingGate(boolean open, String gateName) {


        String openClose = "open";

        if (!open) {
            openClose = "close";
        }

        MudLocationDirectionGate gateBetween = getGateNamed(gateName);

        // cannot open a non-existant gate
        if (gateBetween == null){
            return LastAction.createError("Are you sure you can " + openClose + " " + gateName + "?");
        }

        // cannot open a gate that is hidden normally
        if (!gateBetween.isVisible()) {
            // There is no gate... that I can see
            return LastAction.createError("I don't see anything to " + openClose + " that way");
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


    public List<VisibleGate> getVisibleGatesHere(final MudLocation whereAmI) {


        List<VisibleGate> visibleGates = new ArrayList<>();

        for (MudLocationExit exit : whereAmI.getExits()) {
            if(exit.isGated()) {
                MudLocationDirectionGate theGate = getGateNamed(exit.getGateName());
                if (theGate != null && theGate.isVisible()) {
                    VisibleGate visibleGate = new VisibleGate(exit.getDirection(), theGate.isOpen(), theGate.shortDescription(), theGate.closedDescription());
                    visibleGates.add(visibleGate);
                }
            }
        }

        return visibleGates;
    }
}
