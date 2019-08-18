package uk.co.compendiumdev.restmud.engine.game.playerevents;

import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.MudUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 01/06/2016.
 */
public class PlayerEvents {

    public static List<BroadcastGameMessage> updateActivePowerItems(List<MudCollectable> collectablesFrom, MudUser player, MudLocation junkRoom) {
        // run through each active power item and decrease its power

        List<BroadcastGameMessage> messages = new ArrayList<>();

        for(MudCollectable collectable : collectablesFrom){
            if(itBestowsAnAbilityWhichCostsPowerWhenOn(collectable)) {
                    int power = collectable.getAbilityPower();
                    power = power - 1;
                    collectable.setAbilityPower(power);

                    messages.add(new BroadcastGameMessage(System.currentTimeMillis(),
                            String.format("Your '%s' is working and now '%s' has %d power",
                                    collectable.getDescription(), collectable.getCollectableId(), power)));

                    // remove abilities here
                    if (power <= 0) {
                        // poof it vanishes
                        messages.add(new BroadcastGameMessage(System.currentTimeMillis(),
                                String.format("Your '%s' has %d power left and has disappeared",
                                        collectable.getDescription(), power)));


                        junkRoom.moveCollectableFromPlayerInventory(collectable.getCollectableId(), player.inventory());
                    }
                }
        }

        return messages;
    }

    private static boolean itBestowsAnAbilityWhichCostsPowerWhenOn(MudCollectable collectable) {
        return  collectable.getAbilityName().length() > 0 &&
                collectable.getIsAbilityToggleable() &&
                collectable.getIsAbilityOn();
    }
}
