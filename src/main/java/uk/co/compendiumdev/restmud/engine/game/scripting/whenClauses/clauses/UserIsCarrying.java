package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;


public class UserIsCarrying implements ScriptWhenClause {

    public String getCommandName(){
        return When.USER_IS_CARRYING;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, PlayerCommand command) {

        return player.inventory().contains(when.getParameter());
    }

}
