package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.generators.RandomTreasureGenerator;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses.support.ThenClauseRandomGenerator;


public class RandomlyGenerateNonHoardableJunk implements ScriptThenCommand {
    private final MudGame game;

    public RandomlyGenerateNonHoardableJunk(MudGame game) {
        this.game = game;
    }

    @Override
    public String getCommandName() {
        return Then.RANDOMLY_GENERATE_NON_HOARDABLE_JUNK;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {

        ProcessConditionReturn ret = new ProcessConditionReturn();

        RandomTreasureGenerator moneygod = ThenClauseRandomGenerator.configuredFor(game, scriptClause, getCommandName(), ret);

        int numberOfTreasures = ThenClauseRandomGenerator.getNumberToGenerateFrom(scriptClause, getCommandName(), ret);

        moneygod.createNonHoardableJunk(numberOfTreasures);

        return ret;
    }
}
