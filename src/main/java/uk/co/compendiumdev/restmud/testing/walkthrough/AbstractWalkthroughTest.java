package uk.co.compendiumdev.restmud.testing.walkthrough;


import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;
import org.junit.Assert;
import uk.co.compendiumdev.restmud.testing.dsl.GameTestDSL;

/**
 * Created by Alan on 31/01/2017.
 */
public class AbstractWalkthroughTest {

    public MudGame game;
    public GameTestDSL dsl;
    public String user="tester";


    public void setupTheGame(String gameName){
        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.generate(gameName);

        // generate does not add any users
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addDefaultUser("The Test User", user, "aPassword");

        dsl = new GameTestDSL(game);

    }

    public void setupTheGame(MudGameDefinition aGameDefn){

        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        aGameDefn.setUserInputParser(game.getUserInputParser());

        theGameInit.generate(aGameDefn);

        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addDefaultUser("The Test User", user, "aPassword");

        dsl = new GameTestDSL(game);

    }

    public void successfullyVisitRoom(String roomid, ResultOutput result) {
        //dsl.checkPlayerLocationIs(roomid)
        Assert.assertEquals(roomid, result.look.location.locationId);
    }

    public ResultOutput walkthrough(String message, String verb, String noun) {
        return dsl.walkthroughStep(message, user ,verb, noun);
    }

    public ResultOutput doVerb( String verb, String noun) {
        return dsl.doVerb(user, verb, noun);
    }

    public ResultOutput successfully(ResultOutput resultOutput) {
        return dsl.successfully(resultOutput);
    }

    public ResultOutput failTo(ResultOutput resultOutput) {
        return dsl.failTo(resultOutput);
    }

    public void outputWalkthroughWarning() {
        dsl.walkthroughStep("\n## Warning\n");

        dsl.walkthroughStep("Using this game walkthrough will prevent you from investigating the game fully and learning what you can. Also note that I am not a professional hint and walkthrough writer so there is no guarantee that you an use this document without spoilage. I suggest you: read the hints first, dip into and out of the walkthrough, map your won map, only use the map here as a last resort.");
        dsl.walkthroughStep("This walkthrough is automatically generated from a 'test'. It is created as the game is played. So the output you see here is what the game provides, if you are doing something different then you are playing a different version of the game, a different game, or doing something wrong");
    }

}
