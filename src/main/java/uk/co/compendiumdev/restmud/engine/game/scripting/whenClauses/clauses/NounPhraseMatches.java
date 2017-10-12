package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;


public class NounPhraseMatches implements ScriptWhenClause {
    private final MudGame game;

    public NounPhraseMatches(MudGame game) {
        this.game = game;
    }

    public String getCommandName(){
        return When.NOUNPHRASE_EQUALS;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, String nounPhrase) {

        return when.getParameter().contentEquals(nounPhrase);
    }

    @Override
    public ScriptWhenClause addHttpDetails(RestMudHttpRequestDetails details) {
        return this;
    }
}
