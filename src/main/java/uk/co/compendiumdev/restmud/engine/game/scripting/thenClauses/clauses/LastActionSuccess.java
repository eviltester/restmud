package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;


public class LastActionSuccess implements ScriptThenCommand {

    public String getCommandName(){
        return Then.LAST_ACTION_SUCCESS;
    }

    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player){
        ProcessConditionReturn ret = new ProcessConditionReturn();
        ret.addNewAction(LastAction.createSuccess(scriptClause.getParameter()));
        return ret;
    }


}
