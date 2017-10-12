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
public class TeleportUserToLocation implements ScriptThenCommand {
    private final MudGame game;

    public TeleportUserToLocation(MudGame game) {
        this.game = game;
    }

    @Override
    public String getCommandName() {
        return Then.TELEPORT_PLAYER_TO_LOCATION;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        player.setLocationId(scriptClause.getParameter());
        return ImmutableEmptyProcessConditionReturn.get();
    }
}
