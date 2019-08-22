package uk.co.compendiumdev.restmud.web.server;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;
import uk.co.compendiumdev.restmud.engine.game.generators.RandomTreasureGenerator;
import uk.co.compendiumdev.restmud.output.json.JSendOutput;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;
import uk.co.compendiumdev.restmud.web.server.apistates.IAuthenticator;
import uk.co.compendiumdev.restmud.web.server.httpmessages.RequestDataExtractor;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

/**
 * Created by Alan on 25/07/2016.
 */
public class RestMudWizServer {


    private final MudGame game;
    private final IAuthenticator authenticator;
    private final GameCommandParser gameParser;

    public RestMudWizServer(MudGame game, IAuthenticator authenticator) {
        this.game = game;
        this.authenticator = authenticator;
        this.gameParser = new GameCommandParser(game);
    }

    public String wizChangePlayerGUIMode(Request request, Response response) {
        apiUserMustBeAuthenticatedAsAWizard(request);

        String gameMode = request.params(":gameguimode");
        ResultOutput resultOutput =  game.setGameGuiMode(gameMode);
        return FormatResultOutputAsJSendOutput.asString(resultOutput, response);
    }

    public String wizTeleportToLocationId(Request request, Response response) {
        apiUserMustBeAuthenticatedAsAWizard(request);

        String locationId = request.params(":locationid");

        ResultOutput resultOutput =  game.teleportUserTo(authenticator.getAuthenticatedUserFromSession(request).userName(), locationId);
        return FormatResultOutputAsJSendOutput.asString(resultOutput, response);
    }

    public String wizLightsUpLocation(Request request, Response response) {
        apiUserMustBeAuthenticatedAsAWizard(request);

        String locationId = request.params(":locationid");

        ResultOutput resultOutput =  game.lightLocation(locationId);
        return FormatResultOutputAsJSendOutput.asString(resultOutput, response);
    }

    public String wizDarkensLocation(Request request, Response response) {
        apiUserMustBeAuthenticatedAsAWizard(request);

        String locationId = request.params(":locationid");

        ResultOutput resultOutput =  game.darkenLocation(locationId);
        return FormatResultOutputAsJSendOutput.asString(resultOutput, response);
    }


    public String wizSwitchesGameVerbMode(Request request, Response response) {
        apiUserMustBeAuthenticatedAsAWizard(request);

        String gameModeName = request.params(":gameverbmode");
        ResultOutput resultOutput = game.setVerbModeViaApi(gameModeName);
        return FormatResultOutputAsJSendOutput.asString(resultOutput, response);
    }






    public String wizChangesTheirVisibilityToOthers(Request request, Response response) {
        apiUserMustBeAuthenticatedAsAWizard(request);

        String visible = request.queryParams("visible");
        boolean visibility = visible.equalsIgnoreCase("true");

        MudUser wiz = authenticator.getAuthenticatedUserFromSession(request);
        wiz.setVisibilityToOthers(visibility);

        response.redirect("/wiz?message=Visibility%20Is%20Now%20" + visible);
        return null;
    }

    public String wizTogglesAnAuthenticationMode(Request request, Response response) {
        apiUserMustBeAuthenticatedAsAWizard(request);

        String mode = request.queryParams("mode");
        String value = request.queryParams("value");
        boolean toggle = value.equalsIgnoreCase("true");

        authenticator.toggleAuthMode(mode,toggle);

        response.redirect("/wiz?message=Auth%20Mode%20" + mode + "%20Toggled%20To%20" + value);
        return null;
    }


    private void apiUserMustBeAuthenticatedAsAWizard(Request request) {
        if(!authenticator.isWizard(request, game)){
            JSendOutput stateReturn = JSendOutput.Fail().failMessage(
                    "Authentication Error", "You are not a wizard!");
            halt(403, stateReturn.asJson());
        }
    }

    public String wizClosesExitFromLocationToLocation(Request request, Response response) {
        apiUserMustBeAuthenticatedAsAWizard(request);

        String fromLocation = request.queryParams("location");
        String toLocation = request.queryParams("toLocation");

        ResultOutput resultOutput = game.closeGate(fromLocation, toLocation);
        return FormatResultOutputAsJSendOutput.asString(resultOutput, response);
    }

    public String wizOpensExitFromLocationToLocation(Request request, Response response) {
        apiUserMustBeAuthenticatedAsAWizard(request);

        String fromLocation = request.queryParams("location");
        String toLocation = request.queryParams("toLocation");

        ResultOutput resultOutput = game.openGate(fromLocation, toLocation);
        return FormatResultOutputAsJSendOutput.asString(resultOutput, response);
    }

    public String wizHandlePostCommand(Request request, Response response) {

        apiUserMustBeAuthenticatedAsAWizard(request);

        String splatter = RequestDataExtractor.getSplatterFromRequest(request);
        boolean processedSplatter = false;

        ResultOutput resultOutput=null;

        if(splatter.equalsIgnoreCase("changeSecretCode")) {

            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(request.body(), params, "UTF-8");

            String secretGameCode = params.get("secret").get(0);

            game.getSecretGameRegistrationCode().set(secretGameCode);

            resultOutput = ResultOutput.getLastActionSuccess("changed the Secret Game Code to " + secretGameCode);
        }

        if(splatter.equalsIgnoreCase("shout")) {

            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(request.body(), params, "UTF-8");

            String shoutyMessage = params.get("shouted").get(0);

            game.broadcastMessages().wizardBroadcaseMessage(shoutyMessage);

            resultOutput = ResultOutput.getLastActionSuccess("The Wizard Shouted " + shoutyMessage);
        }

        if(splatter.equalsIgnoreCase("trimmessages")) {

            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(request.body(), params, "UTF-8");

            String messageCount = params.get("retainmessagecount").get(0);

            game.broadcastMessages().trimTo(Integer.parseInt(messageCount));

            resultOutput = ResultOutput.getLastActionSuccess("The Wizard Trimmed messages to " + messageCount);
        }



        if(splatter.equalsIgnoreCase("clearmessages")) {

            game.broadcastMessages().clear();

            resultOutput = ResultOutput.getLastActionSuccess("The Wizard Cleared all messages");
        }

        if(splatter.equalsIgnoreCase("addrandomtreasures")){
            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(request.body(), params, "UTF-8");
            String messageCount = params.get("numberoftreasures").get(0);
            new RandomTreasureGenerator(game).createHoardableTreasures(Integer.parseInt(messageCount));
            resultOutput = ResultOutput.getLastActionSuccess("The Wizard Added Treasures - " + game.getGameCollectables().count());
        }

        if(splatter.equalsIgnoreCase("addhoardablejunk")){
            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(request.body(), params, "UTF-8");
            String messageCount = params.get("numberofjunk").get(0);
            new RandomTreasureGenerator(game).createHoardableJunk(Integer.parseInt(messageCount));
            resultOutput = ResultOutput.getLastActionSuccess("The Wizard Added Hoardable Junk - " + game.getGameCollectables().count());
        }

        if(splatter.equalsIgnoreCase("addrandomjunk")){
            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(request.body(), params, "UTF-8");
            String messageCount = params.get("numberofjunk").get(0);
            new RandomTreasureGenerator(game).createNonHoardableJunk(Integer.parseInt(messageCount));
            resultOutput = ResultOutput.getLastActionSuccess("The Wizard Added More Junk - " + game.getGameCollectables().count());
        }

        if(splatter.equalsIgnoreCase("addfaketreasure")){
            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(request.body(), params, "UTF-8");
            String messageCount = params.get("numberofitems").get(0);
            new RandomTreasureGenerator(game).createNonHoardableTreasures(Integer.parseInt(messageCount));
            resultOutput = ResultOutput.getLastActionSuccess("The Wizard Added More Fake Treasure - " + game.getGameCollectables().count());
        }

        if(splatter.equalsIgnoreCase("morehoards")){
            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(request.body(), params, "UTF-8");
            String messageCount = params.get("numberofhoards").get(0);
            new RandomTreasureGenerator(game).createHoards(Integer.parseInt(messageCount));
            resultOutput = ResultOutput.getLastActionSuccess("The Wizard Added More Hoards ");
        }

        if(splatter.equalsIgnoreCase("resetgamestate")){
            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(request.body(), params, "UTF-8");
            game.resetGame();
            String message = "The Wizard Reset the Game";
            game.broadcastMessages().wizardBroadcaseMessage(message);
            resultOutput = ResultOutput.getLastActionSuccess(message);
        }

        if(splatter.equalsIgnoreCase("loadgame")){
            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(request.body(), params, "UTF-8");
            String gameName = params.get("gamename").get(0);
            MudGameDefinitionSerialiser serialiser = new MudGameDefinitionSerialiser();
            MudGameDefinition defn = serialiser.createDefnFromJson(serialiser.readJsonFromResource("/games/" + gameName));
            game.resetFrom(defn);
            String message = "The Wizard Reset the Game";
            game.broadcastMessages().wizardBroadcaseMessage(message);
            resultOutput = ResultOutput.getLastActionSuccess(message);
        }

        if(splatter.equalsIgnoreCase("usegamedefinition")){
            MultiMap<String> params = new MultiMap<String>();
            UrlEncoded.decodeTo(request.body(), params, "UTF-8");
            String jsonDefn = params.get("gamedefinition").get(0);
            MudGameDefinitionSerialiser serialiser = new MudGameDefinitionSerialiser();
            MudGameDefinition defn = serialiser.createDefnFromJson(jsonDefn);
            game.resetFrom(defn);
            String message = "The Wizard Reset the Game";
            game.broadcastMessages().wizardBroadcaseMessage(message);
            resultOutput = ResultOutput.getLastActionSuccess(message);
        }


        if(resultOutput==null) {
            resultOutput = ResultOutput.getLastActionError(String.format("Sorry I don't know how to: %s", splatter));
        }

        return FormatResultOutputAsJSendOutput.asString(resultOutput, response);
    }


}
