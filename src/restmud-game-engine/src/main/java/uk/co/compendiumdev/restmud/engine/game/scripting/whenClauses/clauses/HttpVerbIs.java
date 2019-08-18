package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;


public class HttpVerbIs implements ScriptWhenClause {


    public String getCommandName(){
        return When.HTTP_VERB_IS;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, PlayerCommand command) {

        if(command.getHttpDetails()==null){
            return false;
        }

        return command.getHttpDetails().httpverb.equalsIgnoreCase(when.getParameter());
    }

}
