package uk.co.compendiumdev.restmud.engine.game.verbs.handlers.perPlayer;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.GetUserDetails;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alan on 09/08/2016.
 */
public class VerbScore {
    private final MudUser player;

    public VerbScore(MudUser mudUser) {
        player = mudUser;
    }

    public LastAction reportScore() {
        LastAction action = LastAction.createSuccess("Your Score is: " + player.getScore());

        List<GetUserDetails> details = new ArrayList<GetUserDetails>();
        GetUserDetails userDetails = new GetUserDetails(player.userName(), player.displayName(), null, player.getScore());
        details.add(userDetails);

        action.setUsersDetails(details);
        return action;
    }
}
