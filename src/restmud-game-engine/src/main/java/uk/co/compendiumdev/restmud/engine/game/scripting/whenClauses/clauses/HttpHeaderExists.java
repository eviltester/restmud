package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;


public class HttpHeaderExists implements ScriptWhenClause {


    public String getCommandName(){
        return When.HTTP_HEADER_EXISTS;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, PlayerCommand command) {

        if(command.getHttpDetails()==null){
            return false;
        }

        for(String aheader : command.getHttpDetails().headers){
            if(aheader.toLowerCase().trim().startsWith(when.getParameter().toLowerCase())){
                return true;
            }
        }
        return false;
    }

}
