package uk.co.compendiumdev.restmud.web.server;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.output.json.JSendOutput;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;
import uk.co.compendiumdev.restmud.web.server.apistates.*;
import uk.co.compendiumdev.restmud.web.server.httpmessages.RequestDataExtractor;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Map;

import static spark.Spark.halt;

/**
 * Created by Alan on 30/05/2016.
 */
public class RestMudApiServer {

    private final MudGame game;
    private final IAuthenticator authenticator;
    private final GameCommandParser gameParser;

    public RestMudApiServer(MudGame game, IAuthenticator authenticator) {
        this.game = game;
        this.authenticator = authenticator;
        this.gameParser = new GameCommandParser(game);
    }

    public String login(Request request, Response response) {

        ApiResponseFor apiResponse = new ApiResponseFor(request);

        ApiRequestFrom apiRequest = new ApiRequestFrom(request);
        String jsonPayload = apiRequest.bodyAsJson();

        Gson gson = new Gson();
        Map m = gson.fromJson(jsonPayload, Map.class);

        String username = ((String)m.get("username")).toLowerCase();
        String password = (String)m.get("password");

        // loginUser should return a ErrorOrSuccessAction
        LoginState loginState =  authenticator.loginUser(request, username, password);

        ResultOutput resultOutput=null;
        LastAction lastAction;

        int stateReturnCode=0;

        switch (loginState) {
            case SUCCESS_LOGGEDIN:
                stateReturnCode = 200;

                lastAction = LastAction.createSuccess(String.format("user %s is now logged in",
                        authenticator.getAuthenticatedUserFromSession(request).userName()));
                resultOutput = new ResultOutput(lastAction).setUserDetails(game.getUserManager().getUserDetails(username));

                break;
            case ERROR_ALREADY_LOGGEDIN:
                stateReturnCode = 400;
                lastAction = LastAction.createError(
                        String.format("User already logged in as %s",
                                authenticator.getAuthenticatedUserFromSession(request).userName()));
                resultOutput = new ResultOutput(lastAction);
                break;
            case ERROR_INVALID_DETAILS:
                stateReturnCode = 401;
                lastAction = LastAction.createError("Details do not match any registered user");
                resultOutput = new ResultOutput(lastAction);
                break;
        }

        return readyTheHttpResponse(response, apiResponse, resultOutput, stateReturnCode);

    }




    public String register(Request request, Response response) {

        ApiResponseFor apiResponse = new ApiResponseFor(request);

        ApiRequestFrom apiRequest = new ApiRequestFrom(request);
        String jsonPayload = apiRequest.bodyAsJson();

        Gson gson = new Gson();
        Map m = gson.fromJson(jsonPayload, Map.class);

        String username = ((String)m.get("username")).toLowerCase();
        String displayname = (String)m.get("displayname");
        String password = (String)m.get("password");
        String secretGameCode = (String)m.get("secret");

        RegisterUserState state =  authenticator.registerUser(request, username, displayname, password, secretGameCode);

        JSendOutput stateReturn=null;
        int statusCode=0;

        switch(state.getState()){
            case ERROR_INVALID_DETAILS:
            case ERROR_ALREADY_LOGGEDIN:
            case ERROR_ALREADY_REGISTERED:
            case INVALID_GAME_CODE_SUPPLIED:
                stateReturn = JSendOutput.Fail().failMessage(
                        "Invalid Registration",
                        state.getDescription());
                statusCode = 400;
                break;
            case SUCCESS_REGISTERED_USER:
                statusCode=200;
                stateReturn = JSendOutput.Success().
                        successMessage(state.getDescription()).
                        addMessage("username", username).
                        addMessage(Authenticator.USER_AUTH_HEADER, game.getUserManager().getUser(username).getAuthToken());

                break;
        }

        halt(statusCode, readyTheHttpResponse(response, apiResponse, stateReturn));
        return null;
    }

    public String getPlayerDetails(Request request, Response response) {
        ApiResponseFor apiResponse = new ApiResponseFor(request);

        apiUserMustBeAuthenticated(request);

        String username = request.params(":username").toLowerCase();

        return readyTheHttpResponse(response, apiResponse, game.processGetUserDetails(username));
    }

    private  void apiUserMustBeAuthenticated(Request request) {

        ApiResponseFor apiResponse = new ApiResponseFor(request);

        String username = request.params(":username").toLowerCase();

        AuthenticatedUserState responseState = authenticator.getUserAuthenticatedState(request, username, game);
        if(responseState.getState()!= AuthenticatedUserStates.SUCCESS_AUTHENTICATED_USER){
            JSendOutput stateReturn = JSendOutput.Fail().failMessage(
                    "Authentication Error", responseState.getDescription());
            halt(403, apiResponse.getResponseBodyText(stateReturn.asJson()));
        }
    }

    public String getPlayerVerbHandler(Request request, Response response) {
        ApiResponseFor apiResponse = new ApiResponseFor(request);

        apiUserMustBeAuthenticated(request);

        String username = request.params(":username").toLowerCase();

        String splatter = RequestDataExtractor.getSplatterFromRequest(request).toLowerCase();
        RestMudHttpRequestDetails details = new RestMudHttpRequestDetails(request.headers(), request.requestMethod());

        ResultOutput resultOutput;

        // no data in the request
        if(splatter.length()==0){
            // by default return the user details as the result output
            resultOutput = game.processGetUserDetails(username);
        }else {
            resultOutput = gameParser.processTheVerbForGetRequestSplatterAPI(username, splatter, details);
        }

        return readyTheHttpResponse(response, apiResponse, resultOutput);

    }

    public String deletePlayerVerbHandler(Request request, Response response) {
        ApiResponseFor apiResponse = new ApiResponseFor(request);

        apiUserMustBeAuthenticated(request);

        String username = request.params(":username").toLowerCase();

        String splatter = RequestDataExtractor.getSplatterFromRequest(request).toLowerCase();
        RestMudHttpRequestDetails details = new RestMudHttpRequestDetails(request.headers(), request.requestMethod());

        ResultOutput resultOutput;

        // no data in the request
        if(splatter.length()==0){
            // by default logout the user for a delete request
            return logoutPlayer(request, response);
        }else {
            resultOutput = gameParser.processTheVerbForGetRequestSplatterAPI(username, splatter, details);
        }

        return readyTheHttpResponse(response, apiResponse, resultOutput);
    }

    public String logoutPlayer(Request request, Response response) {
        ApiResponseFor apiResponse = new ApiResponseFor(request);

        ResultOutput resultOutput = logoutCurrentUserSession(request);

        return readyTheHttpResponse(response, apiResponse, resultOutput);
    }

    private ResultOutput logoutCurrentUserSession(Request request) {
        MudUser user = authenticator.getAuthenticatedUserFromSession(request);
        authenticator.removeAuthenticatedUser(request);

        LastAction loggedOut = LastAction.createSuccess(user.userName() + " has been logged out");

        return new ResultOutput(loggedOut);
    }

    public String logoutGivenPlayer(Request request, Response response) {

        ApiResponseFor apiResponse = new ApiResponseFor(request);

        // this to support admin logging out users


        apiUserMustBeAuthenticated(request);

        String username = request.params(":username").toLowerCase();

        ResultOutput resultOutput = new ResultOutput(LastAction.createError("Could not process request"));

        // if same user as making request then log them out
        if(authenticator.usernameMatchesSession(username, request)){
            resultOutput = logoutCurrentUserSession(request);
        }else{
            // if on behalf of wizard
            if(authenticator.isWizard(request,game)){
                authenticator.addUserToLogMeOutQueue(username);
                resultOutput = new ResultOutput(LastAction.createSuccess(username + " queued for logout"));
            }
        }

        return readyTheHttpResponse(response, apiResponse, resultOutput);
    }

    /* used for any request that has verb noun in body e.g. put, post etc. */
    public String handlePlayerVerbNounBody(Request request, Response response) {

        ApiResponseFor apiResponse = new ApiResponseFor(request);

        apiUserMustBeAuthenticated(request);

        String username = request.params(":username").toLowerCase();


        ApiRequestFrom apiRequest = new ApiRequestFrom(request);
        // body should be json unless content-type is application/xml
        String jsonPayload = apiRequest.bodyAsJson();

        Gson gson = new Gson();
        Map m = gson.fromJson(jsonPayload, Map.class);

        String verb = (String) m.get("verb");
        String nounphrase = (String) m.get("nounphrase");

        // TODO: check payload and prevent server error with a halt on bad request
        ResultOutput resultOutput;
        if(verb==null){
            resultOutput = new ResultOutput(LastAction.createError("Sorry I don't know what you mean by " + jsonPayload));
        }else {
            RestMudHttpRequestDetails details = new RestMudHttpRequestDetails(request.headers(), request.requestMethod());
            resultOutput = gameParser.processTheVerbForRequestAPI(details, username, verb, nounphrase);
        }

        return readyTheHttpResponse(response, apiResponse, resultOutput);

    }

    private String readyTheHttpResponse(Response response, ApiResponseFor apiResponse, ResultOutput resultOutput) {
        response.type(apiResponse.getResponseFormatHeader());

        return apiResponse.getResponseBodyText(
                FormatResultOutputAsJSendOutput.asString(resultOutput, response));
    }

    private String readyTheHttpResponse(Response response, ApiResponseFor apiResponse, ResultOutput resultOutput, int httpCode) {
        response.type(apiResponse.getResponseFormatHeader());

        return apiResponse.getResponseBodyText(
                FormatResultOutputAsJSendOutput.asString(resultOutput, response, httpCode));
    }

    private String readyTheHttpResponse(Response response, ApiResponseFor apiResponse, JSendOutput jsendOutput) {
        response.type(apiResponse.getResponseFormatHeader());

        return apiResponse.getResponseBodyText(jsendOutput.asJson());
    }


    public String handleGetGenericApiCall(Request request, Response response) {
        return handleGenericUnhandledApiErrorCall(request, response);
    }

    public String handlePostGenericApiCall(Request request, Response response) {
        return handleGenericUnhandledApiErrorCall(request, response);
    }

    public String handleGenericUnhandledApiErrorCall(Request request, Response response) {
        ApiResponseFor apiResponse = new ApiResponseFor(request);

        String splatter = RequestDataExtractor.getSplatterFromRequest(request);
        JSendOutput stateReturn = JSendOutput.Fail().failMessage(
                "Cannot", String.format("Sorry I don't know how to: %s %s", request.requestMethod(), splatter));

        halt(400, readyTheHttpResponse(response, apiResponse, stateReturn));

        return null;

    }



}
