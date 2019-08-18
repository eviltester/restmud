package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.Directions;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationDirectionGate;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbGoHandler   implements VerbHandler {
    private MudGame game;

    @Override
    public void setGame(MudGame mudGame) {
        this.game = mudGame;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        MudLocation location = game.getGameLocations().get(player.getLocationId());

        LastAction lastAction;

        String baseDirection = Directions.findBaseDirection(nounPhrase.toLowerCase());

        String goAppendum = "";

        if(location.canGo(baseDirection)){

            String destinationId = location.destinationFor(baseDirection);
            if(destinationId.contentEquals("local")){
                // delegate movement to the game itself
                // handled by a VerbCondition or a LocationCondition
                lastAction = LastAction.createError("I can't go that way at the moment");
            }else {

                MudLocation destination = game.getGameLocations().get(destinationId);

                // is there a gate between here and there?
                MudLocationDirectionGate gateBetween = game.getGateManager().getGateBetween(location, destination);

                if(gateBetween != null) {
                    // can the gate impact my direction of travel?
                    if (gateBetween.mightBlock(location, destination, baseDirection)) {
                        return goThroughGate(player, gateBetween, baseDirection);
                    }
                }

                if(destination==null){
                    lastAction = LastAction.createError("You can't go " + baseDirection + goAppendum);
                }else {
                    player.setLocationId(destination.getLocationId());
                    lastAction = LastAction.createSuccess("You go " + baseDirection + goAppendum);
                }
            }

        }else{

            // perhaps it is a secret gate and not a direction?
            MudLocationDirectionGate gateBetween = game.getGateManager().getGateGoingFromHereInThatDirection(location, baseDirection);
            if(gateBetween!= null) {
                if(gateBetween.isVisible()){
                    return goThroughGate(player, gateBetween, baseDirection);
                }
            }

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
}
