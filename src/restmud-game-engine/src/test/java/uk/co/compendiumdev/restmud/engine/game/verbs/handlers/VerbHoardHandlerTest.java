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
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

public class VerbHoardHandlerTest {

    private static MudGame game;
    private static MudUser player;




    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1", "Room 1", "the room to the left", "e:2").setCanHoardTreasureHere(true);
            create.location("2","Room 2", "the room to the right", "w:1").setCanHoardTreasureHere(false);

            create.collectable("athing", "A Thing", "1").canHoard(true,200);
            create.collectable("somejunk", "A Piece of Junk", "1").canHoard(false,100);
            create.collectable("realjunk", "A Real Piece of Junk", "1").canHoard(true,-200);

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
    public void canHoardSomethingOwned(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertEquals(0,player.getScore());

        VerbHoardHandler hoard = new VerbHoardHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals(200,player.getScore());
    }

    @Test
    public void cannotHoardThingsTwice(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertEquals(0,player.getScore());

        VerbHoardHandler hoard = new VerbHoardHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals(200,player.getScore());

        hoard = new VerbHoardHandler().setGame(game);
        action = hoard.doVerb(player, "athing");
        Assert.assertTrue(action.isFail());
        Assert.assertEquals(200,player.getScore());
    }

    @Test
    public void cannotHoardIfNoHoardHere(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));

        Assert.assertTrue(game.getGameLocations().get(player.getLocationId()).canHoardTreasureHere());

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertEquals(0,player.getScore());

        p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertEquals("2",player.getLocationId());
        Assert.assertFalse(game.getGameLocations().get(player.getLocationId()).canHoardTreasureHere());

        VerbHoardHandler hoard = new VerbHoardHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "athing");
        Assert.assertTrue(action.isFail());
        Assert.assertEquals(0,player.getScore());
    }


    @Test
    public void cannotHoardThingsThatCannotBeHoarded(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("somejunk"));

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "somejunk", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertEquals(0,player.getScore());

        VerbHoardHandler hoard = new VerbHoardHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "somejunk");
        Assert.assertTrue(action.isFail());
        Assert.assertEquals(0,player.getScore());
    }

    @Test
    public void someHoardalbeThingsCostMePoints(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("realjunk"));

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "realjunk", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertEquals(0,player.getScore());

        VerbHoardHandler hoard = new VerbHoardHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "realjunk");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals(-200,player.getScore());
    }

    @Test
    public void canNotHoardSomethingIDoNotOwn(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));

        Assert.assertEquals(0,player.getScore());

        VerbHoardHandler hoard = new VerbHoardHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "athing");
        Assert.assertTrue(action.isFail());
        Assert.assertEquals(0,player.getScore());
    }

    @Test
    public void canNotHoardSomethingThatDoesNotExist(){

        Assert.assertFalse(game.getGameLocations().get("1").collectables().contains("nothing"));

        Assert.assertEquals(0,player.getScore());

        VerbHoardHandler hoard = new VerbHoardHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "nothing");
        Assert.assertTrue(action.isFail());
        Assert.assertEquals(0,player.getScore());
    }

    @Test
    public void canOnlyHoardSomethingIfHoardLocation(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertEquals(0,player.getScore());

        p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbHoardHandler hoard = new VerbHoardHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "athing");
        Assert.assertTrue(action.isFail());
        Assert.assertEquals(0,player.getScore());
    }



    @Test
    public void verbHandlerConditions(){

        VerbHoardHandler handler = new VerbHoardHandler().setGame(game).usingCurrentVerb("hoard");
        Assert.assertTrue("Hoarding takes time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("Hoarding generates messages",
                handler.shouldAddGameMessages());
        Assert.assertFalse(
                "Hoarding so no need to look",
                handler.shouldLookAfterVerb());
    }
}
