package uk.co.compendiumdev.restmud.engine.game.verbs.primitive;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.locations.GateStatus;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationDirectionGate;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationExit;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.VerbGoHandler;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.VerbGoHandlerTest;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.VerbLookHandler;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LookResultOutput;

public class LookAndGoExitGatesTest {

    private static MudGame game;
    private static MudUser player;


    class UnitTestGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            // add exits and gates during test

            create.location("1", "Room 1", "east room", "");
            create.location("2","Room 2", "west room", "");

            game.initFromDefinition(defn);
        }
    }

    @Before
    public void setupTheGame(){
        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.generate(new UnitTestGame());

        // generate does not add any users
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addDefaultUser("The Test User", "tester", "aPassword");

        // set the user back to the central room after each path
        game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1");

        player = game.getUserManager().getUser("tester");

    }

    @Test
    public void anOpenGateOnAnExitIsShown(){

        final MudLocation room = game.getGameLocations().get("1");
        final MudLocationExit gatedExit = new MudLocationExit("1", "e", "2");
        gatedExit.setGateName("gate");
        final MudLocationDirectionGate gate = new MudLocationDirectionGate("gate", GateStatus.OPEN);
        game.getGateManager().addGate(gate);
        room.addExit(gatedExit);

        final VerbLookHandler look = new VerbLookHandler().setGame(game);

        final LastAction result = look.doVerb(player, "look");

        Assert.assertTrue(result.isSuccess());
        final LookResultOutput saw = result.getLookReport();
        Assert.assertNotNull(saw);

        // check exit as expected
        Assert.assertEquals("1",saw.location.locationId);
        Assert.assertEquals("1 exit", 1, saw.exits.size());
        Assert.assertEquals("1 exit to east","e",saw.exits.get(0).direction);

        // check gate as expected
        Assert.assertEquals("1 gate", 1, saw.visibleGates.size());
        Assert.assertEquals("1 gate to east","e",saw.visibleGates.get(0).direction);
        Assert.assertEquals("gate is open",true,saw.visibleGates.get(0).open);

        System.out.println("A visible open gate and exit to the east");
        System.out.println(new Gson().toJson(saw));

        // can go through open hidden gates
        Assert.assertTrue(new VerbGoHandler().setGame(game).doVerb(player,"e").isSuccess());
        Assert.assertEquals("2", player.getLocationId());
    }

    @Test
    public void aClosedGateOnAnExitIsShown(){

        final MudLocation room = game.getGameLocations().get("1");
        final MudLocationExit gatedExit = new MudLocationExit("1", "e", "2");
        gatedExit.setGateName("gate");
        final MudLocationDirectionGate gate = new MudLocationDirectionGate("gate", GateStatus.CLOSED);
        game.getGateManager().addGate(gate);
        room.addExit(gatedExit);

        final VerbLookHandler look = new VerbLookHandler().setGame(game);

        final LastAction result = look.doVerb(player, "look");

        Assert.assertTrue(result.isSuccess());
        final LookResultOutput saw = result.getLookReport();
        Assert.assertNotNull(saw);

        // check exit as expected
        Assert.assertEquals("1",saw.location.locationId);
        Assert.assertEquals("1 exit", 1, saw.exits.size());
        Assert.assertEquals("1 exit to east","e",saw.exits.get(0).direction);

        // check gate as expected
        Assert.assertEquals("1 gate", 1, saw.visibleGates.size());
        Assert.assertEquals("1 gate to east","e",saw.visibleGates.get(0).direction);
        Assert.assertEquals("gate is closed",false,saw.visibleGates.get(0).open);

        System.out.println("A visible closed gate and exit to the east");
        System.out.println(new Gson().toJson(saw));

        // cannot go through closed gates
        Assert.assertTrue(new VerbGoHandler().setGame(game).doVerb(player,"e").isFail());
        Assert.assertEquals("1", player.getLocationId());
    }

    @Test
    public void aGateOnASecretExitIsShown(){

        final MudLocation room = game.getGameLocations().get("1");
        final MudLocationExit gatedExit = new MudLocationExit("1", "e", "2");
        gatedExit.setGateName("gate");
        gatedExit.setSecretRoute(true);

        final MudLocationDirectionGate gate = new MudLocationDirectionGate("gate", GateStatus.OPEN);
        game.getGateManager().addGate(gate);
        room.addExit(gatedExit);

        final VerbLookHandler look = new VerbLookHandler().setGame(game);

        final LastAction result = look.doVerb(player, "look");

        Assert.assertTrue(result.isSuccess());
        final LookResultOutput saw = result.getLookReport();
        Assert.assertNotNull(saw);

        // check exit as expected
        Assert.assertEquals("1",saw.location.locationId);
        Assert.assertEquals("0 exits visible", 0, saw.exits.size());

        // check gate as expected
        Assert.assertEquals("1 gate", 1, saw.visibleGates.size());
        Assert.assertEquals("1 gate to east","e",saw.visibleGates.get(0).direction);
        Assert.assertEquals("gate is open",true,saw.visibleGates.get(0).open);

        System.out.println("A visible gate to the east secret exit");
        System.out.println(new Gson().toJson(saw));

        // can go through visible gates
        final LastAction go = new VerbGoHandler().setGame(game).doVerb(player, "e");
        Assert.assertTrue(go.isSuccess());
        Assert.assertEquals("2", player.getLocationId());
    }

    @Test
    public void aSecretGateOnASecretExitIsNotShown(){

        final MudLocation room = game.getGameLocations().get("1");
        final MudLocationExit gatedExit = new MudLocationExit("1", "e", "2");
        gatedExit.setGateName("gate");
        gatedExit.setSecretRoute(true);

        final MudLocationDirectionGate gate = new MudLocationDirectionGate("gate", GateStatus.OPEN);
        gate.gateIsHidden(true);
        game.getGateManager().addGate(gate);
        room.addExit(gatedExit);

        final VerbLookHandler look = new VerbLookHandler().setGame(game);

        final LastAction result = look.doVerb(player, "look");

        Assert.assertTrue(result.isSuccess());
        final LookResultOutput saw = result.getLookReport();
        Assert.assertNotNull(saw);

        // check exit as expected
        Assert.assertEquals("1",saw.location.locationId);
        Assert.assertEquals("0 exits visible", 0, saw.exits.size());

        // check gate as expected
        // TODO: should this be null - this is just backwards compatibility?
        Assert.assertEquals("no gates", null, saw.visibleGates);

        System.out.println("A hidden gate to the east secret exit");
        System.out.println(new Gson().toJson(saw));

        // can go through open hidden gates
        Assert.assertTrue(new VerbGoHandler().setGame(game).doVerb(player,"e").isSuccess());
        Assert.assertEquals("2", player.getLocationId());
    }

    @Test
    public void aSecretExitIsNotShown(){

        final MudLocation room = game.getGameLocations().get("1");
        final MudLocationExit exit = new MudLocationExit("1", "e", "2");
        exit.setSecretRoute(true);
        room.addExit(exit);

        final VerbLookHandler look = new VerbLookHandler().setGame(game);

        final LastAction result = look.doVerb(player, "look");

        Assert.assertTrue(result.isSuccess());
        final LookResultOutput saw = result.getLookReport();
        Assert.assertNotNull(saw);

        // check exit as expected
        Assert.assertEquals("1",saw.location.locationId);
        Assert.assertEquals("0 exits visible", 0, saw.exits.size());

        // check gate as expected
        // TODO: should this be null - this is just backwards compatibility?
        Assert.assertEquals("no gates", null, saw.visibleGates);

        System.out.println("east secret exit");
        System.out.println(new Gson().toJson(saw));

        // can go through open hidden gates
        Assert.assertTrue(new VerbGoHandler().setGame(game).doVerb(player,"e").isSuccess());
        Assert.assertEquals("2", player.getLocationId());
    }

    @Test
    public void anExitIsShown(){

        final MudLocation room = game.getGameLocations().get("1");
        final MudLocationExit exit = new MudLocationExit("1", "e", "2");
        room.addExit(exit);

        final VerbLookHandler look = new VerbLookHandler().setGame(game);

        final LastAction result = look.doVerb(player, "look");

        Assert.assertTrue(result.isSuccess());
        final LookResultOutput saw = result.getLookReport();
        Assert.assertNotNull(saw);

        // check exit as expected
        Assert.assertEquals("1",saw.location.locationId);
        Assert.assertEquals("1 exits visible", 1, saw.exits.size());
        Assert.assertEquals("1 exits to east", "e", saw.exits.get(0).direction);

        // check gate as expected
        // TODO: should this be null - this is just backwards compatibility?
        Assert.assertEquals("no gates", null, saw.visibleGates);

        System.out.println("east exit");
        System.out.println(new Gson().toJson(saw));

        // can go through open hidden gates
        Assert.assertTrue(new VerbGoHandler().setGame(game).doVerb(player,"e").isSuccess());
        Assert.assertEquals("2", player.getLocationId());
    }
}


