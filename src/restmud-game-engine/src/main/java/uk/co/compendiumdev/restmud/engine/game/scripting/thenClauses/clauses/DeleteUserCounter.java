package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.*;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;

/**
 * Created by Alan on 07/08/2016.
 */
public class DeleteUserCounter implements ScriptThenCommand {


    public DeleteUserCounter(MudGame game) {

    }

    @Override
    public String getCommandName() {
        return Then.DELETE_USER_COUNTER;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        ScriptableCounter counter = new ScriptableCounter(scriptClause.getParameter());
        player.getCounters().deleteCounter(counter.name);
        return ImmutableEmptyProcessConditionReturn.get();
    }
}
