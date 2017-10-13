package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;


public class HttpHeaderExists implements ScriptWhenClause {

    private RestMudHttpRequestDetails httpdetails;

    public String getCommandName(){
        return When.HTTP_HEADER_EXISTS;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, String nounPhrase) {

        if(httpdetails==null){
            return false;
        }

        for(String aheader : httpdetails.headers){
            if(aheader.toLowerCase().trim().startsWith(when.getParameter().toLowerCase())){
                return true;
            }
        }
        return false;
    }

    @Override
    public ScriptWhenClause addHttpDetails(RestMudHttpRequestDetails details) {
        this.httpdetails = details;
        return this;
    }
}
