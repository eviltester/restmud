package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class DefaultVerbHandler implements VerbHandler{


    @Override
    public void setGame(MudGame mudGame) {
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {
        // by default we fail to do anything
        return null;
        //return LastAction.createError("Sorry I couldn't do that");
    }

    @Override
    public boolean shouldAddGameMessages() {
        return true;
    }

    @Override
    public boolean shouldLookAfterVerb() {
        return false;
    }

    @Override
    public boolean actionUpdatesTimeStamp() {
        return true;
    }
}
