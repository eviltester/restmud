package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.InventoryReport;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

public class VerbInventoryHandlerTest {

    private static MudGame game;
    private static MudUser player;

    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1", "Room 1", "the room to the left", "");

            create.collectable("athing", "A Thing", "1");

            game.initFromDefinition(defn);

        }
    }

    @Before
    public void setupTheGame(){
        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.generate(new VerbHandlerGame());

        // generate does not add any users
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addDefaultUser("The Test User", "tester", "aPassword");

        // set the user back to the central room after each path
        game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1");

        player = game.getUserManager().getUser("tester");

    }

    @Test
    public void canSeeItemInInventoryIfITake(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertFalse(game.getGameLocations().get("1").collectables().contains("athing"));

        VerbInventoryHandler inventory = new VerbInventoryHandler().setGame(game);
        LastAction action = inventory.doVerb(player, "");
        Assert.assertTrue(action.isSuccess());
        final InventoryReport report = action.getInventoryReport();
        Assert.assertEquals(1,report.contents.length);
    }

    @Test
    public void canSeeNoItemsInInventoryIfINoneTaken(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));

        VerbInventoryHandler inventory = new VerbInventoryHandler().setGame(game);
        LastAction action = inventory.doVerb(player, "");
        Assert.assertTrue(action.isSuccess());
        final InventoryReport report = action.getInventoryReport();
        Assert.assertEquals(0,report.contents.length);
    }



    @Test
    public void verbHandlerConditions(){

        VerbInventoryHandler handler = new VerbInventoryHandler().setGame(game).usingCurrentVerb("i");
        Assert.assertTrue("Inventory takes time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("Inventory generates messages",
                handler.shouldAddGameMessages());
        Assert.assertFalse(
                "Inventory will not items here, so no need to look",
                handler.shouldLookAfterVerb());
    }
}
