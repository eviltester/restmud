package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.locations.GateDirection;
import uk.co.compendiumdev.restmud.engine.game.locations.GateStatus;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationDirectionGate;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class VerbOpenHandlerTest {

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

            room1 = create.location("1", "Room 1", "the room to the left", "n:local");
            create.location("2","Room 2", "the room to the right", "");

            defn.addGate(openedgatename, "1", "e", "2", GateDirection.ONE_WAY, GateStatus.OPEN).gateIsHidden(false);
            defn.addGate(closedgatename, "1", "s", "2", GateDirection.ONE_WAY, GateStatus.CLOSED).gateIsHidden(false);

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
    public void canOpenAClosedGate(){

        final MudLocationDirectionGate gate = game.getGateManager().getGateNamed(closedgatename);

        Assert.assertFalse("Expected Gate Closed based on defn", gate.isOpen());

        VerbOpenHandler openHandler = new VerbOpenHandler().setGame(game);
        final LastAction action = openHandler.doVerb(player,"s");

        Assert.assertTrue("Expected Gate Opened after handler", gate.isOpen());
        Assert.assertTrue(action.isSuccess());
    }

    @Test
    public void cannotOpenANonExistantGate(){

        final MudLocationDirectionGate gate =
                game.getGateManager().getGateGoingFromHereInThatDirection(room1, "w");

        Assert.assertNull("Expected No Gate Going West based on defn", gate);

        VerbOpenHandler openHandler =  new VerbOpenHandler().setGame(game);
        final LastAction action = openHandler.doVerb(player, "w");

        Assert.assertTrue( action.isFail());
    }

    @Test
    public void cannotOpenIfNoDirectionProvided(){

        VerbOpenHandler openHandler =  new VerbOpenHandler().setGame(game);
        final LastAction action = openHandler.doVerb(player, "");

        Assert.assertTrue( action.isFail());
    }

    @Test
    public void cannotOpenIfNounIsNotADirection(){

        VerbOpenHandler openHandler =  new VerbOpenHandler().setGame(game);
        final LastAction action = openHandler.doVerb(player, "Bob");

        Assert.assertTrue( action.isFail());
    }

    //TODO: add condition handling to allow local open gate conditions
    @Test
    public void haveNoAbilityForLocalGateProcessingYet(){

        VerbOpenHandler openHandler =  new VerbOpenHandler().setGame(game);
        final LastAction action = openHandler.doVerb(player, "n");

        Assert.assertTrue( action.isFail());
    }

    @Test
    public void cannotOpenAnOpenGate(){

        final MudLocationDirectionGate gate = game.getGateManager().getGateNamed(openedgatename);

        Assert.assertTrue("Expected Gate Opened based on defn", gate.isOpen());

        VerbOpenHandler openHandler =  new VerbOpenHandler().setGame(game);
        final LastAction action = openHandler.doVerb(player,"e");

        Assert.assertTrue("Expected Gate remained Open after handler", gate.isOpen());
        Assert.assertTrue(action.isFail());
    }

    @Test
    public void openVerbHandlerConditions(){

        VerbOpenHandler openHandler = new VerbOpenHandler().setGame(game);
        Assert.assertTrue("Opening a gate should take time",
                openHandler.actionUpdatesTimeStamp());
        Assert.assertTrue("Opening a gate can generate messages",
                openHandler.shouldAddGameMessages());
        Assert.assertTrue(
                "Opening a gate can change environment so we should look after closing",
                openHandler.shouldLookAfterVerb());

    }
}
