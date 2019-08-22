package uk.co.compendiumdev.restmud.games;



import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.BasicTestGameDefinitionGenerator;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.co.compendiumdev.restmud.testing.dsl.GameTestDSL;

public class BasicTestGameTest {

    private static final String userName = "tester";
    private static MudGame game;
    private static GameTestDSL dsl;

    /* Example Documented Game is deterministic and well documented - it should be used to automate gameplay paths */

    @BeforeClass
    static public void setupTheGame(){


        GameInitializer theGameInit = new GameInitializer();
        game = theGameInit.getGame();
        MudGameDefinition defn = MudGameDefinition.create(game.getUserInputParser());
        new BasicTestGameDefinitionGenerator().define(defn);
        game.initFromDefinition(defn);

        // generate does not add any users
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addDefaultUser("The Test User", userName, "aPassword");

        // set the user back to the central room after each path
        game.getCommandProcessor().wizardCommands().teleportUserTo(userName, "1");

        dsl = new GameTestDSL(game);

    }


    private ResultOutput doVerb(String auser, String verb, String noun) {
        return dsl.doVerb(auser, verb, noun);
    }

    private ResultOutput successfully(ResultOutput resultOutput) {
        return dsl.successfully(resultOutput);
    }
    private ResultOutput failTo(ResultOutput resultOutput) {
        return dsl.failTo(resultOutput);
    }


    @Test
    public void room6(){

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo(userName, "6");
        result = doVerb(userName,"look", "");

        Assert.assertEquals("There are no collectibles here", 0, result.look.collectables.length);
        Assert.assertNull("There are no location objects", result.look.visibleObjects);

    }

    @Test
    public void canExamineTinyPipeAndItAppearsAsACollectible(){

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo(userName, "9");
        result = doVerb(userName,"look", "");

        Assert.assertEquals("There are no collectibles here", 0, result.look.collectables.length);

        result = doVerb(userName,"examine", "atinypipe");
        result = doVerb(userName,"look", "");
        Assert.assertEquals("There is one collectible here", 1, result.look.collectables.length);
    }


    }
