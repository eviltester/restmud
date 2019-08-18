package uk.co.compendiumdev.restmud.commandlist;

import org.junit.Assert;
import uk.co.compendiumdev.restmud.api.RestMudAPI;
import uk.co.compendiumdev.restmud.api.RestMudResponseProcessor;

import java.util.List;

public class CommandExecutor {
    private final RestMudAPI api;
    private RestMudResponseProcessor lastResult;

    public CommandExecutor(RestMudAPI api) {
        this.api = api;
    }

    public void execute(List<Command> commands) {
        for(Command command : commands){
            processCommand(command.verb, command.noun);
        }
    }

    public RestMudResponseProcessor processCommand(String verb, String noun) {

        String theVerb = verb.toLowerCase();
        String theNoun = noun.toLowerCase();
        RestMudResponseProcessor result=null;

        System.out.println(String.format("API(%s) > %s %s", api.getUserName(), theVerb, theNoun));

        switch(theVerb){
            case "go":
                result = api.go(theNoun);
                break;
            case "look":
                result = api.look();
                break;
            case "open":
                result = api.open(theNoun);
                break;
            case "close":
                result = api.close(theNoun);
                break;
            case "examine":
                result = api.examine(theNoun);
                break;
            case "use":
                result = api.use(theNoun);
                break;
            case "score":
                result = api.score();
                break;
            case "take":
                result = api.take(theNoun);
                break;
            case "drop":
                result = api.drop(theNoun);
                break;
            case "hoard":
                result = api.hoard(theNoun);
                break;
            case "inspect":
                result = api.inspect(theNoun);
                break;
            case "polish":
                result = api.polish(theNoun);
                break;
            case "inventory":
                result = api.inventory();
                break;
            case "illuminate":
                result = api.illuminate();
                break;
            case "darken":
                result = api.darken();
                break;
            case "checkresult":
                if(theNoun.toLowerCase().contains("fail")){
                    Assert.assertFalse(this.lastResult.isSuccessful());
                }else{
                    // assume whatever was typed meant success
                    Assert.assertTrue(this.lastResult.isSuccessful());
                }
                break;
            default:
                System.out.println("** HMM I DO NOT KNOW HOW TO DO THAT, LET ME TRY");
                if(theNoun.length()>0){
                    result = api.genericVerbNoun(theVerb, theNoun);
                }else{
                    result = api.genericVerb(theVerb);
                }
                break;
        }

        this.lastResult = result;
        return result;
    }
}
