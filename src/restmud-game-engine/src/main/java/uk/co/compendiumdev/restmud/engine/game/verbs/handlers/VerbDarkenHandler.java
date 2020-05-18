package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.verbs.VerbGameAbilities;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class VerbDarkenHandler implements VerbHandler {

    private String verbName="darken";

    @Override
    public VerbDarkenHandler setGame(MudGame mudGame) {
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {
        // User can darken rooms if items in inventory if they have the ability to darken

        if (!player.hasTheAbilityTo(VerbGameAbilities.ILLUMINATE_DARKEN)) {
            return LastAction.createError("You try to make things darker, but it doesn't work. You need to be using something that illuminates first!");
        }

        // get the thing that allows me to illuminate
        MudCollectable torch = player.grantedTheAbilityToBy(VerbGameAbilities.ILLUMINATE_DARKEN);

        // TODO: is there a danger that torch is null? If so handle that here

        if(!torch.getIsAbilityOn()){
            return LastAction.createError("You try to make things darker, but you have not illuminated anything!");
        }

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
        return true;
    }

    @Override
    public boolean actionUpdatesTimeStamp() {
        return true;
    }

    @Override
    public VerbDarkenHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }
}