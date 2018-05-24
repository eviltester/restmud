package uk.co.compendiumdev.restmud.engine.game.verbs.handlers.perPlayer;

import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 01/06/2016.
 */
public class VerbDrop {
    private final MudUser player;

    public VerbDrop(MudUser player) {
        this.player = player;
    }

    public LastAction thisThingHere(MudCollectable thing, MudLocation whereAmI) {

        String thingId = thing.getCollectableId();
        // am I carrying it?
        if(!player.inventory().contains(thingId)) {
            return LastAction.createError(String.format("You dropped: Nothing (You are not carrying %s)", thingId));
        }

        // drop it here? where am i?
        if(whereAmI.moveCollectableFromPlayerInventory(thingId, player.inventory())){
            return LastAction.createSuccess(String.format("You dropped: %s. You are no longer carrying %s.", thing.getCollectableId(),thing.getDescription()));
        }else{
            return LastAction.createError(String.format("You dropped: Nothing (You couldn't drop %s now)", thingId));
        }
    }
}
