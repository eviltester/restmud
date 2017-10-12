package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;

/**
 * Created by Alan on 07/08/2016.
 */
public class ForceLook implements ScriptThenCommand {
    private final MudGame game;

    public ForceLook(MudGame game) {
        this.game= game;
    }

    @Override
    public String getCommandName() {
        return Then.FORCE_LOOK;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        ProcessConditionReturn ret = new ProcessConditionReturn();
        ret.weShouldForceALook();
        return ret;
    }
}
