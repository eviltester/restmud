package uk.co.compendiumdev.restmud.engine.game.verbs.handlers.perPlayer;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;

/**
 * Created by Alan on 04/06/2016.
 */
public class VerbHoard {
    private final MudUser player;

    public VerbHoard(MudUser player) {
        this.player = player;
    }

    public LastAction here(MudLocation whereAmI, MudCollectable theThing, String nounPhrase) {

        if(theThing==null){
            return LastAction.createError(String.format("What? I don't know what a %s is", nounPhrase));
        }

        String lastActionDescription = "You hoarded:";

        // am I carrying it?
        if(!player.inventory().contains(theThing.getCollectableId())) {
            return LastAction.createError(String.format("%s nothing (You are not carrying %s)", lastActionDescription, theThing.getCollectableId()));
        }

        if(!theThing.isHoardable()) {
            return LastAction.createError(String.format("%s nothing (%s can not be hoarded)", lastActionDescription, theThing.getCollectableId()));
        }

        if (!whereAmI.canHoardTreasureHere()) {
            return LastAction.createError(String.format("%s nothing (you couldn't hoard %s her because there is no hoard here", lastActionDescription, theThing.getCollectableId()));
        }

        if (!player.inventory().removeItem(theThing.getCollectableId())) {
            return LastAction.createError(String.format("%s nothing (you don't own %s now)", lastActionDescription, theThing.getCollectableId()));
        }

        if (!player.treasureHoard().addItem(theThing)) {
            return LastAction.createError(String.format("%s nothing (you can't hoard %s now)", lastActionDescription, theThing.getCollectableId()));
        }

        int scored = theThing.getHoardableScore();
        player.incrementScoreBy(scored);

        return LastAction.createSuccess(String.format("%s %s [scored %d]", lastActionDescription, theThing.getCollectableId(), scored));

    }
}
