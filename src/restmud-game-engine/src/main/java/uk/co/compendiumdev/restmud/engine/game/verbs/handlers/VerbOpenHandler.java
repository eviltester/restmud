package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.Directions;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationExit;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;


public class VerbOpenHandler implements VerbHandler {
    private MudGame game;
    private String verbName="open";

    @Override
    public VerbOpenHandler setGame(MudGame mudGame) {
        this.game = mudGame;
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        // is the nounphrase a direction?
        String baseDirection = game.getDirections().findBaseDirection(nounPhrase.toLowerCase());
        if(baseDirection.length() == 0){
            return LastAction.createError("I can't open " + nounPhrase);
        }
        MudLocation location = game.getGameLocations().get(player.getLocationId());

        // TODO: this doesn't actually check for gates, it checks for exits
        if(location.canGo(baseDirection) ){
            MudLocationExit exit = location.exitFor(baseDirection);
            if(exit!=null && exit.isLocal()){
                // no local handling for gates yet
                return LastAction.createError("I don't know how to open that way");
            }
        }

        return game.getGateManager().openGate(location, baseDirection);

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

    @Override
    public VerbOpenHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }

}
