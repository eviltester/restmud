package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.locations.*;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class VerbCloseHandlerTest {

    private static MudGame game;
    private static MudUser player;
    MudLocation room1;
    String closedgatename = "closedsouthgate";
    String openedgatename = "openeastgate";

    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            room1 = create.location("1", "Room 1", "the room to the left", "n:local,e:2:openeastgate,s:2:closedsouthgate");
            create.location("2","Room 2", "the room to the right", "");

            defn.addGate(openedgatename, GateStatus.OPEN).gateIsHidden(false);
            defn.addGate(closedgatename, GateStatus.CLOSED).gateIsHidden(false);

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
    public void canCloseAnOpenGate(){

        final MudLocationDirectionGate gate = game.getGateManager().getGateNamed(openedgatename);

        Assert.assertTrue("Expected Gate Open based on defn", gate.isOpen());

        VerbCloseHandler closeHandler =  new VerbCloseHandler().setGame(game).usingCurrentVerb("close");
        final LastAction action = closeHandler.doVerb(player,"e");

        Assert.assertFalse("Expected Gate Closed after handler", gate.isOpen());
        Assert.assertTrue(action.isSuccess());
    }

    @Test
    public void cannotCloseANonExistantExit(){

        final MudLocationExit exit = room1.exitFor("w");

        Assert.assertNull("Expected No Gate or exit Going West based on defn", exit);

        VerbCloseHandler closeHandler =  new VerbCloseHandler().setGame(game);
        final LastAction action = closeHandler.doVerb(player, "w");

        Assert.assertTrue( action.isFail());
    }

    @Test
    public void cannotCloseANonExistantGate(){

        final MudLocationDirectionGate gate = game.getGateManager().getGateNamed("bobsgate");

        Assert.assertNull("Expected No Gate or exit Going West based on defn", gate);

        VerbCloseHandler closeHandler =  new VerbCloseHandler().setGame(game);
        final LastAction action = closeHandler.doVerb(player, "bobsgate");

        Assert.assertTrue( action.isFail());
    }

    @Test
    public void cannotCloseIfNoDirectionProvided(){

        VerbCloseHandler closeHandler =  new VerbCloseHandler().setGame(game);
        final LastAction action = closeHandler.doVerb(player, "");

        Assert.assertTrue( action.isFail());
    }

    @Test
    public void cannotCloseIfNounIsNotADirection(){

        VerbCloseHandler closeHandler =  new VerbCloseHandler().setGame(game);
        final LastAction action = closeHandler.doVerb(player, "Bob");

        Assert.assertTrue( action.isFail());
    }

    //TODO: add condition handling to allow local close gate conditions
    @Test
    public void haveNoAbilityForLocalGateProcessingYet(){

        VerbCloseHandler closeHandler =  new VerbCloseHandler().setGame(game);
        final LastAction action = closeHandler.doVerb(player, "n");

        Assert.assertTrue( action.isFail());
    }

    @Test
    public void cannotCloseAClosedGate(){

        final MudLocationDirectionGate gate = game.getGateManager().getGateNamed(closedgatename);

        Assert.assertFalse("Expected Gate Closed based on defn", gate.isOpen());

        VerbCloseHandler closeHandler =  new VerbCloseHandler().setGame(game);
        final LastAction action = closeHandler.doVerb(player,"s");

        Assert.assertFalse("Expected Gate remained Closed after handler", gate.isOpen());
        Assert.assertTrue(action.isFail());
    }

    @Test
    public void closeVerbHandlerConditions(){

        VerbCloseHandler closeHandler = new VerbCloseHandler().setGame(game);
        Assert.assertTrue("Closing a gate should take time",
                closeHandler.actionUpdatesTimeStamp());
        Assert.assertTrue("Closing a gate can generate messages",
                closeHandler.shouldAddGameMessages());
        Assert.assertTrue(
                "Closing a gate can change environment so we should look after closing",
                closeHandler.shouldLookAfterVerb());

    }
}
