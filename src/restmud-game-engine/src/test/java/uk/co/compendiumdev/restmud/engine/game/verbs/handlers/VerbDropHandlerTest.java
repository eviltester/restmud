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
import uk.co.compendiumdev.restmud.engine.game.verbs.VerbGameAbilities;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

public class VerbDropHandlerTest {

    private static MudGame game;
    private static MudUser player;


    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1", "Room 1", "the room to the left", "e:2");
            create.location("2","Room 2", "the room to the right", "w:1");

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
    public void canDropItemITake(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));
        Assert.assertFalse(game.getGameLocations().get("2").collectables().contains("athing"));

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertFalse(game.getGameLocations().get("1").collectables().contains("athing"));
        Assert.assertFalse(game.getGameLocations().get("2").collectables().contains("athing"));

        p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertFalse(game.getGameLocations().get("1").collectables().contains("athing"));
        Assert.assertFalse(game.getGameLocations().get("2").collectables().contains("athing"));

        VerbDropHandler droppy = new VerbDropHandler().setGame(game);
        LastAction action = droppy.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());

        Assert.assertFalse(game.getGameLocations().get("1").collectables().contains("athing"));
        Assert.assertTrue(game.getGameLocations().get("2").collectables().contains("athing"));
    }

    @Test
    public void cannotDropSomethingTwice(){

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbDropHandler droppy = new VerbDropHandler().setGame(game);
        LastAction action = droppy.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());

        action = droppy.doVerb(player, "athing");
        Assert.assertTrue(action.isFail());
    }

    @Test
    public void cannotDropSomethingThatDoesNotExist(){

        VerbDropHandler droppy = new VerbDropHandler().setGame(game);
        LastAction action = droppy.doVerb(player, "anonexistantthing");
        Assert.assertTrue(action.isFail());
    }

    @Test
    public void cannotDropNothing(){

        VerbDropHandler droppy = new VerbDropHandler().setGame(game);
        LastAction action = droppy.doVerb(player, "");
        Assert.assertTrue(action.isFail());
    }

    @Test
    public void cannotDropSomethingIDoNotHave(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));
        Assert.assertFalse(game.getGameLocations().get("2").collectables().contains("athing"));

        VerbDropHandler droppy = new VerbDropHandler().setGame(game);
        LastAction action = droppy.doVerb(player, "athing");
        Assert.assertTrue(action.isFail());

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));
        Assert.assertFalse(game.getGameLocations().get("2").collectables().contains("athing"));
    }

    @Test
    public void dropVerbHandlerConditions(){

        VerbDropHandler handler = new VerbDropHandler().setGame(game).usingCurrentVerb("drop");
        Assert.assertTrue("Dropping takes time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("Dropping generates messages",
                handler.shouldAddGameMessages());
        Assert.assertFalse(
                "Dropping will change items here, but we know we dropped it so no need to look",
                handler.shouldLookAfterVerb());
    }
}
