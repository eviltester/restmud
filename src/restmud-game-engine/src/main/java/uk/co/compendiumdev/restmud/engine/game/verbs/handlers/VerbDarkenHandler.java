package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.verbs.VerbGameAbilities;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class VerbDarkenHandler  implements VerbHandler {

    @Override
    public VerbDarkenHandler setGame(MudGame mudGame) {
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {
        // User can darken rooms if items in inventory if they have the ability to darken

        if(!player.hasTheAbilityTo(VerbGameAbilities.ILLUMINATE_DARKEN)){
            return LastAction.createError("You try to make things darker, but it doesn't work. You need to be using something that illuminates first!");
        }

        // get the thing that allows me to illuminate
        MudCollectable torch = player.grantedTheAbilityToBy(VerbGameAbilities.ILLUMINATE_DARKEN);


        return darken(player, torch);
    }

    private LastAction darken(MudUser player, MudCollectable torch) {
        torch.setAbilityOn(false);

        String results =
                String.format("Good work. You extinguished the '%s'. Your '%s' has %d power left.",
                        torch.getDescription(), torch.getDescription(), torch.getAbilityPower());

        return LastAction.createSuccess(results);
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
