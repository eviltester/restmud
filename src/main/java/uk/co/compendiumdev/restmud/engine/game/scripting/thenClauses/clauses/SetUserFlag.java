package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.*;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;

/**
 * Created by Alan on 07/08/2016.
 */
public class SetUserFlag implements ScriptThenCommand {
    private final MudGame game;

    public SetUserFlag(MudGame game) {
        this.game = game;
    }

    @Override
    public String getCommandName() {
        return Then.SET_USER_FLAG;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {

        player.setUserFlag( new ScriptableFlag(scriptClause.getParameter()));
        return ImmutableEmptyProcessConditionReturn.get();
    }
}
