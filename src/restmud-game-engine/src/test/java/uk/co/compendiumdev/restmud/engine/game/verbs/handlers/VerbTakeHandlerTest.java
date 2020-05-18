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
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

public class VerbTakeHandlerTest {

    private static MudGame game;
    private static MudUser player;

    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1", "Room 1", "the room to the left", "e:2,s:3");
            create.location("2","Room 2", "the room to the right", "w:1");
            create.location("3","Room Dark", "the room to the south", "n:1").makeDark();

            create.collectable("athing", "A Thing", "1");
            create.collectable("flystick", "A FlyingStick", "1").addsAbility("fly/land",100);
            create.collectable("flystick2", "Another FlyingStick", "1").addsAbility("fly/land",100);
            create.collectable("anotherthing", "Another Thing", "3");
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
    public void canDropItemITake(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));
        Assert.assertFalse(game.getGameLocations().get("2").collectables().contains("athing"));

        VerbTakeHandler take = new VerbTakeHandler().setGame(game);
        final LastAction action = take.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());

        Assert.assertFalse(game.getGameLocations().get("1").collectables().contains("athing"));
        Assert.assertFalse(game.getGameLocations().get("2").collectables().contains("athing"));

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertFalse(game.getGameLocations().get("1").collectables().contains("athing"));
        Assert.assertFalse(game.getGameLocations().get("2").collectables().contains("athing"));

        p = game.getCommandProcessor().processTheVerbInGame("tester",
                "drop", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertFalse(game.getGameLocations().get("1").collectables().contains("athing"));
        Assert.assertTrue(game.getGameLocations().get("2").collectables().contains("athing"));
    }

    @Test
    public void cannotTakeSomethingTwice(){

        VerbTakeHandler take = new VerbTakeHandler().setGame(game);
        LastAction action = take.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());

        take = new VerbTakeHandler().setGame(game);
        action = take.doVerb(player, "athing");
        Assert.assertFalse(action.isSuccess());
    }

    // TODO: this seems like a stupid rule, why limit it?
    @Test
    public void canOnlyTakeOneThingThatGrantsAnAbility(){

        VerbTakeHandler take = new VerbTakeHandler().setGame(game);
        LastAction action = take.doVerb(player, "flystick");
        Assert.assertTrue(action.isSuccess());

        take = new VerbTakeHandler().setGame(game);
        action = take.doVerb(player, "flystick2");
        Assert.assertFalse(action.isSuccess());
    }

    @Test
    public void cannotTakeSomethingThatDoesNotExist(){

        VerbTakeHandler take = new VerbTakeHandler().setGame(game);
        LastAction action = take.doVerb(player, "anonexistantthing");
        Assert.assertTrue(action.isFail());
    }

    @Test
    public void cannotTakeNothing(){

        VerbTakeHandler take = new VerbTakeHandler().setGame(game);
        LastAction action = take.doVerb(player, "");
        Assert.assertTrue(action.isFail());
    }

    @Test
    public void cannotTakeSomethingThatIsNotHere(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));
        Assert.assertFalse(game.getGameLocations().get("2").collectables().contains("athing"));

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbTakeHandler take = new VerbTakeHandler().setGame(game);
        LastAction action = take.doVerb(player, "athing");
        Assert.assertTrue(action.isFail());

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));
        Assert.assertFalse(game.getGameLocations().get("2").collectables().contains("athing"));
    }

    @Test
    public void cannotTakeALocationObject(){

        Assert.assertTrue(game.getGameLocations().get("1").objects().contains("fixedthing"));
        Assert.assertFalse(game.getGameLocations().get("2").objects().contains("fixedthing"));

        VerbTakeHandler take = new VerbTakeHandler().setGame(game);
        LastAction action = take.doVerb(player, "fixedthing");
        Assert.assertTrue(action.isFail());

        Assert.assertTrue(game.getGameLocations().get("1").objects().contains("fixedthing"));
        Assert.assertFalse(game.getGameLocations().get("2").objects().contains("fixedthing"));
    }

    @Test
    public void cannotTakeAnythingInTheDark(){

        Assert.assertTrue(game.getGameLocations().get("3").collectables().contains("anotherthing"));
        Assert.assertFalse(game.getGameLocations().get("1").collectables().contains("anotherthing"));

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "s", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbTakeHandler take = new VerbTakeHandler().setGame(game);
        LastAction action = take.doVerb(player, "anotherthing");
        Assert.assertTrue(action.isFail());

        Assert.assertFalse(game.getGameLocations().get("1").collectables().contains("anotherthing"));
        Assert.assertTrue(game.getGameLocations().get("3").collectables().contains("anotherthing"));
    }

    @Test
    public void verbHandlerConditions(){

        VerbTakeHandler handler = new VerbTakeHandler().setGame(game).usingCurrentVerb("take");
        Assert.assertTrue("Taking takes time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("Taking generates messages",
                handler.shouldAddGameMessages());
        Assert.assertFalse(
                "Taking will change items here, but we know we took it so no need to look",
                handler.shouldLookAfterVerb());
    }
}
