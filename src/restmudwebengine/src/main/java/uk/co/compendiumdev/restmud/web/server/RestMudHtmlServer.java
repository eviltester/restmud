package uk.co.compendiumdev.restmud.web.server;

import uk.co.compendiumdev.restmud.web.server.apistates.*;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.PlayerGuiMode;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;
import uk.co.compendiumdev.restmud.web.server.html.RestMudRenderer;
import uk.co.compendiumdev.restmud.web.server.httpmessages.RequestDataExtractor;
import com.google.gson.Gson;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

/**
 * Created by Alan on 30/05/2016.
 */
public class RestMudHtmlServer {


    private final RestMudRenderer renderer;
    private final MudGame game;
    private final IAuthenticator authenticator;
    private final GameCommandParser gameParser;

    public RestMudHtmlServer(MudGame game, IAuthenticator authenticator) {
        this.renderer = new RestMudRenderer(game);
        this.game = game;
        this.authenticator = authenticator;
        this.gameParser = new GameCommandParser(game);
    }

    public String getIndexPage(Request request) {
        String status = request.queryParams("status");

        return renderer.getIndexPageHtml(status);
    }

    public String getHelpPage(Request request, Response response) {


        if(!authenticator.isMultiPlayer()){
            return getPlayerHelpPage(request);
        }

        String requestErrorParamValue = request.queryParams("error");

        if(requestErrorParamValue!=null){
            response.status(400);
        }

        PlayerGuiMode guiMode = game.getGuiMode();

        return renderer.getHelpPage(requestErrorParamValue, guiMode);
    }

    public String logout(Request request, Response response) {
        authenticator.removeAuthenticatedUser(request);
        response.redirect("/?status=loggedOut");
        return null;
    }

    // login from GUI is a page shown with a login form
    //     login page posts to loginform
    //     errors redirect to login page
    //     success redirects to 'look' page
    // login from API is a message with either error response or 200 with session code
    //
    public String getLoginPage(Request request, Response response) {
        // user is registered, do they already have a session. if so use it
        MudUser authenticatedUser = authenticator.getAuthenticatedUserFromSession(request);
        String username=null;

        if(authenticatedUser!=null) {
            username = authenticatedUser.userName();
            // user is already authenticated so redirect to look
            response.redirect(String.format("/player/%s/look", username));
            return null;
        }

        String errorParam = request.queryParams("error");
        if(errorParam != null){
            response.status(403);
        }

        String errorMessage=null;

        if(!authenticator.get_allow_login_form()) {
            errorMessage = "The Login Form has been disabled.";
        }



        return renderer.renderLoginPage(username, errorParam, errorMessage);
    }

    public String register(Request request, Response response) {

        // user is registered, do they already have a session. if so use it
        MudUser authenticatedUser = authenticator.getAuthenticatedUserFromSession(request);
        String username = null;

        if(authenticatedUser != null) {
            username = authenticatedUser.userName();
            // user is already authenticated so redirect to look
            response.redirect(String.format("/player/%s/look", username));
            return null;
        }

        String errorMessage = request.queryParams("errormessage");
        if(errorMessage!=null){
            response.status(400);
        }

        return renderer.renderRegisterPage(username, errorMessage);
    }

    public String getWizHomePage(Request request, Response response) {

        if(!authenticator.isWizard(request, game)){
            //response.redirect(String.format("/help?error=%s",UrlEncoded.encodeString("/wiz - Forbidden Page! Wizards Only!")));
            //response.status(403);
            halt(403,"<h1>Forbidden Page! Wizards Only!</h1>");
            //return null;
        }

        MudUser authenticatedUser = authenticator.getAuthenticatedUserFromSession(request);
        String username = null;

        if(authenticatedUser != null) {
            username = authenticatedUser.userName();
        }

        // if authenticated user is a wiz

        String errorMessage = request.queryParams("errormessage");
        if(errorMessage!=null){
            response.status(400);
        }


        Map<String,String> extraParams = new HashMap<String, String>();
        extraParams.put("allow_auth_header",String.valueOf(authenticator.get_allow_auth_header()));
        extraParams.put("allow_basic_auth",String.valueOf(authenticator.get_allow_basic_auth()));
        extraParams.put("allow_login_form",String.valueOf(authenticator.get_allow_login_form()));

        String infoMessage = request.queryParams("message");
        if(infoMessage!=null) {
            extraParams.put("infoMessage",infoMessage);
        }

        return renderer.renderWizPage(errorMessage, username, extraParams);
    }

    // login as an action from the GUI
    public String handlePostLogin(Request request, Response response) {

        MultiMap<String> params = new MultiMap<String>();
        UrlEncoded.decodeTo(request.body(), params, "UTF-8");

        String username = params.get("username").get(0);
        String password = params.get("password").get(0);

        LoginState loginState = authenticator.loginUser(request, username, password);

        String stateRedirectUrl="";

        switch (loginState) {
            case SUCCESS_LOGGEDIN:
                stateRedirectUrl = String.format("/player/%s/look", authenticator.getAuthenticatedUserFromSession(request).userName());
                break;
            case ERROR_ALREADY_LOGGEDIN:
                stateRedirectUrl = String.format("/player/%s/look?alreadyLoggedIn", authenticator.getAuthenticatedUserFromSession(request).userName());
                break;
            case ERROR_INVALID_DETAILS:
                stateRedirectUrl = String.format("/login?error");
                break;
        }

        response.redirect(stateRedirectUrl);
        return null;
    }

    public String handlePostRegister(Request request, Response response) {
        MultiMap<String> params = new MultiMap<String>();
        UrlEncoded.decodeTo(request.body(), params, "UTF-8");

        String username = params.get("username").get(0);
        String displayname = params.get("displayname").get(0);
        String password = params.get("password").get(0);
        String secretGameCode = params.get("secret").get(0);

        RegisterUserState state =  authenticator.registerUser(request, username, displayname, password, secretGameCode);

        switch(state.getState()){
            case ERROR_INVALID_DETAILS:
            case ERROR_ALREADY_LOGGEDIN:
            case ERROR_ALREADY_REGISTERED:
            case INVALID_GAME_CODE_SUPPLIED:
                response.redirect("/register?errormessage="+UrlEncoded.encodeString(state.getDescription()));
                break;
            case SUCCESS_REGISTERED_USER:
                response.redirect(String.format("/player/%s/look?registered", username));
                break;
        }

        return null;
    }

    public String getPlayerHomePage(Request request) {
        handleGuiUsernameMustBeRegisteredAndLoggedInToPlay(request);

        String username = request.params(":username");

        ResultOutput resultOutput = game.processGetUserDetails(username);

        Gson gson = new Gson();
        String lastActionResponse = gson.toJson(resultOutput);

        Map map = gson.fromJson(lastActionResponse, Map.class);

        map.put("username", username);

        return new MustacheTemplateEngine().render(new ModelAndView(map, "look.mustache"));
    }


    private void handleGuiUsernameMustBeRegisteredAndLoggedInToPlay(Request request) {

        String username = request.params(":username");

        AuthenticatedUserState responseState = authenticator.getUserAuthenticatedState(request, username, game);

        switch(responseState.getState()){
            case ERROR_DIFFERENT_USER_LOGGED_IN:
                halt(403, responseState.getDescription());
                break;
            case ERROR_INVALID_BASIC_AUTH_DETAILS:
            case ERROR_USER_NOT_REGISTERED:
                halt(404, responseState.getDescription());
                break;
            case SUCCESS_AUTHENTICATED_USER:
                break;
        }
    }

    public String getPlayerHelpPage(Request request) {
        handleGuiUsernameMustBeRegisteredAndLoggedInToPlay(request);

        String username = request.params(":username");

        Map map = new HashMap();

        if(!authenticator.isMultiPlayer()){
            username=SinglePlayerAuthenticator.SINGLE_PLAYER_USERNAME;
            map.put("single_player_mode", true);
        }

        PlayerGuiMode guiMode = game.getGuiMode(username);

        switch(guiMode){
            case SUPER_EASY:
            case EASY:
                map.put("display_player_hrefs",true);
        }

        map.put("username", username);
        return new MustacheTemplateEngine().render(new ModelAndView(map, "help.mustache"));
    }

    public String getPlayerVerbHandler(Request request, Response response) {
        handleGuiUsernameMustBeRegisteredAndLoggedInToPlay(request);

        String username = request.params(":username");

        String splatter = RequestDataExtractor.getSplatterFromRequest(request);


        Gson gson = new Gson();

        ResultOutput resultOutput;

        if(splatter.length()>0) {
            resultOutput = gameParser.processTheVerbForGetRequestSplatterGUI(username, splatter);
        }else{
            response.status(400);
            resultOutput = ResultOutput.sorryIDontKnowHowToDoThat(" - Are you using UTF-8 encoded english?");
        }

        String lastActionResponse = gson.toJson(resultOutput);

        System.out.println(lastActionResponse);

        PlayerGuiMode guiMode = game.getGuiMode(username);

        Map map = gson.fromJson(lastActionResponse, Map.class);

        map.put("username", username);

        if(authenticator.isWizard(request, game)){
            // if they are a wizard, add the wizard options to the GUI
            map.put("thisIsAWizard", true);
        }

        switch(guiMode){
            case SUPER_EASY:
                // display hrefs for player verbs on look screen
                map.put("display_collectables_verbs",true);

                // make the exits hyperlinks to click on to move
                map.put("display_exits_verbs",true);

                // links to look, inventory, help etc
                map.put("display_youcanalso_verbs",true);

                // add take, drop, hoard links to the inventory
                map.put("display_inventory_with_verbs",true);

                // show exits as open and closed
                // with  hyperlinks to open and close
                map.put("display_visible_gate_verbs",true);

                // adds javascript to make you can also verbs work
                map.put("add_verb_javascript",true);

                break;
            case EASY:
                // display ids in inventory  of collectables
                map.put("display_inventory_with_ids",true);

                // show exits as open and closed
                map.put("display_visible_gate_status",true);

                // adds javascript to make player verbs work
                map.put("add_verb_javascript",true);

                break;
            case NORMAL:
                // display ids in location but not in inventory - need to use API for that
                map.put("display_inventory_basicsonly",true);

        }

        return new MustacheTemplateEngine().render(new ModelAndView(map, "look.mustache"));


    }
}
