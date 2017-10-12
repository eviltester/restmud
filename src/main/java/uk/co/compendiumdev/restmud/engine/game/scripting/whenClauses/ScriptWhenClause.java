package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses;


import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;

public interface ScriptWhenClause {
    String getCommandName();
    boolean execute(ScriptClause when, MudUser player, String nounPhrase);
    ScriptWhenClause addHttpDetails(RestMudHttpRequestDetails details);
}
