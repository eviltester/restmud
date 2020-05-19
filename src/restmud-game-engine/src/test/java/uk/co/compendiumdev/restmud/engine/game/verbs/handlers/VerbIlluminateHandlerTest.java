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

public class VerbIlluminateHandlerTest {

    private static MudGame game;
    private static MudUser player;


    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1", "Room 1", "the dark room to the left", "e:2")
                .makeLight();
            create.location("2","Room 2", "the light room to the right", "w:1")
                .makeDark();

            create.collectable("atorch", "A Torch", "1").
                    addsToggleAbility(VerbGameAbilities.ILLUMINATE_DARKEN,100, false);

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
    public void canIlluminateWhenPickedUpTorchIsNotOn(){

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "atorch", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertFalse(player.canISeeInTheDark());
        Assert.assertTrue(game.getGameLocations().get(player.getLocationId()).isLocationDark());


        VerbIlluminateHandler illuminate = new VerbIlluminateHandler().setGame(game).
                                                usingCurrentVerb("illuminate");
        LastAction action = illuminate.doVerb(player, "");
        Assert.assertTrue(action.isSuccess());

        Assert.assertTrue(player.canISeeInTheDark());
    }

    @Test
    public void cannotIlluminateIfNotCarryingTorch(){

//        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
//                "take", "atorch", null);
//        Assert.assertTrue(p.resultoutput.isSuccess());

        final ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertFalse(player.canISeeInTheDark());
        Assert.assertTrue(game.getGameLocations().get(player.getLocationId()).isLocationDark());

        VerbIlluminateHandler handler = new VerbIlluminateHandler().setGame(game).usingCurrentVerb("illuminate");
        LastAction switchOn = handler.doVerb(player, "");
        Assert.assertFalse(switchOn.isSuccess());
        Assert.assertFalse(player.canISeeInTheDark());
    }

    @Test
    public void cannotIlluminateIfTorchIsOn(){

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "atorch", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertFalse(player.canISeeInTheDark());
        Assert.assertTrue(game.getGameLocations().get(player.getLocationId()).isLocationDark());

        VerbIlluminateHandler handler = new VerbIlluminateHandler().setGame(game).usingCurrentVerb("illuminate");
        LastAction switchOn = handler.doVerb(player, "");
        Assert.assertTrue(switchOn.isSuccess());

        final LastAction switchOnAgain = handler.doVerb(player, "");
        Assert.assertTrue(switchOnAgain.isFail());

        Assert.assertTrue(player.canISeeInTheDark());
    }

    @Test
    public void cannotIlluminateIfTorchHasNoPowerLeft(){

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "atorch", null);
        Assert.assertTrue(p.resultoutput.isSuccess());
        player.inventory().get("atorch").setAbilityPower(0);

        p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertFalse(player.canISeeInTheDark());
        Assert.assertTrue(game.getGameLocations().get(player.getLocationId()).isLocationDark());

        VerbIlluminateHandler handler = new VerbIlluminateHandler().setGame(game).usingCurrentVerb("illuminate");
        final LastAction action = handler.doVerb(player, "");

        Assert.assertTrue(action.isFail());

        Assert.assertFalse(player.canISeeInTheDark());
    }


    @Test
    public void illuminateVerbHandlerConditions(){

        VerbIlluminateHandler handler = new VerbIlluminateHandler().setGame(game);
        Assert.assertTrue("Illuminating takes time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("Illuminating generates messages",
                handler.shouldAddGameMessages());
        Assert.assertTrue(
                "Iluminating can change how we see things so we should look after this",
                handler.shouldLookAfterVerb());

    }
}
