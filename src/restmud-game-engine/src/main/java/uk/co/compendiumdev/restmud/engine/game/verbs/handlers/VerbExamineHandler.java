package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbExamineHandler implements VerbHandler {

    private MudGame game;
    private String verbName;

    @Override
    public VerbExamineHandler setGame(MudGame mudGame) {
        this.game = mudGame;
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        // does thing exist?
        String thingId = nounPhrase;

        MudLocation whereAmI = game.getGameLocations().get(player.getLocationId());

        MudLocationObject locationObject = game.getLocationObjects().get(thingId);

        MudCollectable collectable = game.getGameCollectables().get(thingId);

        return examine(player, whereAmI, locationObject, collectable, nounPhrase);

    }

    private LastAction examine(MudUser player, MudLocation whereAmI, MudLocationObject locationObject, MudCollectable collectable, String nounPhrase) {

        // is it a location object here?
        if (locationObject != null) {
            if (!whereAmI.objects().contains(locationObject.getObjectId())) {
                return LastAction.createError(String.format("You wanted to examine %s: but I don't see %s here", locationObject.getObjectId(), locationObject.getDescription()));
            } else {
                return LastAction.createSuccess(
                        String.format("You Examine: %s ... %s",
                                locationObject.getDescription(),
                                locationObject.getExamineDescription()));
            }
        }

        // it was not a location item, so is it a collectable
        if (collectable != null) {
            // was it here or am I carrying it
            if (whereAmI.collectables().contains(collectable.getCollectableId()) || player.inventory().contains(collectable.getCollectableId())) {
                return LastAction.createError(String.format("You examined %s but there is nothing more to say about %s.", collectable.getCollectableId(), collectable.getDescription()));
            } else {
                return LastAction.createError(String.format("You wanted to examine %s: but I don't see that here", collectable.getCollectableId()));
            }
        }

        return LastAction.createError(String.format("Sorry, you want to examine what? What is a %s", nounPhrase));
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
    public VerbExamineHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }
}
