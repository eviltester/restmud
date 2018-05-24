package uk.co.compendiumdev.restmud.engine.game.verbs.handlers.perPlayer;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.InventoryReport;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbInventory {
    private final MudUser player;

    public VerbInventory(MudUser mudUser) {
        this.player = mudUser;
    }

    public LastAction getInventory() {
        String lastActionDescription = player.inventory().asStringListOfDescriptions("You Are Carrying:");

        LastAction lastAction = LastAction.createSuccess(lastActionDescription);


        lastAction.setInventoryReport(new InventoryReport(player.userName(), player.inventory().asIdDescriptionPairs()));

        return lastAction;
    }
}
