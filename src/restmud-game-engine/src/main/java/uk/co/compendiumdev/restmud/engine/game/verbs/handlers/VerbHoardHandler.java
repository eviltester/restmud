package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;


public class VerbHoardHandler  implements VerbHandler{
    private MudGame game;
    private String verbName;

    @Override
    public VerbHoardHandler setGame(MudGame mudGame) {
        this.game = mudGame;
        return this;
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


        return hoard(player, whereAmI, theThing, nounPhrase);
    }

    private LastAction hoard(MudUser player, MudLocation whereAmI, MudCollectable theThing, String nounPhrase) {

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

        // removing it might fail if it was stolen before we get here
        if (!player.inventory().removeItem(theThing.getCollectableId())) {
            return LastAction.createError(String.format("%s nothing (you don't own %s now)", lastActionDescription, theThing.getCollectableId()));
        }

        // if it cannot be added for some reason
        if (!player.treasureHoard().addItem(theThing)) {
            return LastAction.createError(String.format("%s nothing (you can't hoard %s now)", lastActionDescription, theThing.getCollectableId()));
        }

        int scored = theThing.getHoardableScore();
        player.incrementScoreBy(scored);

        return LastAction.createSuccess(String.format("%s %s [scored %d]", lastActionDescription, theThing.getCollectableId(), scored));

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
    public VerbHoardHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }
}
