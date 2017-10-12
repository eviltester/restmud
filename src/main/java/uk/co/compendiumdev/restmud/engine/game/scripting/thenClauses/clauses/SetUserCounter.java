package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.*;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;

/**
 * Created by Alan on 07/08/2016.
 */
public class SetUserCounter implements ScriptThenCommand {
    private final MudGame game;

    public SetUserCounter(MudGame game) {
        this.game = game;
    }

    @Override
    public String getCommandName() {
        return Then.SET_USER_COUNTER;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        player.setUserCounter(new ScriptableCounter(scriptClause.getParameter()));
        return ImmutableEmptyProcessConditionReturn.get();
    }
}
