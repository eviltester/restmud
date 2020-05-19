package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.verbs.VerbGameAbilities;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

public class VerbDarkenHandlerTest {

    private static MudGame game;
    private static MudUser player;

    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1", "Room 1", "the dark room to the left", "e:2").
                    makeLight();
            create.location("2","Room 2", "the light room to the right", "w:1").
                    makeDark();

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
    public void canDarkenWhenPickedUpTorchIsOn(){

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

        VerbDarkenHandler handler = new VerbDarkenHandler().setGame(game).usingCurrentVerb("darken");
        action = handler.doVerb(player, "");
        Assert.assertFalse(player.canISeeInTheDark());
        Assert.assertTrue(action.isSuccess());
    }

    @Test
    public void cannotDarkenIfTorchIsNotOn(){

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "atorch", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertFalse(player.canISeeInTheDark());
        Assert.assertTrue(game.getGameLocations().get(player.getLocationId()).isLocationDark());

        VerbDarkenHandler handler = new VerbDarkenHandler().setGame(game).usingCurrentVerb("darken");
        final LastAction action = handler.doVerb(player, "");

        Assert.assertTrue(action.isFail());

        Assert.assertFalse(player.canISeeInTheDark());
    }

    @Test
    public void cannotDarkenIfIDoNotHaveATorch(){

//        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
//                "take", "atorch", null);
//        Assert.assertTrue(p.resultoutput.isSuccess());

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertFalse(player.canISeeInTheDark());
        Assert.assertTrue(game.getGameLocations().get(player.getLocationId()).isLocationDark());

        VerbDarkenHandler handler = new VerbDarkenHandler().setGame(game).usingCurrentVerb("darken");
        final LastAction action = handler.doVerb(player, "");

        Assert.assertTrue(action.isFail());

        Assert.assertFalse(player.canISeeInTheDark());
    }


    @Test
    public void darkenVerbHandlerConditions(){

        VerbDarkenHandler handler = new VerbDarkenHandler().setGame(game);
        Assert.assertTrue("Darkening takes time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("Darkening generates messages",
                handler.shouldAddGameMessages());
        Assert.assertTrue(
                "Darkening can change how we see things so we should look after this",
                handler.shouldLookAfterVerb());

    }
}
