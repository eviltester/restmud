package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.*;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;

/**
 * Created by Alan on 07/08/2016.
 */
public class DeleteUserFlag implements ScriptThenCommand {


    public DeleteUserFlag(MudGame game) {

    }

    @Override
    public String getCommandName() {
        return Then.DELETE_USER_FLAG;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        player.deleteUserFlag(new ScriptableFlag(scriptClause.getParameter()).name);
        return ImmutableEmptyProcessConditionReturn.get();
    }
}
