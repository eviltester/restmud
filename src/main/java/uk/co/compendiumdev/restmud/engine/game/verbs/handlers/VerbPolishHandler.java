package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbPolishHandler implements VerbHandler{
    private MudGame game;

    @Override
    public void setGame(MudGame mudGame) {
        this.game = mudGame;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {
        // User can polish items in inventory if they have the ability to polish

        MudCollectable thing = game.getGameCollectables().get(nounPhrase);
        if(thing==null){
            return LastAction.createError((String.format("Sorry, I can't see %s anywhere", nounPhrase)));
        }

        if(!player.inventory().contains(nounPhrase)){
            return LastAction.createError(String.format("Polish What? You don't have %s", nounPhrase));
        }

        if(!player.hasTheAbilityTo("polish")){
            return LastAction.createError("You try to polish it with your hand but it doesn't work. You need to carry something that let's you polish stuff!");
        }

        MudCollectable polisher = player.grantedTheAbilityToBy("polish");


        return player.polish().now(thing, polisher, game.getGameLocations().get(MudGame.JUNK_ROOM));
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
