package uk.co.compendiumdev.restmud.games.conditions;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameCommandProcessor;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.DefaultGameDefinitionGenerator;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

public class SynonymsInConditionsTest {

    MudGame game;

    @Before
    public void setupDefaultGame(){

        GameInitializer theGameInit = new GameInitializer();
        game = theGameInit.getGame();
        MudGameDefinition defn = MudGameDefinition.create(game.getUserInputParser());
        DefaultGameDefinitionGenerator.define(defn);

        game.initFromDefinition(defn);

        theGameInit.addDefaultUser("The Test User", "tester", "aPassword");
        MudUser aUser = game.getUserManager().getUser("tester");

    }


    @Test
    public void canUseAndConditions() {


        final GameCommandProcessor processor = new GameCommandProcessor(game);

        ResultOutput result;

        result = processor.processTheVerbInGame("tester", "fly", "self", RestMudHttpRequestDetails.EMPTY());
        System.out.println(result.resultoutput.lastactionresult);
        Assert.assertEquals("success", result.resultoutput.lastactionstate);

        result = processor.processTheVerbInGame("tester", "hover", "self", RestMudHttpRequestDetails.EMPTY());
        System.out.println(result.resultoutput.lastactionresult);
        Assert.assertEquals("success", result.resultoutput.lastactionstate);

        // magic not setup as a synonym
        result = processor.processTheVerbInGame("tester", "magic", "self", RestMudHttpRequestDetails.EMPTY());
        System.out.println(result.resultoutput.lastactionresult);
        Assert.assertEquals("fail", result.resultoutput.lastactionstate);

    }

}
