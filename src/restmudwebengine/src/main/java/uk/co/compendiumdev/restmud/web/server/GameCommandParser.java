package uk.co.compendiumdev.restmud.web.server;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.VerbRegexToVerbMatch;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

import java.util.List;


public class GameCommandParser {
    private final MudGame game;

    public GameCommandParser(MudGame game) {
        this.game = game;
    }

    public ResultOutput processTheVerbForGetRequestSplatterGUI(String username, String splatter) {
        String verbToHandle = findVerbForGetRequestSplatterGUI(splatter);

        if(verbToHandle.length() == 0){
            return ResultOutput.sorryIDontKnowHowToDoThat(splatter);
        }

        return processTheGetVerbInGame(username, splatter, verbToHandle, null);
    }



    // TODO: make specific DELETE, PUT, PATCH, OPTIONS,HEAD etc. verbs
    // currently PUT, PATCH and POST are treated the same
    // currently GET and DELETE are treated the same

    public ResultOutput processTheVerbForGetRequestSplatterAPI(String username, String splatter, RestMudHttpRequestDetails details) {
        String verbHandler = findVerbForGetRequestSplatterAPI(splatter);

        if(verbHandler.length() == 0){
            return ResultOutput.sorryIDontKnowHowToDoThat(splatter);
        }

        return processTheGetVerbInGame(username, splatter, verbHandler, details);
    }

    public ResultOutput processTheVerbForPostRequestAPI(String username, String verb, String nounphrase, RestMudHttpRequestDetails details) {
        String verbHandler = findVerbForPostRequestAPI(verb);

        if(verbHandler.length() == 0){
            return ResultOutput.sorryIDontKnowHowToDoThat(String.format("POST : %s",verb));
        }

        return processTheVerbInGame(username, nounphrase, verbHandler, details);
    }



    public ResultOutput processTheVerbForRequestAPI(RestMudHttpRequestDetails details, String username, String verb, String nounphrase) {

        ResultOutput result;

        switch(details.httpverb.toLowerCase()){
            case "post":
            case "put":
            case "patch":
                result = processTheVerbForPostRequestAPI(username, verb, nounphrase, details);
                break;
            case "delete":
            case "options":
            case "head":
                result = ResultOutput.sorryIDontKnowHowToDoThat(String.format("%s : %s",details.httpverb, verb));
                break;
            default:
                result = ResultOutput.sorryIDontKnowHowToDoThat(String.format("%s : %s",details.httpverb, verb));
                break;
        }

        return result;
    }

    public ResultOutput processTheGetVerbInGame(String username, String splatter, String verbToHandle, RestMudHttpRequestDetails details) {
        int nounPhraseStart = splatter.indexOf('/');
        String nounPhrase = splatter.substring(nounPhraseStart+1).toLowerCase();

        RestMudHttpRequestDetails useTheseDetails = details;

        if(useTheseDetails==null){
            useTheseDetails = new RestMudHttpRequestDetails("get");
        }


        ResultOutput resultOutput = game.processTheVerbInGame(username, verbToHandle, nounPhrase, useTheseDetails);

        if(resultOutput==null){
            resultOutput = new ResultOutput(LastAction.createError(String.format("Sorry, I don't know how to %s", splatter)));
        }

        return resultOutput;
    }

    public ResultOutput processThePostVerbInGame(String username, String nounPhrase, String verbToHandle) {

        ResultOutput resultOutput = game.processTheVerbInGame(username, verbToHandle, nounPhrase, new RestMudHttpRequestDetails("post"));

        if(resultOutput==null){
            resultOutput = new ResultOutput(LastAction.createError(String.format("Sorry, I don't know how to  %s %s", verbToHandle, nounPhrase)));
        }

        return resultOutput;
    }

    private ResultOutput processTheVerbInGame(String username, String nounPhrase, String verbToHandle, RestMudHttpRequestDetails details) {
        ResultOutput resultOutput = game.processTheVerbInGame(username, verbToHandle, nounPhrase, details);

        if(resultOutput==null){
            resultOutput = new ResultOutput(LastAction.createError(String.format("Sorry, I don't know how to %s %s", verbToHandle, nounPhrase)));
        }

        return resultOutput;
    }


    private String findVerbForGetRequestSplatterGUI(String splatter) {

        // does splatter match an enabled player GET request?
        // does splatter match an enabled game GET request?

        return findVerbForRequestSplatter(splatter, game.getVerbMode().getGuiMatchers());

    }

    public String findVerbForGetRequestSplatterAPI(String splatter) {
        // does splatter match an enabled player GET request?

        return findVerbForRequestSplatter(splatter, game.getVerbMode().getApiGetVerbMatchers());
    }

    public String findVerbForPostRequestAPI(String verbPhrase) {
        // does splatter match an enabled player GET request?

        return findVerbForRequestSplatter(verbPhrase, game.getVerbMode().getApiPostVerbMatchers());
    }

    public String findVerbForRequestSplatter(String splatter, List<VerbRegexToVerbMatch> matchers) {

        String verbHandler = "";

        for(VerbRegexToVerbMatch routeMatcher : matchers){
            if(splatter.matches(routeMatcher.getVerbRegex())){
                verbHandler = routeMatcher.getMatchingVerb();
                break;
            }
        }

        if(verbHandler.length()==0) {
            // is it a local verb?

            for(VerbRegexToVerbMatch routeMatcher : game.getLocalVerbs()){
                if(splatter.matches(routeMatcher.getVerbRegex())){
                    verbHandler = routeMatcher.getMatchingVerb();
                    break;
                }
            }
        }

        return verbHandler;
    }


}
