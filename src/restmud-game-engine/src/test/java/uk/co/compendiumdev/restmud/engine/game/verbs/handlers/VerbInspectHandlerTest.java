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
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ObjectInspection;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

/*
    Inspecting is a way of finding out information about a collectable
    i.e. if I hoard it, is it useful etc.
 */
public class VerbInspectHandlerTest {

    private static MudGame game;
    private static MudUser player;

    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1", "Room 1", "the room to the left", "e:2");
            create.location("2", "Room 2", "the room to the right", "w:1");

            create.collectable("athing", "A Thing", "1").canHoard(true,200);
            create.collectable("abadthing", "A Bad Thing", "1").canHoard(true,-200);
            create.collectable("aworthlessthing", "A Worthless Thing", "1").canHoard(false,100);
            create.locationObject("anitem","The Item", "The item examined", "1");

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
    public void canInspectACollectableIfIHavePoints(){

        player.setScore(200);

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbInspectHandler inspect = new VerbInspectHandler().setGame(game);
        LastAction action = inspect.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());

        final ObjectInspection report = action.getObjectInspectionReport();
        Assert.assertEquals(report.collectableHoardableScore,"200");
    }

    @Test
    public void cannotInspectACollectableIfIHaveNoPoints(){

        player.setScore(0);

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbInspectHandler inspect = new VerbInspectHandler().setGame(game);
        LastAction action = inspect.doVerb(player, "athing");
        Assert.assertTrue(action.isFail());

        final ObjectInspection report = action.getObjectInspectionReport();
        Assert.assertNull(report);
    }

    @Test
    public void cannotInspectCollectablesThatAreNotHere(){

        player.setScore(200);

        // go to room where collectable does exist
        final ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbInspectHandler inspect = new VerbInspectHandler().setGame(game);
        LastAction action = inspect.doVerb(player, "athing");
        Assert.assertTrue(action.isFail());
    }

    @Test
    public void canInspectACollectableIDoNotOwn(){

        player.setScore(200);

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));

        VerbInspectHandler inspect = new VerbInspectHandler().setGame(game);
        LastAction action = inspect.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());

        final ObjectInspection report = action.getObjectInspectionReport();
        Assert.assertEquals(report.collectableHoardableScore,"200");
    }

    @Test
    public void cannotInspectLocationObjects(){

        player.setScore(200);

        VerbInspectHandler inspect = new VerbInspectHandler().setGame(game);
        LastAction action = inspect.doVerb(player, "anitem");

        Assert.assertTrue(action.isFail());
    }


    @Test
    public void verbHandlerConditions(){

        VerbInspectHandler handler = new VerbInspectHandler().setGame(game).usingCurrentVerb("inspect");
        Assert.assertTrue("Inspecting takes time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("Inspecting generates messages",
                handler.shouldAddGameMessages());
        Assert.assertFalse(
                "Inspecting will not items here, so no need to look",
                handler.shouldLookAfterVerb());
    }
}
