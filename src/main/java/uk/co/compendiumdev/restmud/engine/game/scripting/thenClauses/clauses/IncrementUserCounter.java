package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.*;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;

/**
 * Created by Alan on 07/08/2016.
 */
public class IncrementUserCounter implements ScriptThenCommand {
    private final MudGame game;

    public IncrementUserCounter(MudGame game) {
        this.game = game;
    }

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
