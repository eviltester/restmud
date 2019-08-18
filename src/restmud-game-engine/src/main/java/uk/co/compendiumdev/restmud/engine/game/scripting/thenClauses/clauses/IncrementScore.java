package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.ImmutableEmptyProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;

public class IncrementScore implements ScriptThenCommand {

    @Override
    public String getCommandName() {
        return Then.INCREMENT_SCORE;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        Integer inc = Integer.parseInt(scriptClause.getParameter());
        player.incrementScoreBy(inc.intValue());
        return ImmutableEmptyProcessConditionReturn.get();
    }
}
