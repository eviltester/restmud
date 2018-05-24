package uk.co.compendiumdev.restmud.engine.game.verbs.handlers.perPlayer;

import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 01/06/2016.
 */
public class VerbTake {
    private final MudUser player;

    public VerbTake(MudUser player) {
        this.player = player;
    }

    public LastAction removeFromLocationAndAddToInventory(MudLocation whereAmI, MudCollectable thing) {
        // does the thing grant any abilities?
        String extraMessage="";
        String thingAbility = thing.getAbilityName();

        if(thingAbility.length()>0){

            String abilityOn = "things";

            if(thingAbility.contains("/")){
                thingAbility = thingAbility.replaceFirst("/", "' and '");
                abilityOn="";
            }
            extraMessage=String.format(" , oooh, and you now have the ability to '%s' %s (power=%d).", thingAbility, abilityOn, thing.getAbilityPower());
        }

        if(whereAmI.moveCollectableToPlayerInventory(thing.getCollectableId(), player.inventory())){
            return LastAction.createSuccess(String.format("You took: %s. You now have the %s %s",thing.getCollectableId(), thing.getDescription(), extraMessage));
        }else{
            return LastAction.createError(String.format("You took: Nothing (I couldn't take %s)", thing.getCollectableId()));
        }

    }
}
