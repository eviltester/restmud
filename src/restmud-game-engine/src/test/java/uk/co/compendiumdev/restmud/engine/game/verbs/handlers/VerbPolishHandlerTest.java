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
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

public class VerbPolishHandlerTest {

    private static MudGame game;
    private static MudUser player;


    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1", "Room 1", "the room to the left", "e:2").setCanHoardTreasureHere(true);
            create.location("2","Room 2", "the room to the right", "w:1").setCanHoardTreasureHere(false);

            create.collectable("athing", "A Thing", "1").canHoard(true,200);
            create.collectable("rubbish", "A Rubbish Thing", "1").canHoard(false,100);
            create.collectable("polishcloth", "A cloth", "1").addsAbility("polish", 1000);

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
    public void canPolishSomethingOwnedIfCanPolish(){

        final MudCollectable cloth = game.getGameCollectables().get("polishcloth");
        final MudCollectable thing = game.getGameCollectables().get("athing");

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertEquals(1000, cloth.getAbilityPower());
        Assert.assertEquals(200, thing.getHoardableScore());

        p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "polishcloth", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbPolishHandler hoard = new VerbPolishHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());

        Assert.assertTrue(1000 > cloth.getAbilityPower());
        Assert.assertTrue(200 <  thing.getHoardableScore());
    }


    @Test
    public void canUseAllPolishPower(){

        final MudCollectable cloth = game.getGameCollectables().get("polishcloth");
        final MudCollectable thing = game.getGameCollectables().get("athing");
        cloth.setAbilityPower(9);

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertEquals(9, cloth.getAbilityPower());
        Assert.assertEquals(200, thing.getHoardableScore());

        p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "polishcloth", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbPolishHandler hoard = new VerbPolishHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());

        Assert.assertEquals(0, cloth.getAbilityPower());
        Assert.assertTrue(200 <  thing.getHoardableScore());
    }

    @Test
    public void cannotPolishSomethingNotHoardable(){

        final MudCollectable cloth = game.getGameCollectables().get("polishcloth");
        final MudCollectable thing = game.getGameCollectables().get("rubbish");

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "rubbish", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertEquals(1000, cloth.getAbilityPower());
        Assert.assertEquals(100, thing.getHoardableScore());

        p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "polishcloth", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbPolishHandler hoard = new VerbPolishHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "rubbish");
        Assert.assertTrue(action.isFail());

        Assert.assertEquals(1000, cloth.getAbilityPower());
        Assert.assertEquals(100,  thing.getHoardableScore());
    }

    @Test
    public void cannotPolishIfNothingToPolishWith(){

        final MudCollectable thing = game.getGameCollectables().get("athing");

        ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "athing", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        Assert.assertEquals(200, thing.getHoardableScore());

        VerbPolishHandler hoard = new VerbPolishHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "athing");
        Assert.assertTrue(action.isFail());

        Assert.assertEquals(200, thing.getHoardableScore());
    }

    @Test
    public void cannotPolishIfNotCarryingAnythingToPolish(){

        final MudCollectable cloth = game.getGameCollectables().get("polishcloth");
        final MudCollectable thing = game.getGameCollectables().get("athing");

        Assert.assertEquals(1000, cloth.getAbilityPower());
        Assert.assertEquals(200, thing.getHoardableScore());

        final ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "polishcloth", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbPolishHandler hoard = new VerbPolishHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "athing");
        Assert.assertTrue(action.isFail());

        Assert.assertEquals(1000, cloth.getAbilityPower());
        Assert.assertEquals(200, thing.getHoardableScore());
    }

    @Test
    public void cannotPolishNotExistThingToPolish(){

        final MudCollectable cloth = game.getGameCollectables().get("polishcloth");

        Assert.assertEquals(1000, cloth.getAbilityPower());

        final ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "take", "polishcloth", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbPolishHandler hoard = new VerbPolishHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "nothing");
        Assert.assertTrue(action.isFail());

        Assert.assertEquals(1000, cloth.getAbilityPower());
    }





    @Test
    public void verbHandlerConditions(){

        VerbPolishHandler handler = new VerbPolishHandler().setGame(game).usingCurrentVerb("polish");
        Assert.assertTrue("Polishing takes time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("Polishing shows messages",
                handler.shouldAddGameMessages());
        Assert.assertFalse(
                "Polishing so no need to look",
                handler.shouldLookAfterVerb());
    }
}
