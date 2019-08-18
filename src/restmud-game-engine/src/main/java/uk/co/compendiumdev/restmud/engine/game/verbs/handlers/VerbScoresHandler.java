package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.GetUserDetails;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbScoresHandler implements VerbHandler{
    private MudGame game;

    @Override
    public void setGame(MudGame mudGame) {
        this.game = mudGame;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {
        LastAction action = LastAction.createSuccess("You checked the scores");

        List<GetUserDetails> details = new ArrayList<GetUserDetails>();

        for(MudUser user : game.getUserManager().getUsers()){
            GetUserDetails userDetails = new GetUserDetails(user.userName(), user.displayName(), null, user.getScore());
            details.add(userDetails);
        }

        action.setUsersDetails(details);
        return action;
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
        return false;
    }
}
