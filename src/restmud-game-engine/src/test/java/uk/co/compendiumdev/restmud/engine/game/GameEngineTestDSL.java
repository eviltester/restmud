package uk.co.compendiumdev.restmud.engine.game;

import org.junit.Assert;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 12/10/2017.
 */
public class GameEngineTestDSL {

    private final MudGame game;

    private class Command{
        public final String verb;
        public final String noun;

        public Command(String verb, String noun){
            this.verb = verb;
            this.noun = noun;
        }
    }

    private final List<Command> commands = new ArrayList<>();

    public GameEngineTestDSL(MudGame aGame){
        this.game= aGame;
    }

    public ResultOutput doVerb(String user, String verb, String noun ){
        return doVerb(user, verb, noun, new RestMudHttpRequestDetails("post"));
    }

    public ResultOutput doVerb(String user, String verb, String noun, RestMudHttpRequestDetails request ){
        System.out.println(String.format("%s: %s %s", user, verb, noun));
        ResultOutput result = game.getCommandProcessor().processTheVerbInGame(user, verb, noun, request);
        System.out.println(String.format("%s: %s", result.resultoutput.lastactionstate, result.resultoutput.lastactionresult));
        return result;
    }

    public ResultOutput successfully(ResultOutput resultOutput) {
        Assert.assertEquals("Check We Performed Action Successfully", LastAction.SUCCESS, resultOutput.resultoutput.lastactionstate);
        commands.add(new Command("checkresult","success"));
        return resultOutput;
    }

    public ResultOutput failTo(ResultOutput resultOutput) {
        Assert.assertEquals(LastAction.FAIL, resultOutput.resultoutput.lastactionstate);
        commands.add(new Command("checkresult","fail"));
        return resultOutput;
    }
}
