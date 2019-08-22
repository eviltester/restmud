package uk.co.compendiumdev.restmud.testing.dsl;

import com.google.gson.Gson;
import org.junit.Assert;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.playerevents.BroadcastGameMessage;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 21/08/2016.
 */
public class GameTestDSL {

    private MudGame game;
    private List<String> walkthrough = new ArrayList<String>();
    private List<String> looked = new ArrayList<>();

    private class Command{
        public String verb;
        public String noun;

        public Command(String verb, String noun){
            this.verb = verb;
            this.noun = noun;
        }
    }
    private List<Command> commands = new ArrayList<>();



    public GameTestDSL(MudGame aGame){
        this.game= aGame;
    }

    public ResultOutput doVerb(String user, String verb, String noun ){
        return doVerb(user, verb, noun, new RestMudHttpRequestDetails("post"));
    }

    public ResultOutput doVerb(String user, String verb, String noun, RestMudHttpRequestDetails request ){
        System.out.println(String.format("%s: %s %s", user, verb, noun));
        ResultOutput result = game.getCommandProcessor().processTheVerbInGame(user, verb, noun, request);
        System.out.println(String.format("%s: %s", result.resultoutput.lastactionstate, result.resultoutput.lastactionresult));

        Gson json = new Gson();
        System.out.println(json.toJson(result));

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

    public void startWalkthrough() {
        walkthrough = new ArrayList<String>();
        looked = new ArrayList<>();
    }

    public void walkthroughStep(String walkthroughEntry) {
        walkthrough.add(walkthroughEntry);
    }

    public ResultOutput walkthroughStep(String message, String auser, String verb, String noun) {
        if(message.length()>0) {
            walkthrough.add("\n _" + message + "_ \n");
        }
        walkthrough.add("");

        if(verb.length()>0) {
            walkthrough.add(String.format("`%s %s`", verb, noun));
            ResultOutput result = doVerb(auser, verb, noun);
            commands.add(new Command(verb,noun));

            walkthrough.add(String.format("\n> `%s`", result.resultoutput.lastactionresult));
            if(result.look != null){
                if(result.look.location != null){
                    walkthrough.add(String.format("\n> `%s` : `%s`", result.look.location.locationId, result.look.location.shortName));
                    if(!looked.contains(result.look.location.locationId)) {  // only show long description once in walkthrough
                        walkthrough.add(String.format("\n> `%s` ", result.look.location.description));
                        looked.add(result.look.location.locationId); // remember we have looked here
                    }
                }
                if(result.look.visibleObjects !=null){
                    walkthrough.add(String.format("\n> I can see some things here:\n"));
                    for(IdDescriptionPair thing : result.look.visibleObjects){
                        walkthrough.add(String.format("* `%s` (`%s`)", thing.description, thing.id));
                    }
                }
            }
            if(result.gameMessages != null && result.gameMessages.messages !=null && result.gameMessages.messages.size()>0){
                walkthrough.add(String.format("\n> ... messages ...\n"));
                for(BroadcastGameMessage aMessage : result.gameMessages.messages){
                    walkthrough.add(String.format("* %s", aMessage.message));
                }
            }


            return result;
        }

        return null;
    }

    public void outputWalkthrough() {
        for(String line : walkthrough){
            System.out.println(line);
        }
    }

    public boolean writeWalkthroughToFile(String path){
        StringBuilder walkthroughReport = new StringBuilder();

        for(String line : walkthrough){
            walkthroughReport.append(line + System.lineSeparator());
        }
        try {
            Paths.get(path).getParent().toFile().mkdirs();
            Files.write(Paths.get(path), walkthroughReport.toString().getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(String.format("Failed to Write walkthrough to %s", path));
            return false;
        }
    }

    public boolean writeCommandListToFile(String path){
        StringBuilder walkthroughCommandList = new StringBuilder();

        for(Command command : commands){
            walkthroughCommandList.append(command.verb + "," + command.noun + System.lineSeparator());
        }
        try {
            Paths.get(path).getParent().toFile().mkdirs();
            Files.write(Paths.get(path), walkthroughCommandList.toString().getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(String.format("Failed to Write command list to %s", path));
            return false;
        }
    }
}
