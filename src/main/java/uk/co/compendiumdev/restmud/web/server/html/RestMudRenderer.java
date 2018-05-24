package uk.co.compendiumdev.restmud.web.server.html;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.PlayerGuiMode;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;

import java.util.HashMap;
import java.util.Map;

public class RestMudRenderer {

    private final MudGame game;

    public RestMudRenderer(MudGame game) {
        this.game = game;
    }

    public String getIndexPageHtml(String status) {

        Map map = new HashMap();

        if(status!=null && status.contentEquals("loggedOut")){
            map.put("amLoggedOut", true);
        }

        if (game.getUserManager().numberOfUsers() > 0) {
            map.put("displayusers", "yes");
            map.put("users", game.getUserManager().getUsers());
            map.put("totalscore", game.getTotalScore());
        }

        return new MustacheTemplateEngine().render(new ModelAndView(map, "frontpage.mustache"));
    }

    public String getHelpPage(String requestErrorParamValue, PlayerGuiMode guiMode) {

        Map map = new HashMap();

        if(requestErrorParamValue != null) {
            map.put("errormessage", String.format("Sorry, I don't know how to: %s", requestErrorParamValue));
        }

        switch(guiMode){
            case SUPER_EASY:
            case EASY:
                map.put("display_player_hrefs",true);
        }

        return new MustacheTemplateEngine().render(new ModelAndView(map, "help.mustache"));
    }

    public String renderLoginPage(String userName, String errorParam, String errorMessage) {
            // Render any error messages on the page if we were sent back here
            Map map = new HashMap();

            if(userName!=null){
                map.put("errormessage", String.format("You are already logged in as %s", userName));
                map.put("loggedinas", userName);
            }

            if(errorParam != null) {
                map.put("errormessage", "You were unable to login. Are you sure your details were correctly entered?");
            }

            if(errorMessage != null) {
                map.put("errormessage", String.format("ERROR: %s", errorMessage));
            }

            // render the login page for the GUI
            return new MustacheTemplateEngine().render(new ModelAndView(map, "login.mustache"));
    }

    public String renderRegisterPage(String username, String errorMessage) {

            Map map = new HashMap();

            if (username != null) {
                map.put("errormessage",
                        String.format("You cannot register a user because you are already logged in as %s", username));
                map.put("loggedinas", username);
            }

            if(errorMessage != null) {
                map.put("errormessage", errorMessage);
            }

            return new MustacheTemplateEngine().render(new ModelAndView(map, "register.mustache"));
    }

    public String renderWizPage(String errorMessage, String username, Map<String, String> extraParams) {

        Map map = new HashMap();

        map.put("GameSecretCode", game.getSecretGameRegistrationCode().get());
        map.put("GuiModeValue", game.getGuiMode().templateGuiModeString());
        map.put("builtInGames", new MudGameDefinitionSerialiser().getBuiltInGamesAsIdDescriptionPairs());

        if(errorMessage != null) {
            map.put("errormessage", errorMessage);
        }

        map.put("GameVerbModeName", game.getVerbModeName());

        map.put("verbModes", game.getVerbModes());

        map.put("username", username);

        map.put("locations", game.getGameLocations().getLocationsAsIdDescriptionPairs());

        map.put("gates", game.getGatesAsIdDescriptionPairs());

        map.put("players", game.getUserManager().getUsersAsIdDescriptionPairs());

        map.putAll(extraParams);

        return new MustacheTemplateEngine().render(new ModelAndView(map, "wiz.mustache"));
    }
}
