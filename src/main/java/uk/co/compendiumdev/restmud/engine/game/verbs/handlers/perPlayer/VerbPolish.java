package uk.co.compendiumdev.restmud.engine.game.verbs.handlers.perPlayer;

import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

import java.util.Random;

/**
 * Created by Alan on 30/05/2016.
 */
public class VerbPolish {
    private final MudUser player;

    public VerbPolish(MudUser player) {
        this.player = player;
    }

    public LastAction now(MudCollectable thingToPolish, MudCollectable polisher, MudLocation junkRoom) {


        // Can only polish hoardable things

        if(!thingToPolish.isHoardable()){
            return LastAction.createError("Sorry, it would not be worthwhile polishing that!");
        }

        // generate a random polish amount at max your polish power
        int fullPower = polisher.getAbilityPower();
        int maxPower = fullPower/2;
        int minPower = 1;

        int polishPower;

        if(fullPower<10){
            polishPower = fullPower;
        }else{
            polishPower = new Random().nextInt(maxPower - minPower + 1) + minPower;
        }

        int hoardableAt = thingToPolish.getHoardableScore();
        hoardableAt = hoardableAt + polishPower;
        thingToPolish.canHoard(true, hoardableAt);

        int powerLeft = fullPower-polishPower;
        polisher.setAbilityPower(powerLeft);

        String polishResults =
                String.format("Good work. You polished '%s' by %d and now it is worth %d hoard points. Your '%s' has %d polish power left.",
                        thingToPolish.getDescription(), polishPower, hoardableAt,
                        polisher.getDescription(), powerLeft);

        String disappearMessage="";
        if(polishPower<=0){
            // it disappears
            disappearMessage = String.format(" Your '%s' vanishes, it must have been magic.", polisher.getDescription());
            junkRoom.moveCollectableFromPlayerInventory(polisher.getCollectableId(), player.inventory());
        }

        return LastAction.createSuccess(String.format("%s %s", polishResults, disappearMessage));
    }
}
