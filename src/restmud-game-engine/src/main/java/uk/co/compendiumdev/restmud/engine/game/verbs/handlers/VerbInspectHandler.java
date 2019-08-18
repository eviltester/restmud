package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbInspectHandler  implements VerbHandler {
    private MudGame game;

    @Override
    public void setGame(MudGame mudGame) {
        this.game = mudGame;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        MudLocation whereAmI = game.getGameLocations().get(player.getLocationId());

        // does thing exist?
        String thingId = nounPhrase;

        // is it a collectable that is carried?

        MudCollectable collectable = game.getGameCollectables().get(thingId);

        if(collectable==null) {
            return LastAction.createError(String.format("Sorry I don't know how to inspect %s", nounPhrase));
        }


        return player.inspect().aCollectable(whereAmI, collectable, nounPhrase);
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

}
