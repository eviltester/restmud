package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.scripting.*;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;

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
        moveCollectableToLocation(game.getGameCollectables().get(attribs.name),
                game.getGameLocations().get(attribs.value));
        return ImmutableEmptyProcessConditionReturn.get();
    }

    /**
     * given a collectable, move it to location, ensure that it only exists in this location
     * @param collectable
     * @param location
     */
    public boolean moveCollectableToLocation(MudCollectable collectable, MudLocation location) {

        // remove the collectable from any other location
        for(MudLocation otherLocation : game.getGameLocations().getLocations()){
            if(otherLocation.collectables().contains(collectable)){
                otherLocation.collectables().removeItem(collectable);
                // TODO: create event saying it zapped from the room for any players in the room
            }
        }

        // remove the collectable from any player inventories
        for(MudUser user : game.getUserManager().getUsers()){
            if(user.inventory().contains(collectable)){
                user.inventory().removeItem(collectable);
                // TODO: create event for the player saying it has vanished
            }
        }

        return location.collectables().addItem(collectable);
    }
}
