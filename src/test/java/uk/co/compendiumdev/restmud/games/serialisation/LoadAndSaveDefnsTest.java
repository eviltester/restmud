package uk.co.compendiumdev.restmud.games.serialisation;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;


public class LoadAndSaveDefnsTest {


    private static final String userName = "tester";
    private static MudGame game;


    @BeforeClass
    static public void setupTheGame(){
        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        // generate game from Json Defn
        // load from a resource file
        MudGameDefinitionSerialiser loader = new MudGameDefinitionSerialiser();
        String jsonDefn = loader.readJsonFromResource("/games/defaultenginegame.json");

        System.out.println(loader.getListOfBuiltInGames());

        // Init the Game
        theGameInit.generateFromJson(jsonDefn);

        // generate does not add any users
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addDefaultUser("The Test User", userName, "aPassword");

        game.teleportUserTo(userName, "1"); // set the user back to the central room after each path

    }


    @Test
    public void gameLoadingWorkedBecauseWeCanPlayIt(){

        ResultOutput result;
        result = game.teleportUserTo(userName,"1");
        MudUser player = game.getUserManager().getUser(userName);

        game.processTheVerbInGame(userName, "go", "n", null);

        Assert.assertEquals("2",player.getLocationId());


    }


    @Test
    public void canResetAGameToRestoreItToOriginalSettings(){

        ResultOutput result;
        result = game.teleportUserTo(userName,"1");
        MudUser player = game.getUserManager().getUser(userName);

        game.processTheVerbInGame(userName, "go", "n", null);

        Assert.assertEquals("2",player.getLocationId());

        result = game.processTheVerbInGame(userName, "take", "agem", null);
        Assert.assertEquals("success",result.resultoutput.lastactionstate);

        result = game.processTheVerbInGame(userName, "take", "agem", null);
        Assert.assertEquals("fail",result.resultoutput.lastactionstate);

        game.resetGame();

        // should be able to take the gem again
        result = game.teleportUserTo(userName,"2");
        Assert.assertEquals("2",player.getLocationId());

        result = game.processTheVerbInGame(userName, "take", "agem", null);
        Assert.assertEquals("success",result.resultoutput.lastactionstate);
    }

}
