package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbScoreHandler implements VerbHandler{

    @Override
    public void setGame(MudGame mudGame) {
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        return player.handleScoreVerb().reportScore();
    }

    @Override
    public boolean shouldAddGameMessages() {
        return false;
    }

    @Override
    public boolean shouldLookAfterVerb() {
        return false;
    }

    @Override
    public boolean actionUpdatesTimeStamp() {
        return false;
    }
}
