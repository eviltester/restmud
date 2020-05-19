package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.Directions;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationDirectionGate;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationExit;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class VerbGoHandler   implements VerbHandler {
    private MudGame game;
    private String verbName="go";

    @Override
    public VerbGoHandler setGame(MudGame mudGame) {
        this.game = mudGame;
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        MudLocation location = game.getGameLocations().get(player.getLocationId());

        LastAction lastAction;

        String baseDirection = game.getDirections().findBaseDirection(nounPhrase.toLowerCase());

        String goAppendum = "";

        if(location.canGo(baseDirection)){

            MudLocationExit exit = location.exitFor(baseDirection);

            if(exit.isLocal()){
                // delegate movement to the game itself
                // handled by a VerbCondition or a LocationCondition
                // if we are here then the condition didn't handle it and add LastAction so
                // report that we can't go that way
                lastAction = LastAction.createError("I can't go that way at the moment");
            }else {

                MudLocationDirectionGate gateBetween=null;

                if(exit.isGated()){
                    // I can only go through visible gates
                    gateBetween = game.getGateManager().getGateNamed(exit.getGateName());

                    if(gateBetween == null) {
                        System.out.println(String.format("WARNING EXIT HAS GATE BUT NO GATE FOUND named %s in location %s",
                                            exit.getGateName(), location.getLocationId()));
                    }else{
                        return goThroughGate(player, gateBetween, baseDirection);
                    }
                }


                MudLocation destination = game.getGameLocations().get(exit.getDestinationId());

                // TODO: destination would only be null if we could not find it above
                //  so we should have a syntax check in the definition to make sure that all defined destinations exist
                // to make this null check unnecessary
                if(destination==null){
                    lastAction = LastAction.createError("You can't go " + baseDirection + goAppendum);
                }else {
                    player.setLocationId(destination.getLocationId());
                    lastAction = LastAction.createSuccess("You go " + baseDirection + goAppendum);
                }
            }

        }else{
            lastAction = LastAction.createError("You can't go " + nounPhrase);
        }

        return lastAction;
    }

    @Override
    public boolean shouldAddGameMessages() {
        return true;
    }


    @Override
    public boolean actionUpdatesTimeStamp() {
        return true;
    }


    @Override
    public boolean shouldLookAfterVerb() {
        return true;
    }

    private LastAction goThroughGate(MudUser user, MudLocationDirectionGate gateBetween, String direction) {

        String goAppendum;

        if(!gateBetween.isOpen()){
            // OK we have a problem then because the gate is not open
            return LastAction.createError("You can't go " + direction + " because " + gateBetween.shortDescription() + " is " + gateBetween.closedDescription());

        }

        // let's say we went through
        goAppendum = " " + gateBetween.throughDescription() + " " + gateBetween.shortDescription();
        String locationId = gateBetween.getLocationIdFor(direction);
        // TODO: this guard condition is only required if we got the gate definitions incorrect
        // and the destination listed does not exist, add syntax checks in definition to avoid this issue
        if(locationId!=null && locationId.length()>0) {
            user.setLocationId(locationId);
            gateBetween.goThrough();
            if(gateBetween.getAutoCloses()){
                goAppendum += ". And " + gateBetween.shortDescription() + " closes behind you.";
            }
            return LastAction.createSuccess("You go " + direction + goAppendum);
        }else{
            return LastAction.createError("You can't go " + direction);
        }
    }

    @Override
    public VerbGoHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }

}
