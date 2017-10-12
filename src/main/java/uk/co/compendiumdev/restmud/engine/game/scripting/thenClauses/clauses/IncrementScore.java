package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.ImmutableEmptyProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;

/**
 * Created by Alan on 07/08/2016.
 */
public class IncrementScore implements ScriptThenCommand {
    private final MudGame game;

    public IncrementScore(MudGame game) {
        this.game = game;
    }

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
