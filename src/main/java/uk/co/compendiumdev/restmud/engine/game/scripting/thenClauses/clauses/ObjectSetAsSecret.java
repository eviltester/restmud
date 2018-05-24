package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.*;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;

/**
 * Created by Alan on 07/08/2016.
 */
public class ObjectSetAsSecret implements ScriptThenCommand {
    private final MudGame game;

    public ObjectSetAsSecret(MudGame game) {
        this.game = game;
    }

    @Override
    public String getCommandName() {
        return Then.OBJECT_SET_SECRET;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        AttributePair attribs = new AttributePair(scriptClause.getParameter());
        MudLocationObject obj = game.getGameLocations().get(player.getLocationId()).objects().get(attribs.name);
        if(obj!=null){obj.setAsSecret(Boolean.parseBoolean(attribs.value));}
        return ImmutableEmptyProcessConditionReturn.get();
    }
}
