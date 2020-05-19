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

    public void addAllGatesToLocations(final Locations gameLocations) {

        for(MudLocationDirectionGate gate : gates.values()){

            if(gate.getFromLocationId()==null){
                // gate is supposed to already be allocated to location
            }else {
                // using old format of gates so location might not know about the gate


                MudLocationExit fromExit = gameLocations.get(gate.fromLocation.getLocationId()).
                        exitFor(gate.getFromDirection());

                if (fromExit == null) {
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

                if (toExit != null) {
                    if (gate.getWhichWayDirection() != GateDirection.ONE_WAY) {
                        toExit.setGateName(gate.getGateName());
                    } else {
                        System.out.println("Do not add a gate on exit route if it is one way");
                    }

                } else {
                    // may have been a one way gate
                    if (gate.getWhichWayDirection() != GateDirection.ONE_WAY) {
                        // todo: handle and report possible syntax error no to location
                        System.out.println("Found a gate without a way back that is not defined as one way: " + gate.getGateName());

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
