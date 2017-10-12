package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbHoardHandler  implements VerbHandler{
    private MudGame game;

    @Override
    public void setGame(MudGame mudGame) {
        this.game = mudGame;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        // does thing exist?
        String thingId = nounPhrase;

        MudCollectable theThing = game.getGameCollectables().get(thingId);
        if(theThing==null){
            return LastAction.createError((String.format("Sorry, I can't see %s anywhere", nounPhrase)));
        }

        // where am i?
        MudLocation whereAmI = game.getGameLocations().get(player.getLocationId());


        return player.hoard().here(whereAmI, theThing, nounPhrase);
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
