package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.ImmutableEmptyProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptableCounter;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;

public class IncrementUserCounter implements ScriptThenCommand {

    @Override
    public String getCommandName() {
        return Then.INCREMENT_USER_COUNTER;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        ScriptableCounter counter = new ScriptableCounter(scriptClause.getParameter());
        player.incrementUserCounter(counter.name, counter.getValue());
        return ImmutableEmptyProcessConditionReturn.get();
    }
}
