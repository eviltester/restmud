package uk.co.compendiumdev.restmud.engine.game.verbs.handlers.perPlayer;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;

/**
 * Created by Alan on 04/06/2016.
 */
public class VerbExamine {
    private final MudUser player;

    public VerbExamine(MudUser player) {
        this.player = player;
    }

    public LastAction here(MudLocation whereAmI, MudLocationObject locationObject, MudCollectable collectable, String nounPhrase) {


        // is it a location object here?
        if(locationObject!=null){
            if(!whereAmI.objects().contains(locationObject.getObjectId())) {
                return LastAction.createError(String.format("You wanted to examine %s: but I don't see %s here", locationObject.getObjectId(), locationObject.getDescription()));
            } else {
                return LastAction.createSuccess(
                        String.format("You Examine: %s ... %s",
                                locationObject.getDescription(),
                                locationObject.getExamineDescription()));
            }
        }

        // it was not a location item, so is it a collectable
        if(collectable!=null) {
            // was it here or am I carrying it
            if(whereAmI.collectables().contains(collectable.getCollectableId()) || player.inventory().contains(collectable.getCollectableId())) {
                return  LastAction.createError(String.format("You examined %s but there is nothing more to say about %s.", collectable.getCollectableId(), collectable.getDescription()));
            }else{
                return LastAction.createError(String.format("You wanted to examine %s: but I don't see that here", collectable.getCollectableId()));
            }
        }

        return LastAction.createError(String.format("Sorry, you want to examine what? What is a %s", nounPhrase));
    }
}
