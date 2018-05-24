package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;


public class NounPhraseMatches implements ScriptWhenClause {

    public String getCommandName(){
        return When.NOUNPHRASE_EQUALS;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, PlayerCommand command) {

        return when.getParameter().equalsIgnoreCase(command.getNounPhrase());
    }

}
