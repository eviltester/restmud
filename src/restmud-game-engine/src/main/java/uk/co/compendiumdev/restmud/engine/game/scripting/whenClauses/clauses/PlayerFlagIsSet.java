package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptableFlag;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;


public class PlayerFlagIsSet implements ScriptWhenClause {


    private final ScriptableFlag flag;

    public PlayerFlagIsSet() {

        this.flag = ScriptableFlag.empty();
    }

    public String getCommandName(){
        return When.IS_USER_FLAG_SET;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, PlayerCommand command) {

        // re-use existing flag
        flag.setFrom(when.getParameter());

        Boolean lastMatch = false;

        if(player.userFlagExists(flag.name)){
            lastMatch = player.getUserFlag(flag.name)==flag.getValue();
        }

        return lastMatch;


    }

}
