package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.InventoryReport;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class VerbInventoryHandler  implements VerbHandler {

    private String verbName;

    @Override
    public VerbInventoryHandler setGame(MudGame mudGame) {
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        return getInventory(player);
    }

    private LastAction getInventory(final MudUser player) {
        String lastActionDescription = player.inventory().asStringListOfDescriptions("You Are Carrying:");

        LastAction lastAction = LastAction.createSuccess(lastActionDescription);

        lastAction.setInventoryReport(new InventoryReport(player.userName(), player.inventory().asIdDescriptionPairs()));

        return lastAction;
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
    public VerbInventoryHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }
}
