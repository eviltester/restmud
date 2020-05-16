package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class DefaultVerbHandler implements VerbHandler{


    private String verbName="";

    @Override
    public DefaultVerbHandler setGame(MudGame mudGame) {
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {
        // by default this is an error if we didn't create a LastAction in the custom verb handling condition
        return LastAction.createError(String.format("I don't know how to \"%s %s\" here", verbName, nounPhrase));
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

    @Override
    public DefaultVerbHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }

}
