package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class VerbDropHandler implements VerbHandler {

    private MudGame game;
    private String verbName="drop";

    @Override
    public VerbDropHandler setGame(MudGame mudGame) {
        this.game = mudGame;
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        LastAction lastAction;

        // does thing exist?

        MudCollectable thing = game.getGameCollectables().get(nounPhrase);
        if (thing == null) {
            return LastAction.createError((String.format("Sorry, I can't see %s anywhere", nounPhrase)));
        }

        MudLocation whereAmI = game.getGameLocations().get(player.getLocationId());

        return drop(player, thing, whereAmI);
    }

    private LastAction drop(MudUser player, MudCollectable thing, MudLocation whereAmI) {

        String thingId = thing.getCollectableId();
        // am I carrying it?
        if (!player.inventory().contains(thingId)) {
            return LastAction.createError(String.format("You dropped: Nothing (You are not carrying %s)", thingId));
        }

        // drop it here? where am i?
        if (whereAmI.moveCollectableFromPlayerInventory(thingId, player.inventory())) {
            return LastAction.createSuccess(String.format("You dropped: %s. You are no longer carrying %s.", thing.getCollectableId(), thing.getDescription()));
        } else {
            // because it is multi-user, this is to cover the tiny chance that
            // the object was teleported out of my inventory as I was in the process of dropping it
            // or the location was blocked from moving items into
            return LastAction.createError(String.format("You dropped: Nothing (You couldn't drop %s now)", thingId));
        }
    }

    @Override
    public boolean shouldAddGameMessages() {
        return true;
    }

    @Override
    public boolean shouldLookAfterVerb() {
        return false;
    }

    @Override
    public boolean actionUpdatesTimeStamp() {
        return true;
    }

    @Override
    public VerbDropHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }
}