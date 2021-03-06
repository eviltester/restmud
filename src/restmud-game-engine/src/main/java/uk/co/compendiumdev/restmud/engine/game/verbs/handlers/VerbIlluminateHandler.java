package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.verbs.VerbGameAbilities;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class VerbIlluminateHandler implements VerbHandler{


    private String verbName="illuminate";

    @Override
    public VerbIlluminateHandler setGame(MudGame mudGame) {
        return this;
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

        // TODO: if there is danger that torch may be null then address that here

        if(torch.getIsAbilityOn()){
            return LastAction.createError("You try to make things easier to see, but it doesn't work. You have already illuminated something!");
        }

        return illuminate(torch);
    }

    public LastAction illuminate(MudCollectable torch) {
        torch.setAbilityOn(true);

        int power = torch.getAbilityPower();

        if(power>0){

            return LastAction.createSuccess(
                    String.format("Good work. You illuminated the '%s'. Your '%s' has %d power left.",
                            torch.getDescription(), torch.getDescription(), power));
        }else{

            return LastAction.createError(
                    String.format("No, that won't work. Your '%s' has no power left.",
                            torch.getDescription()));

        }
    }

    @Override
    public boolean shouldAddGameMessages() {
        return true;
    }

    @Override
    public boolean shouldLookAfterVerb() {
        return true;
    }

    @Override
    public boolean actionUpdatesTimeStamp() {
        return true;
    }

    @Override
    public VerbIlluminateHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }
}
