package uk.co.compendiumdev.restmud.engine.game;

import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationDirectionGate;
import uk.co.compendiumdev.restmud.engine.game.parser.VerbToken;
import uk.co.compendiumdev.restmud.engine.game.playerevents.BroadcastGameMessage;
import uk.co.compendiumdev.restmud.engine.game.playerevents.PlayerEvents;
import uk.co.compendiumdev.restmud.engine.game.scripting.AttributePair;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.VerbHandler;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.VerbLookHandler;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.GetUserDetails;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

import java.util.ArrayList;
import java.util.List;

public class GameCommandProcessor {
    private final MudGame game;
    private final WizardCommands wizardCommands;

    public GameCommandProcessor(final MudGame mudGame) {
        this.game = mudGame;
        this.wizardCommands = new WizardCommands(mudGame);
    }

    public ResultOutput processGetUserDetails(String username) {


        ResultOutput resultOutput;

        GetUserDetails userDetails = game.getUserManager().getUserDetails(username);

        if(userDetails == null){
            resultOutput = new ResultOutput(LastAction.createError(String.format("Could not find user %s", username)));

        }else{

            resultOutput = new ResultOutput(LastAction.createSuccess("Get User Details")).setUserDetails(userDetails);
        }

        return resultOutput;
    }

    public ResultOutput processTheVerbInGame(String username, String verbName, String theNounPhrase, RestMudHttpRequestDetails httpRequestDetails) {
        LastAction lastAction=null;
        ResultOutput resultOutput;

        String nounPhrase = "";

        if(theNounPhrase!=null){
            nounPhrase = theNounPhrase.toLowerCase();
        }

        boolean actionUpdatesTimeStamp;
        boolean ensureLookHappens = false;


        List<BroadcastGameMessage> conditionMessages = new ArrayList<>();
        List<AttributePair> collectablesToAdd = new ArrayList<>();
        List<AttributePair> locationObjectsToAdd= new ArrayList<>();


        MudUser player = game.getUserManager().getUser(username);
        if(player==null){
            return ResultOutput.getLastActionError("Sorry, who are you?");
        }


        // TODO: get the verb, if it is a synonym for another verb then use the synonym

        // process any verb conditions next by matching the verb and seeing if any conditions match
        VerbToken verbToken = game.getUserInputParser().getVerbToken(verbName);

        player.setCurrentCommand(new PlayerCommand(verbName, verbToken, nounPhrase, httpRequestDetails));

        ProcessConditionReturn processed = game.getScriptingEngine().rulesProcessor().processVerbConditions(player, player.getCurrentCommand());

        if(processed.hasAnyMessages()){
            conditionMessages.addAll(processed.messagesAsBroadcaseMessagesList());
        }
        if(processed.hasAnyCollectables()){
            collectablesToAdd.addAll(processed.getCollectables());
        }
        if(processed.hasAnyLocationObjects()){
            locationObjectsToAdd.addAll(processed.getLocationObjects());
        }
        if(processed.isLookForced()){
            ensureLookHappens = true;
        }


        // if there was a last action from the conditions then use that
        if(processed.hasAnyActions()){
            lastAction = processed.getLastAction();
        }



        //userInputParser.getVerbHandler(verbToHandle);
        //verbHandler.doVerb(username, nounphrase)
        //verbHandler.createResultOutput(lastAction, username, nounphrase)

        VerbHandler vh= game.getUserInputParser().getVerbHandler(verbName);

        // if the custom conditions did not create any actions
        if(lastAction==null && vh!=null) {
            // process the last action with generic data
            lastAction = vh.doVerb(player, nounPhrase);
        }


        // TODO: last action should not be null, everything should come throu a verbhandler or condition
        if(lastAction==null) {
            System.out.println(String.format("Warning we did not process the verb noun combo \"%s %s\"", verbName, nounPhrase));
            resultOutput = ResultOutput.getLastActionError(String.format("I don't know how to \"%s %s\" here", verbName, nounPhrase));
        }else{
            // handle game verbs that matched conditions
            resultOutput = new ResultOutput(lastAction);
        }


        // by default
        actionUpdatesTimeStamp = true;

        if(vh!=null) {
            if (vh.shouldAddGameMessages()) {
                resultOutput.addGameMessages(game.broadcastMessages().getMessagesSince(player.getLastActionTimeStamp()));
            }
            if (vh.shouldLookAfterVerb()) {
                ensureLookHappens = true;
            }
            actionUpdatesTimeStamp = vh.actionUpdatesTimeStamp();
        }

        // process any turn conditions now - turn conditions should not create last actions, if they do, they will be ignored
        ProcessConditionReturn processedTurns = game.getScriptingEngine().rulesProcessor().processTurnConditions(player);
        if(processedTurns.hasAnyMessages()){
            conditionMessages.addAll(processedTurns.messagesAsBroadcaseMessagesList());
        }
        if(processedTurns.hasAnyCollectables()){
            collectablesToAdd.addAll(processedTurns.getCollectables());
        }
        if(processedTurns.hasAnyLocationObjects()){
            locationObjectsToAdd.addAll(processedTurns.getLocationObjects());
        }
        if(processed.isLookForced()){
            ensureLookHappens = true;
        }

        if(ensureLookHappens){
            if(!resultOutput.haveLooked()){
                resultOutput.setLook(
                        new VerbLookHandler().setGame(game).usingCurrentVerb("look").
                                doVerb(player,"").getLookReport());
            }
            resultOutput.setGameMessages(game.broadcastMessages().getMessagesSince(player.getLastActionTimeStamp()));
        }

        // add any collectables into the ResultOutput
        if(resultOutput.look != null) {
            resultOutput.look.addAdditionalCollectables(collectablesToAdd);
            resultOutput.look.addAdditionalLocationObjects(locationObjectsToAdd);
        }

        if (actionUpdatesTimeStamp) {

            player.updateLastActionTimeStamp();

            // add local inventory messages
            List<BroadcastGameMessage> extraMessages = PlayerEvents.updateActivePowerItems(player.inventory().itemsView(), player, game.getGameLocations().get(game.JUNK_ROOM));


            resultOutput.addGameMessages(extraMessages);
        }

        // Always see the condition messages
        resultOutput.addGameMessages(conditionMessages);

        return resultOutput;
    }

    public WizardCommands wizardCommands() {
        return wizardCommands;
    }

    // TODO : move to own class and add more guard controls to avoid 500 errors in wiz interface
    public class WizardCommands{

        private final MudGame game;

        public WizardCommands(MudGame game){
            this.game = game;
        }

        public ResultOutput teleportUserTo(String username, String locationId) {

            MudUser user = game.getUserManager().getUser(username);
            user.setLocationId(locationId);

            return new ResultOutput(LastAction.createSuccess("Successfully teleported " + username + " to location " + locationId));

        }

        public ResultOutput lightLocation(String locationId) {
            game.getGameLocations().get(locationId).makeLight();
            return new ResultOutput(LastAction.createSuccess("Successfully Switched on lights in location " + locationId));
        }

        public ResultOutput darkenLocation(String locationId) {
            game.getGameLocations().get(locationId).makeDark();
            return new ResultOutput(LastAction.createSuccess("Successfully Turned Off lights in location " + locationId));
        }

        public ResultOutput closeGate(String gateName) {
            return openCloseGate(false, gateName);        }

        public ResultOutput openGate(String gateName) {
            return openCloseGate(true, gateName);
        }

        private ResultOutput openCloseGate(boolean openit, String gateName) {
            MudLocationDirectionGate gate = game.getGateManager().getGateNamed(gateName);

            if(gate==null){
                return new ResultOutput(LastAction.createError("Could not find gate named " + gateName));
            }

            if(openit) {
                gate.open();
                return new ResultOutput(LastAction.createSuccess("Successfully Opened Gate named " + gateName));
            }else{
                gate.close();
                return new ResultOutput(LastAction.createSuccess("Successfully Closed Gate named " + gateName));
            }
        }


        public ResultOutput broadcast(final String message) {
            game.broadcastMessages().wizardBroadcaseMessage(message);
            return new ResultOutput(LastAction.createSuccess("Wizard Broadcast Message: "+message));
        }
    }
}
