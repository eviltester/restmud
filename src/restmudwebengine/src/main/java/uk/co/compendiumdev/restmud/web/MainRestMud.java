package uk.co.compendiumdev.restmud.web;

import uk.co.compendiumdev.restmud.web.config.AppConfig;
import uk.co.compendiumdev.restmud.web.config.AppConfigFromArgs;
import uk.co.compendiumdev.restmud.engine.game.PlayerMode;
import uk.co.compendiumdev.restmud.web.server.RestMudWizServer;
import uk.co.compendiumdev.restmud.web.server.apistates.Authenticator;
import uk.co.compendiumdev.restmud.web.server.apistates.IAuthenticator;
import uk.co.compendiumdev.restmud.web.server.apistates.SinglePlayerAuthenticator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.web.server.httpmessages.RequestDataExtractor;
import uk.co.compendiumdev.restmud.web.server.RestMudApiServer;
import uk.co.compendiumdev.restmud.web.server.RestMudHtmlServer;
import spark.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static spark.Spark.*;

/**
 * Created by alan on 14/12/2015.
 */
public class MainRestMud {


        public static MudGame game;
        public static IAuthenticator authenticator;
        public static RestMudHtmlServer html;
        public static RestMudApiServer api;
        public static RestMudWizServer wiz;




        static boolean hasHerokuAssignedPort(){
            ProcessBuilder processBuilder = new ProcessBuilder();
            return (processBuilder.environment().get("PORT") != null);
        }
        static int getHerokuAssignedPort() {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (hasHerokuAssignedPort()) {
                return Integer.parseInt(processBuilder.environment().get("PORT"));
            }
            return -1; //return default port if heroku-port isn't set (i.e. on localhost)
        }

        public static void singlePlayerSetup(AppConfig config){

            GameInitializer gameInitializer = new GameInitializer();
            gameInitializer.setWizardAuthHeader(Authenticator.WIZARD_AUTH_HEADER);
            gameInitializer.generate(config.gameName());
            //gameInitializer.addAWizard();

            game = gameInitializer.getGame();

            authenticator = new SinglePlayerAuthenticator(game);

            html = new RestMudHtmlServer(game, authenticator);
            api = new RestMudApiServer(game, authenticator);
            // wiz exists because the game might want to issue wiz broadcast messages
            // but no wizard has been setup so no-one can login as wizard
            wiz = new RestMudWizServer(game, authenticator);

            /* single player force logged in*/
            gameInitializer.addDefaultUser( SinglePlayerAuthenticator.SINGLE_PLAYER_DISPLAYNAME,
                    SinglePlayerAuthenticator.SINGLE_PLAYER_USERNAME,
                    SinglePlayerAuthenticator.SINGLE_PLAYER_PASSWORD);
        }

        public static void multiPlayerSetup(AppConfig config){

            GameInitializer gameInitializer = new GameInitializer();
            gameInitializer.setWizardAuthHeader(Authenticator.WIZARD_AUTH_HEADER);
            //gameInitializer.generate("test_game_basic");
            gameInitializer.generate(config.gameName());
            gameInitializer.addAWizard();
            gameInitializer.addDefaultUsers();

            game = gameInitializer.getGame();

            authenticator = new Authenticator(game);

            html = new RestMudHtmlServer(game, authenticator);
            api = new RestMudApiServer(game, authenticator);
            wiz = new RestMudWizServer(game, authenticator);

        }

        public static void main(String[] args) {

            // configure multi-player, single player etc. from command line
            AppConfig config = AppConfigFromArgs.getAppConfig(args);

            // added to support heroku as per https://sparktutorials.github.io/2015/08/24/spark-heroku.html
            // environment can override config for port
            if(hasHerokuAssignedPort()) {
                config.setPort(getHerokuAssignedPort());
                port(config.port());

            }

            // default to single player - set as multiplayer with -playermode multi
            if(config.playerMode() == PlayerMode.MULTI) {
                multiPlayerSetup(config);
            }else{
                singlePlayerSetup(config);
            }


            /**
             *
             * System GUI Routings
             *
             */
            get("/", (request, response) -> { return html.getIndexPage(request); });
            get("/help", (request, response) -> {   return html.getHelpPage(request, response); });


            // ***********************
            // MULTIPLAYER ONLY STARTS
            // ***********************
            if(config.playerMode() == PlayerMode.MULTI) {
                get("/logout", (request, response) -> {
                    return html.logout(request, response);
                });
                get("/login", (request, response) -> {
                    return html.getLoginPage(request, response);
                });
                get("/register", (request, response) -> {
                    return html.register(request, response);
                });
                get("/wiz", (request, response) -> {
                    return html.getWizHomePage(request, response);
                });

                /**
                 * System GUI Form Processing
                 */
                post("/login", (request, response) -> {
                    return html.handlePostLogin(request, response);
                });
                post("/register", (request, response) -> {
                    return html.handlePostRegister(request, response);
                });

                /**
                 * The System API Calls
                 */
                post("/api/login", (request, response) -> {
                    return api.login(request, response);
                });
                post("/api/register", (request, response) -> {
                    return api.register(request, response);
                });

                // logout the current logged in user
                post("/api/player/logout", (request, response) -> {
                    return api.logoutPlayer(request, response);
                });
                post("/api/player/:username/logout", (request, response) -> {
                    return api.logoutGivenPlayer(request, response);
                });


                // The Wiz interface - mostly a hack wrapped around API calls
                get("/wiz/config/playerGuiMode/:gameguimode", (request, response) -> {
                            return wizGuiRedirectAsJsonPageMessage(wiz.wizChangePlayerGUIMode(request, response), response);
                        }
                );

                get("/wiz/teleport/:locationid", (request, response) -> {
                            return wizGuiRedirectAsJsonPageMessage(wiz.wizTeleportToLocationId(request, response), response);
                        }
                );

                get("/wiz/lightlocation/:locationid", (request, response) -> {
                            return wizGuiRedirectAsJsonPageMessage(wiz.wizLightsUpLocation(request, response), response);
                        }
                );

                get("/wiz/darkenlocation/:locationid", (request, response) -> {
                            return wizGuiRedirectAsJsonPageMessage(wiz.wizDarkensLocation(request, response), response);
                        }
                );

                get("/wiz/config/gameVerbMode/:gameverbmode", (request, response) -> {
                            return wizGuiRedirectAsJsonPageMessage(wiz.wizSwitchesGameVerbMode(request, response), response);
                        }
                );

                get("/wiz/visibilityToOthers", (request, response) -> {
                            return wizGuiRedirectAsJsonPageMessage(wiz.wizChangesTheirVisibilityToOthers(request, response), response);
                        }
                );

                get("/wiz/setAuth", (request, response) -> {
                            return wizGuiRedirectAsJsonPageMessage(wiz.wizTogglesAnAuthenticationMode(request, response), response);
                        }
                );

                get("/wiz/close", (request, response) -> {
                            return wizGuiRedirectAsJsonPageMessage(wiz.wizClosesExitFromLocationToLocation(request, response), response);
                        }
                );

                get("/wiz/open", (request, response) -> {
                            return wizGuiRedirectAsJsonPageMessage(wiz.wizOpensExitFromLocationToLocation(request, response), response);
                        }
                );

                // generic splat wiz POST form interface
                post("/wiz/*", (request, response) -> {
                            return wizGuiRedirectAsJsonPageMessage(wiz.wizHandlePostCommand(request, response), response);
                        }
                );
            }
            // ***********************
            // MULTIPLAYER ONLY ENDS
            // ***********************

            /*
            * The game GUI interfaces
            */
            // Info page for user showing auth code
            get("/player/:username", (request, response) -> { return html.getPlayerHomePage(request); });
            get("/player/:username/help", (request, response) -> {  return html.getPlayerHelpPage(request);});
            // generic splat GUI GET handler
            get("/player/:username/*", (request, response) -> {   return html.getPlayerVerbHandler(request, response); });

            /*
            * The game API interfaces
            */
            // get the user details from the api
            get("/api/player/:username", (request, response) -> {  return api.getPlayerDetails(request, response); });
            // generic splat GET api
            get("/api/player/:username/*", (request, response) -> { return api.getPlayerVerbHandler(request, response); } );
            // allow DELETE requests to be used in game for actions
            delete("/api/player/:username/*", (request, response) -> { return api.deletePlayerVerbHandler(request, response); } );

            // generic POST api
            // http://restcookbook.com/HTTP%20Methods/put-vs-post/ post because not idempotent
            post("/api/player/:username", (request, response) -> {  return api.handlePlayerVerbNounBody(request, response); } );
            put("/api/player/:username", (request, response) -> {  return api.handlePlayerVerbNounBody(request, response); } );
            patch("/api/player/:username", (request, response) -> {  return api.handlePlayerVerbNounBody(request, response); } );

            // generic error handling splat apis
            get("/api/*", (request, response) -> {  return api.handleGetGenericApiCall(request, response); } );
            post("/api/*", (request, response) -> { return api.handlePostGenericApiCall(request, response); } );
            put("/api/*", (request, response) -> { return api.handleGenericUnhandledApiErrorCall(request, response); } );
            head("/api/*", (request, response) -> { return api.handleGenericUnhandledApiErrorCall(request, response); } );
            options("/api/*", (request, response) -> { return api.handleGenericUnhandledApiErrorCall(request, response); } );
            delete("/api/*", (request, response) -> { return api.handleGenericUnhandledApiErrorCall(request, response); } );
            patch("/api/*", (request, response) -> { return api.handleGenericUnhandledApiErrorCall(request, response); } );


            // generic GUI and API everything error handling splat GET api
                get("/*", (request, response) -> {

                            String splatter = RequestDataExtractor.getSplatterFromRequest(request);

                            response.redirect("/help?error=" + splatter);
                            response.status(400);
                            return null;
                        }
                );

            }

    private static String wizGuiRedirectAsJsonPageMessage(String json, Response response) {

        String resultString = "";
        try {
            resultString = URLEncoder.encode(json,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            resultString = "Could not encode json on wiz redirect";
        }
        response.redirect("/wiz?message=" + resultString);
        return null;
    }





    /*
     User
        - display name
        - username
        - password
        - game session
        - location
        - health
        - inventory
      */


}
