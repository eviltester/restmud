package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectableTemplate;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

public class VerbUseHandlerTest {

    private static MudGame game;
    private static MudUser player;

    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1", "Room 1", "the room to the left", "e:2,s:3");
            create.location("2","Room 2", "the room to the right", "w:1");

            // TODO: dispensers should be able to empty i.e. max number of things to dispense - wizard can refill
            // TODO: dispensers should be able to break - wizard can fix
            // TODO: dispensers should be able to have time limits between vending items
            create.dispenser("1", "vendingmachine", "a vending machine", "it vends things").
                    setDispenserTemplate(new MudCollectableTemplate("shiny", "ashiny thing").
                            setHoardableAttributes(true, 1,100));

            create.locationObject("fixedthing", "A non-takeable thing", "fixed to floor",  "1");

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
    public void canUseItem(){

        Assert.assertTrue(game.getGameLocations().get("1").objects().contains("vendingmachine"));
        Assert.assertFalse(game.getGameLocations().get("2").objects().contains("vendingmachine"));

        Assert.assertEquals(0,game.getGameLocations().get("1").collectables().itemsView().size());

        VerbUseHandler use = new VerbUseHandler().setGame(game);
        final LastAction action = use.doVerb(player, "vendingmachine");
        Assert.assertTrue(action.isSuccess());

        Assert.assertEquals(1,game.getGameLocations().get("1").collectables().itemsView().size());

        // use it again
        Assert.assertTrue(use.doVerb(player, "vendingmachine").isSuccess());
        Assert.assertEquals(2,game.getGameLocations().get("1").collectables().itemsView().size());
    }


    @Test
    public void cannotUseSomethingThatDoesNotExist(){

        VerbUseHandler use = new VerbUseHandler().setGame(game);
        LastAction action = use.doVerb(player, "gummachine");
        Assert.assertTrue(action.isFail());
    }

    @Test
    public void cannotUseNothing(){

        VerbUseHandler use = new VerbUseHandler().setGame(game);
        LastAction action = use.doVerb(player, "");
        Assert.assertTrue(action.isFail());
    }

    @Test
    public void cannotUseSomethingThatIsNotHere(){

        Assert.assertTrue(game.getGameLocations().get("1").objects().contains("vendingmachine"));
        Assert.assertFalse(game.getGameLocations().get("2").objects().contains("vendingmachine"));

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbUseHandler use = new VerbUseHandler().setGame(game);
        LastAction action = use.doVerb(player, "vendingmachine");
        Assert.assertTrue(action.isFail());

        Assert.assertTrue(game.getGameLocations().get("1").objects().contains("vendingmachine"));
        Assert.assertFalse(game.getGameLocations().get("2").objects().contains("vendingmachine"));
    }

    @Test
    public void cannotUseANormalLocationObject(){

        Assert.assertTrue(game.getGameLocations().get("1").objects().contains("fixedthing"));
        Assert.assertFalse(game.getGameLocations().get("2").objects().contains("fixedthing"));

        VerbUseHandler use = new VerbUseHandler().setGame(game);
        LastAction action = use.doVerb(player, "fixedthing");
        Assert.assertTrue(action.isFail());

        Assert.assertTrue(game.getGameLocations().get("1").objects().contains("fixedthing"));
        Assert.assertFalse(game.getGameLocations().get("2").objects().contains("fixedthing"));
    }


    @Test
    public void verbHandlerConditions(){

        VerbUseHandler handler = new VerbUseHandler().setGame(game).usingCurrentVerb("use");
        Assert.assertTrue("Using takes time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("Using generates messages",
                handler.shouldAddGameMessages());
        Assert.assertFalse(
                "Using might change items here, but we will not look",
                handler.shouldLookAfterVerb());
    }
}
