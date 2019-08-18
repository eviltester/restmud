package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.verbs.VerbGameAbilities;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbIlluminateHandler implements VerbHandler{


    @Override
    public void setGame(MudGame mudGame) {
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {
        // Verb Guards - user exists, can illuminate, have a thing that allows illumination

        // User can illuminate rooms if items in inventory if they have the ability to illuminate

        if(!player.hasTheAbilityTo(VerbGameAbilities.ILLUMINATE_DARKEN)){
            return LastAction.createError("You try to make things easier to see, but it doesn't work. You need to carry something that let's you light up rooms!");
        }

        // get the thing that allows me to illuminate
        MudCollectable torch = player.grantedTheAbilityToBy(VerbGameAbilities.ILLUMINATE_DARKEN);

        return player.illuminate().using(torch);
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
