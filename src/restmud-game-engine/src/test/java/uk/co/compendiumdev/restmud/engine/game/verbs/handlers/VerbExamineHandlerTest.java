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

public class VerbExamineHandlerTest {

    private static MudGame game;
    private static MudUser player;
    MudLocation takeroom;
    MudLocation droproom;
    private MudCollectable thing;
    private MudLocationObject itemInLocation;

    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            takeroom = create.location("1", "Room 1", "the room to the left", "e:2");
            takeroom = create.location("2", "Room 2", "the room to the right", "w:1");

            thing = create.collectable("athing", "A Thing", "1");
            itemInLocation = create.locationObject("anitem","The Item", "The item examined", "1");
            itemInLocation = create.locationObject("anotheritem","The Other Item", "The other item examined", "2");

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

    // TODO: currently examine has to be implemented by conditions - should it?
    //  why not have an examine description on collectables?
    @Test
    public void collectablesDoNotHaveAnExaminableDescriptionButWeCanExamineThem(){

        VerbExamineHandler examine = new VerbExamineHandler().setGame(game);
        LastAction action = examine.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        examine = new VerbExamineHandler().setGame(game);
        action = examine.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());
    }

    @Test
    public void cannotExamineCollectablesThatAreNotHere(){

        // go to room where collectable does exist
        final ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbExamineHandler examine = new VerbExamineHandler().setGame(game);
        LastAction action = examine.doVerb(player, "athing");
        Assert.assertTrue(action.isFail());

    }

    @Test
    public void canExamineLocationObjects(){

        VerbExamineHandler examine = new VerbExamineHandler().setGame(game);
        LastAction action = examine.doVerb(player, "anitem");

        Assert.assertTrue(action.isSuccess());
        Assert.assertTrue(action.lastactionresult.contains("The item examined"));
    }

    @Test
    public void cannotExamineLocationObjectsThatDoNotExistAtAll(){

        VerbExamineHandler examine = new VerbExamineHandler().setGame(game);
        LastAction action = examine.doVerb(player, "anonexistantitem");

        Assert.assertTrue(action.isFail());
    }

    @Test
    public void cannotExamineLocationObjectsInOtherLocations(){

        VerbExamineHandler examine = new VerbExamineHandler().setGame(game);
        LastAction action = examine.doVerb(player, "anotheritem");

        Assert.assertTrue(action.isFail());

        // go to room where location object does exist
        final ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        examine = new VerbExamineHandler().setGame(game);
        action = examine.doVerb(player, "anotheritem");

        Assert.assertTrue(action.isSuccess());
        Assert.assertTrue(action.lastactionresult.contains("The other item examined"));
    }



    @Test
    public void verbHandlerConditions(){

        VerbExamineHandler handler = new VerbExamineHandler().setGame(game).usingCurrentVerb("examine");
        Assert.assertTrue("Examining takes time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("Examining generates messages",
                handler.shouldAddGameMessages());
        Assert.assertFalse(
                "Examining will not items here, so no need to look",
                handler.shouldLookAfterVerb());
    }
}
