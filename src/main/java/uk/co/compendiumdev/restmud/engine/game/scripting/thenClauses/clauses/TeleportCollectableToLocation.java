package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.*;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;

public class TeleportCollectableToLocation implements ScriptThenCommand {
    private final MudGame game;

    public TeleportCollectableToLocation(MudGame game) {
        this.game = game;
    }

    @Override
    public String getCommandName() {
        return Then.TELEPORT_COLLECTABLE_TO_LOCATION;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        AttributePair attribs = new AttributePair(scriptClause.getParameter());
        game.moveCollectableToLocation(game.getGameCollectables().get(attribs.name),
                game.getGameLocations().get(attribs.value));
        return ImmutableEmptyProcessConditionReturn.get();
    }
}
