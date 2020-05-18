package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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

public class VerbGoHandlerTest {

    private static MudGame game;
    private static MudUser player;
    MudLocation room1;
    String closedgatename = "closedsouthgate";
    String openedgatename = "openeastgate";

    // TODO: the whole gate, local, secret etc. is too complicated need to simplify movement and definition
    // e.g. n:1:blocked, n:2:gate  and if gate then look for gate to process, currently possible to override a gate with a e:3 etc.
    // suspect I created gates before I had implemented conditions could now add conditions to create most gates
    // but 'gates or doors' are fun in multi-player because they can be closed by players to annoy other players
    // need a simpler way to define and implement

    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            // rooms have south to north as simple open passage
            // north from room 1 is a local condition so should fail when no conditions are setup
            // e and west from 1 have one way gates which can be opened and closed

            room1 = create.location("1", "Room 1", "the room to the north", "n:local,s:2");
            // note e:3 is invalid destination
            create.location("2","Room 2", "the room to the south", "n:1,e:3");

            defn.addGate(openedgatename, "1", "e", "2", GateDirection.ONE_WAY, GateStatus.OPEN).gateIsHidden(false);
            defn.addGate(closedgatename, "1", "w", "2", GateDirection.ONE_WAY, GateStatus.CLOSED).gateIsHidden(false);

            // invalid gate definition no valid destination
            // TODO: BUG - this throws a null pointer in game, it is not possible to create gate to invalid location,
            // but should throw an error not a null pointer to help debug the game
            //defn.addGate(openedgatename, "2", "s", "3", GateDirection.ONE_WAY, GateStatus.OPEN).gateIsHidden(false);

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
    public void canGoNorthAndSouthThroughNormalPassages() {

        Assert.assertEquals("1", player.getLocationId());

        VerbGoHandler go = new VerbGoHandler().setGame(game);
        LastAction action = go.doVerb(player,"s");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals("2", player.getLocationId());

        go = new VerbGoHandler().setGame(game);
        action = go.doVerb(player,"n");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals("1", player.getLocationId());

        go = new VerbGoHandler().setGame(game);
        action = go.doVerb(player,"S");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals("2", player.getLocationId());

        go = new VerbGoHandler().setGame(game);
        action = go.doVerb(player,"N");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals("1", player.getLocationId());

        go = new VerbGoHandler().setGame(game);
        action = go.doVerb(player,"south");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals("2", player.getLocationId());

        go = new VerbGoHandler().setGame(game);
        action = go.doVerb(player,"north");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals("1", player.getLocationId());
    }

    @Test
    public void cannotGoWhereThereIsNoExit() {

        Assert.assertEquals("1", player.getLocationId());

        VerbGoHandler go = new VerbGoHandler().setGame(game);
        LastAction action = go.doVerb(player,"se");
        Assert.assertTrue(action.isFail());
        Assert.assertEquals("1", player.getLocationId());
    }

    @Test
    public void cannotGoWhereThereIsNoDestination() {

        Assert.assertEquals("1", player.getLocationId());

        VerbGoHandler go = new VerbGoHandler().setGame(game);
        LastAction action = go.doVerb(player,"e");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals("2", player.getLocationId());

        go = new VerbGoHandler().setGame(game);
        action = go.doVerb(player,"e");
        Assert.assertTrue(action.isFail());
        Assert.assertEquals("2", player.getLocationId());
    }

    @Ignore("Setup for this test highlights a bug where trying to define a gate with an invalid destination throws a null pointer exception")
    @Test
    public void cannotGoThroughGateWhereThereIsNoDestination() {

        Assert.assertEquals("1", player.getLocationId());

        VerbGoHandler go = new VerbGoHandler().setGame(game);
        LastAction action = go.doVerb(player,"e");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals("2", player.getLocationId());

        go = new VerbGoHandler().setGame(game);
        action = go.doVerb(player,"s");
        Assert.assertTrue(action.isFail());
        Assert.assertEquals("2", player.getLocationId());
    }

    @Test
    public void someExitsAreOverriddenByLocalConditionsAndBlockedIfNotHandled() {

        Assert.assertEquals("1", player.getLocationId());

        VerbGoHandler go = new VerbGoHandler().setGame(game);
        LastAction action = go.doVerb(player,"n");
        Assert.assertTrue(action.isFail());
        Assert.assertEquals("1", player.getLocationId());
    }

    @Test
    public void oneWayGatesCanBlockTravelEvenWhenOpened() {

        Assert.assertEquals("1", player.getLocationId());

        VerbGoHandler go = new VerbGoHandler().setGame(game);
        LastAction action = go.doVerb(player,"e");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals("2", player.getLocationId());

        go = new VerbGoHandler().setGame(game);
        action = go.doVerb(player,"w");
        Assert.assertTrue(action.isFail());
        Assert.assertEquals("2", player.getLocationId());

    }

    @Test
    public void gatesDoNotBlockIfOpened(){

        Assert.assertEquals("1", player.getLocationId());

        VerbGoHandler go = new VerbGoHandler().setGame(game);
        LastAction action = go.doVerb(player,"e");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals("2", player.getLocationId());
    }

    @Test
    public void gatesBlockifClosedAndDoNotBlockWhenOpened(){

        Assert.assertEquals("1", player.getLocationId());

        VerbGoHandler go = new VerbGoHandler().setGame(game);
        LastAction action = go.doVerb(player,"w");
        Assert.assertTrue(action.isFail());
        Assert.assertEquals("1", player.getLocationId());

        final MudLocationDirectionGate gate = game.getGateManager().getGateNamed(closedgatename);

        VerbOpenHandler openHandler = new VerbOpenHandler().setGame(game);
        action = openHandler.doVerb(player,"w");

        go = new VerbGoHandler().setGame(game);
        action = go.doVerb(player,"w");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals("2", player.getLocationId());
    }



    @Test
    public void verbHandlerConditions(){

        VerbGoHandler handler = new VerbGoHandler().setGame(game).usingCurrentVerb("go");
        Assert.assertTrue("Going should take time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("Going can generate messages",
                handler.shouldAddGameMessages());
        Assert.assertTrue(
                "Going can change environment so we should look after going",
                handler.shouldLookAfterVerb());
    }
}
