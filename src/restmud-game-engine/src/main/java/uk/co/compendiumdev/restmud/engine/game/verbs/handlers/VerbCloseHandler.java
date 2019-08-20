package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.Directions;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbCloseHandler implements VerbHandler {
    private MudGame game;

    @Override
    public void setGame(MudGame mudGame) {
        this.game = mudGame;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {
        // we should be able to close gates between locations
        // at some point we will be able to clolse 'things'

        // is the nounphrase a direction?
        String baseDirection = game.getDirections().findBaseDirection(nounPhrase.toLowerCase());
        if(baseDirection.length() == 0){
            return LastAction.createError("I can't close " + nounPhrase);
        }

        MudLocation location = game.getGameLocations().get(player.getLocationId());

        // is there even a gate there?
        if(location.canGo(baseDirection) ){
            String destinationId = location.destinationFor(baseDirection);
            if (destinationId.contentEquals("local")) {
                // no local handling for gates yet
                return LastAction.createError("I don't know how to close that way");
            }
        }

        return game.getGateManager().closeGate(location, baseDirection);
    }

    @Override
    public boolean shouldAddGameMessages() {
        return true;
    }

    @Override
    public boolean shouldLookAfterVerb() {
        return true;
    }

    @Override
    public boolean actionUpdatesTimeStamp() {
        return true;
    }
}
