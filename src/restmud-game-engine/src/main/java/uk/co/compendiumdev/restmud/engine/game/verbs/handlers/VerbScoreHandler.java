package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.GetUserDetails;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

import java.util.ArrayList;
import java.util.List;

public class VerbScoreHandler implements VerbHandler{

    @Override
    public VerbScoreHandler setGame(MudGame mudGame) {
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        return reportScore(player);
    }


    private LastAction reportScore(MudUser player) {
        LastAction action = LastAction.createSuccess("Your Score is: " + player.getScore());

        List<GetUserDetails> details = new ArrayList<GetUserDetails>();
        GetUserDetails userDetails = new GetUserDetails(player.userName(), player.displayName(), null, player.getScore());
        details.add(userDetails);

        action.setUsersDetails(details);
        return action;
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
