package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;


public class HttpVerbIs implements ScriptWhenClause {

    private RestMudHttpRequestDetails httpdetails;

    public String getCommandName(){
        return When.HTTP_VERB_IS;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, String nounPhrase) {

        if(httpdetails==null){
            return false;
        }

        return httpdetails.httpverb.equalsIgnoreCase(when.getParameter());
    }

    @Override
    public ScriptWhenClause addHttpDetails(RestMudHttpRequestDetails details) {
        this.httpdetails = details;
        return this;
    }
}
