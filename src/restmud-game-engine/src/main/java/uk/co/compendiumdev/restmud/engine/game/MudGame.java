package uk.co.compendiumdev.restmud.engine.game;

import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.locations.*;
import uk.co.compendiumdev.restmud.engine.game.parser.BuiltInVerbListBuilder;
import uk.co.compendiumdev.restmud.engine.game.parser.UserInputParser;
import uk.co.compendiumdev.restmud.engine.game.parser.VerbToken;
import uk.co.compendiumdev.restmud.engine.game.playerevents.BroadcastGameMessage;
import uk.co.compendiumdev.restmud.engine.game.playerevents.PlayerEvents;
import uk.co.compendiumdev.restmud.engine.game.scripting.*;
import uk.co.compendiumdev.restmud.engine.game.things.IdGenerator;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;
import uk.co.compendiumdev.restmud.engine.game.verbmodes.VerbMode;
import uk.co.compendiumdev.restmud.engine.game.verbmodes.VerbModes;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.VerbHandler;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.VerbLookHandler;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main class for a 'Game' of RestMud
 */
public class MudGame {


    public static final String JUNK_ROOM = "0"; //every game should have a junk room
    // TODO if the game definition does not have one then add it to the game automatically as an unlinked room

    private final UserManager userManager = new UserManager();

    private Locations gameLocations = new Locations();
    private Collectables gameCollectables = new Collectables();
    private final SecretGameCode secretGameRegistrationCode = new SecretGameCode();

    private BroadcastMessages broadcastMessages = new BroadcastMessages();


    private int totalScore=0;
    private PlayerGuiMode playerGuiMode=PlayerGuiMode.SUPER_EASY;
    private VerbModes verbModes;
    private VerbMode verbMode;



    private MudGameGateCollection gateManager = new MudGameGateCollection();

    private InventoryLocationObjects locationObjects = new InventoryLocationObjects();




    private final IdGenerator theIdGenerator = new IdGenerator();
    private Map<Integer, List<VerbCondition>> verbConditions = new HashMap<>();
    private List<PriorityTurnCondition> turnConditions = new ArrayList<>();
    private List<VerbRegexToVerbMatch> localVerbs=new ArrayList<>();
    private List<ScriptClause> startupCommands = new ArrayList<>();
    private String startLocationId;

    private final RestMudScriptingEngine scriptingEngine;
    private final UserInputParser userInputParser;
    private MudGameDefinition gameDefinition;
    private Directions directions;


    /*
        TODO: Suspect that Game and Game State should be managed as seperate classes
           - game state has all locations etc.
           - game engine has all the parsing, verb handling etc.
           - Game might have GameState and GameEngine

     */

    public MudGame(){


        clearGame();

        scriptingEngine = new RestMudScriptingEngine(this);
        userInputParser = new UserInputParser(new BuiltInVerbListBuilder(this).addBuiltInVerbs().getVerbList());


    }

    public String getGameName(){
        return this.gameDefinition.getGameName();
    }

    public String getWelcomeMessage(){
        return this.gameDefinition.getWelcomeMessage();
    }

    private void clearGame() {

        gameLocations = new Locations();
        gameCollectables = new Collectables();
        broadcastMessages = new BroadcastMessages();
        gateManager = new MudGameGateCollection();
        locationObjects = new InventoryLocationObjects();

        verbConditions = new HashMap<>();
        turnConditions = new ArrayList<>();
        localVerbs=new ArrayList<>();
        startupCommands = new ArrayList<>();
        directions = new Directions();


    }

    public UserInputParser getUserInputParser(){
        return userInputParser;
    }

    public void addCollectable(MudCollectable collectable, MudLocation location) {
        getGameCollectables().add(collectable);
        location.collectables().addItem(collectable);
    }



    public UserManager getUserManager(){
        return userManager;
    }



    /***********************************************
     * MAIN VERB DEFINITIONS
     ***********************************************/


    public IdGenerator idGenerator() {
        return theIdGenerator;
    }

    public List<IdDescriptionPair> getGatesAsIdDescriptionPairs() {
        return gateManager.getGatesAsIdDescriptionPairs();
    }

    private LookResultOutput userLook(MudUser player) {

        LookResultOutput output = new LookResultOutput();

        if(player==null){
            return output;
        }

        MudLocation whereAmI = getGameLocations().get(player.getLocationId());


        output = VerbLookHandler.look(player, whereAmI,
                                        gateManager.getGatesHere(whereAmI),
                                        userManager.getUsers());

        return output;

    }



    public ResultOutput processGetUserDetails(String username) {


        ResultOutput resultOutput;

        GetUserDetails userDetails = userManager.getUserDetails(username);

        if(userDetails == null){
            resultOutput = new ResultOutput(LastAction.createError(String.format("Could not find user %s", username)));

        }else{

            resultOutput = new ResultOutput(LastAction.createSuccess("Get User Details")).setUserDetails(userDetails);
        }

        return resultOutput;
    }

    /***********************************************
     * VERB PARSING
     **********************************************/

    public RestMudScriptingEngine scriptingEngine(){
        return scriptingEngine;
    }

    public ResultOutput processTheVerbInGame(String username, String verbToHandle, String theNounPhrase, RestMudHttpRequestDetails httpRequestDetails) {
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


        MudUser player = userManager.getUser(username);
        if(player==null){
            return ResultOutput.getLastActionError("Sorry, who are you?");
        }



        // process any verb conditions next
        VerbToken verbToken = userInputParser.getVerbToken(verbToHandle);

        player.setCurrentCommand(new PlayerCommand(verbToHandle, verbToken, nounPhrase, httpRequestDetails));

        ProcessConditionReturn processed = scriptingEngine.rulesProcessor().processVerbConditions(player, player.getCurrentCommand());

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

        VerbHandler vh=userInputParser.getVerbHandler(verbToHandle);

        if(lastAction==null) {

            // process the last action with generic data
            if(vh!=null) {
                lastAction = vh.doVerb(player, nounPhrase);
            }

        }


        // last action might be null if it is a custom Verb because the default verb handler takes no action
        // and sometimes custom verb rules don't issue a last action (but stuff might happen as a side-effect)
        if(lastAction==null) {
            resultOutput = ResultOutput.getLastActionError(String.format("I don't know how to \"%s %s\" here", verbToHandle, nounPhrase));
        }else{
            // handle game verbs that matched conditions
            resultOutput = new ResultOutput(lastAction);
        }


        // by default
        actionUpdatesTimeStamp = true;
        
        if(vh!=null) {
            if (vh.shouldAddGameMessages()) {
                resultOutput.addGameMessages(broadcastMessages().getMessagesSince(player.getLastActionTimeStamp()));
            }
            if (vh.shouldLookAfterVerb()) {
                resultOutput.setLook(userLook(player));
                ensureLookHappens = false;
            }

            actionUpdatesTimeStamp = vh.actionUpdatesTimeStamp();
        }

        // process any turn conditions now - turn conditions should not create last actions, if they do, they will be ignored
        ProcessConditionReturn processedTurns = scriptingEngine.rulesProcessor().processTurnConditions(player);
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
            resultOutput.setLook(userLook(player));
            resultOutput.setGameMessages(broadcastMessages().getMessagesSince(player.getLastActionTimeStamp()));
        }

        // add any collectables into the ResultOutput
        if(resultOutput.look != null) {
            resultOutput.look.addAdditionalCollectables(collectablesToAdd);
            resultOutput.look.addAdditionalLocationObjects(locationObjectsToAdd);
        }

        if (actionUpdatesTimeStamp) {

                player.updateLastActionTimeStamp();

                // add local inventory messages
            List<BroadcastGameMessage> extraMessages = PlayerEvents.updateActivePowerItems(player.inventory().itemsView(), player, getGameLocations().get(JUNK_ROOM));


                resultOutput.addGameMessages(extraMessages);
        }

        // Always see the condition messages
        resultOutput.addGameMessages(conditionMessages);

        return resultOutput;
    }

 





    public List<VerbRegexToVerbMatch> getLocalVerbs(){
        return this.localVerbs;
    }




    /***********************************************
     * SCORES
     **********************************************/


    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalScore() {
        return totalScore;
    }




    /***********************************************
     * GUI MODES
     **********************************************/

    public PlayerGuiMode getGuiMode(String username) {
        MudUser player = userManager.getUser(username);

        // compare the player gui mode with game gui mode - use whichever is lower
        if(player!=null && player.getGuiMode().getGuiWeighting() < this.playerGuiMode.getGuiWeighting()){
            return player.getGuiMode();
        }

        return this.playerGuiMode;
    }

    public PlayerGuiMode getGuiMode() {
        return this.playerGuiMode;
    }

    public ResultOutput setGameGuiMode(String gameGuiMode) {

        this.playerGuiMode = PlayerGuiMode.getMode(gameGuiMode);

        return new ResultOutput(
                        LastAction.createSuccess(
                                        String.format("Game GUI Mode set to : %s",
                                                      this.playerGuiMode.templateGuiModeString())));

    }


    /***********************************************
     * WIZARD BROADCAST MESSAGES
     **********************************************/

    public BroadcastMessages broadcastMessages(){
        return broadcastMessages;
    }

    public void wizardBroadcastMessage(String message) {
        broadcastMessages().add(String.format("The Wizard Says: %s", message));
    }





    /***********************************************
     * WIZARD COMMANDS
     **********************************************/

    public ResultOutput teleportUserTo(String username, String locationId) {

        MudUser user = userManager.getUser(username);
        user.setLocationId(locationId);

        return new ResultOutput(LastAction.createSuccess("Successfully teleported " + username + " to location " + locationId));

    }

    // turn on the lights in the location
    public ResultOutput lightLocation(String locationId) {
        getGameLocations().get(locationId).makeLight();
        return new ResultOutput(LastAction.createSuccess("Successfully Switched on lights in location " + locationId));
    }

    public ResultOutput darkenLocation(String locationId) {
        getGameLocations().get(locationId).makeDark();
        return new ResultOutput(LastAction.createSuccess("Successfully Turned Off lights in location " + locationId));
    }

    public ResultOutput closeGate(String fromLocation, String toLocation) {

        MudLocationDirectionGate gate = gateManager.getGateBetween(getGameLocations().get(fromLocation), getGameLocations().get(toLocation));
        gate.close();

        return new ResultOutput(LastAction.createSuccess("Successfully Closed Gate from " + fromLocation + " to " + toLocation));
    }

    public ResultOutput openGate(String fromLocation, String toLocation) {

        MudLocationDirectionGate gate = gateManager.getGateBetween(getGameLocations().get(fromLocation), getGameLocations().get(toLocation));
        gate.open();

        return new ResultOutput(LastAction.createSuccess("Successfully Opened Gate from " + fromLocation + " to " + toLocation));
    }


    public void ensureGameHasJunkRoom() {
        MudLocation junkRoom = getGameLocations().get("0");
        if(junkRoom==null){
            getGameLocations().addLocation(new MudLocation("0", "The Junk Room", "This is the Junk Room, where Junk is dumped.", ""));
        }
    }

    public Map<Integer, List<VerbCondition>> getVerbConditions() {
        return verbConditions;
    }

    public MudGameGateCollection getGateManager() {
        return gateManager;
    }

    public List<PriorityTurnCondition> getPriorityTurnConditions() {
        return turnConditions;
    }

    public void setStartLocationId(String startLocation) {
        this.startLocationId = startLocation;
    }

    public String getStartLocationId() {
        return startLocationId;
    }



    /**
     * given a collectable, move it to location, ensure that it only exists in this location
     * @param collectable
     * @param location
     */
    public boolean moveCollectableToLocation(MudCollectable collectable, MudLocation location) {

        // remove the collectable from any other location
        for(MudLocation otherLocation : getGameLocations().getLocations()){
            if(otherLocation.collectables().contains(collectable)){
                otherLocation.collectables().removeItem(collectable);
                // TODO: create event saying it zapped from the room for any players in the room
            }
        }

        // remove the collectable from any player inventories
        for(MudUser user : userManager.getUsers()){
            if(user.inventory().contains(collectable)){
                user.inventory().removeItem(collectable);
                // TODO: create event for the player saying it has vanished
            }
        }

        return location.collectables().addItem(collectable);
    }


    public void tokenizeScriptConditions() {

        for(List<VerbCondition> verbConditionList : verbConditions.values()){

            for(VerbCondition verbCondition : verbConditionList) {

                // ensure all conditions have been tokenized for processing - also raises any errors in command names
                scriptingEngine().thenClauseTokenizer().tokenize(verbCondition.thenClauses());
                scriptingEngine().whenClauseTokenizer().tokenize(verbCondition.whenClauses());

            }
        }

        for(PriorityTurnCondition turnConditionList : turnConditions){
                scriptingEngine().thenClauseTokenizer().tokenize(turnConditionList.thenClauses());
                scriptingEngine().whenClauseTokenizer().tokenize(turnConditionList.whenClauses());
        }
    }


    public Locations getGameLocations() {
        return gameLocations;
    }

    public Collectables getGameCollectables() {
        return gameCollectables;
    }

    public SecretGameCode getSecretGameRegistrationCode() {
        return secretGameRegistrationCode;
    }

    public InventoryLocationObjects getLocationObjects() {
        return locationObjects;
    }



    /***********************************************
     * TODO: Verb Modes should not be managed in the game, these are for API and GUI to manage
     *
     * VERB MODES
     **********************************************/

    public VerbModes getVerbModes() {
        return verbModes;
    }

    public String getVerbModeName() {
        return this.verbMode.getName();
    }

    public VerbMode getVerbMode() {
        return this.verbMode;
    }

    public void setVerbModes(VerbModes verbModes) {
        this.verbModes = verbModes;
        setVerbMode("default");
    }

    public void setVerbMode(String verbModeName) {
        verbMode = this.verbModes.get(verbModeName);
    }

    public ResultOutput setVerbModeViaApi(String verbModeName) {

        if(verbModeName == null){
            return new ResultOutput(LastAction.createError("Verb Mode can not be null"));
        }

        verbMode = this.verbModes.get(verbModeName);

        if(verbMode == null){
            return new ResultOutput(LastAction.createError(String.format("I do not recognise the Verb Mode: %s",verbModeName)));
        }

        setVerbMode(verbMode.getName());

        return new ResultOutput(LastAction.createSuccess(String.format("Verb mode set to %s", verbModeName)));
    }

    public MudGameDefinition gameDefinition() {
        if(this.gameDefinition==null) {
            this.gameDefinition=MudGameDefinition.create(userInputParser);
        }

        return this.gameDefinition;
    }

    public void initFromDefinition(MudGameDefinition defn) {

        // store definition in case need to use it again to reset game
        this.gameDefinition=defn;

        resetGame();



    }

    public void resetGame() {

        // need to clear down any existing things first when reset game
        clearGame();

        MudGameDefinition defn = this.gameDefinition;

        // clone all the directions
        directions = defn.getClonedCopiedDirections();

        // clone all the collectables
        gameCollectables.setFrom(defn.getCollectables().getClonedCopiedCollectables());

        // clone all the location objects
        locationObjects.setFrom(defn.getLocationObjects().getClonedCopiedLocations());

        // Add clones of the objects to the locations
        // to allow instantiation of multiple games from the same definition
        for(MudLocation definedLocation : defn.gameLocations().getLocations()){

            MudLocation gameLocation = definedLocation.getClonedCopy();
            gameLocations.addLocation(gameLocation);

            // now add the objects from the defined location to the game location
            for(MudLocationObject definedLocationObject : definedLocation.objects().itemsView()){
                gameLocation.objects().addItem(locationObjects.get(definedLocationObject.getObjectId()));
            }

            // now add the collectables in the defined location to the game location collectables
            for(MudCollectable definedLocationCollectable : definedLocation.collectables().itemsView()){
                gameLocation.collectables().addItem(gameCollectables.get(definedLocationCollectable.getCollectableId()));
            }
        }

        // cloned copy of the verbConditions not the actual set
        verbConditions.putAll(defn.getClonedVerbConditions());

        // cloned copy of the turnConditions not the actual full set
        turnConditions.addAll(defn.getClonedPriorityTurnConditions());

        // Clone the gates by creating new ones from the definition
        for(MudLocationDirectionGate gate : defn.getGates().values()) {

            MudLocation fromLocation = gameLocations.get(gate.getFromLocationId());
            MudLocation toLocation = gameLocations.get(gate.getToLocationId());

            MudLocationDirectionGate newgate = gate.createCopy(fromLocation, toLocation);
            newgate.gateIsNamed(gate.getGateName());

            gateManager.addGate(newgate);
        }

        // clone the verbs
        localVerbs.addAll(defn.getLocalVerbs());
        userInputParser.addVerbs(defn.getLocalVerbs());

        this.setStartLocationId(defn.getStartLocationId());

        idGenerator().setFrom(defn.getIdGenerator());
        totalScore = defn.getTotalScore();


        // reset the users
        for(MudUser user : getUserManager().getUsers()){
            user.reset();
            user.setLocationId(this.getStartLocationId());
        }

        tokenizeScriptConditions();

        startupCommands.addAll(defn.getClonedStartupRules());
        scriptingEngine.thenClauseCommandList().getTokenizer().tokenize(startupCommands);
        runStartupCommands();

        wizardBroadcastMessage(getWelcomeMessage());
    }

    private void runStartupCommands() {
        for(ScriptClause command : startupCommands){
            scriptingEngine.thenClauseCommandList().getCommand(command.getToken()).execute(command,userManager.getUser("wiz"));
        }
    }

    public void resetFrom(MudGameDefinition defn) {
        this.gameDefinition = defn;
        resetGame();
    }

    public Directions getDirections() {
        return directions;
    }




    /*
            CODE THAT WAS MOVED TO GAME DEFINITION
     */
    /*

     **********************************************
     * GATE CREATION
     **********************************************/

//    public MudLocationDirectionGate addGate(String locationID, String fromDirection, GateDirection whichWayDirection, GateStatus startingGateStatus) {
//        String CALCULATE_TO_DESTINATION="";
//
//        return addGate(theIdGenerator.generateId("gate"), locationID, fromDirection, CALCULATE_TO_DESTINATION, whichWayDirection, startingGateStatus);
//    }
//
//    public MudLocationDirectionGate addGate(String gateName, String locationID, String fromDirection, String theTooLocationID, GateDirection whichWayDirection, GateStatus startingGateStatus) {
//
//        // find the from location
//        MudLocation fromLocation = getGameLocations().get(locationID);
//
//        String toLocationId="";
//        String toLocationDirection="";
//
//        if(theTooLocationID==null || theTooLocationID.length()==0) {
//            // find the to Location
//            toLocationId = fromLocation.destinationFor(fromDirection);
//            MudLocation toLocation = getGameLocations().get(toLocationId);
//            toLocationDirection = toLocation.directionWhichLeadsTo(fromLocation.getLocationId());
//
//        }else{
//            toLocationId = theTooLocationID;
//        }
//        MudLocation toLocation = getGameLocations().get(toLocationId);
//
//
//        // create a gate for the game that sits between those locations
//        MudLocationDirectionGate gate = new MudLocationDirectionGate(fromLocation, fromDirection, toLocation, toLocationDirection, whichWayDirection, startingGateStatus);
//        gate.gateIsNamed(gateName);
//
//        gateManager.addGate(gate);
//
//        return gate;
//
//        //add the gate to the location
//
//        // if BOTH_WAYS then find the to location and add a gate to that location
//
//
//    }
//
//    public void addLocationObjectIn(MudLocationObject mudLocationObject, String toLocation) {
//        getLocationObjects().addItem(mudLocationObject);
//        getGameLocations().get(toLocation).objects().addItem(mudLocationObject);
//    }
//
//    public void addLocationObjectTo(MudLocationObject mudLocationObject, MudLocation toLocation) {
//        getLocationObjects().addItem(mudLocationObject);
//        toLocation.objects().addItem(mudLocationObject);
//    }

//
//    public void addCondition(VerbCondition verbCondition) {
//
//        VerbToken verbToken = userInputParser.getVerbToken(verbCondition.getVerbName());
//        if(verbToken==null){
//            throw new RuntimeException(String.format("Could not find verb [%s] mentioned in verb condition, have you added it?", verbCondition.getVerbName()));
//        }
//
//        List<VerbCondition>conditions = this.verbConditions.get(verbToken.getValue());
//        if(conditions==null){
//            conditions = new ArrayList<VerbCondition>();
//            this.verbConditions.put(verbToken.getValue(), conditions);
//        }
//        conditions.add(verbCondition);
//
//    }
//
//    public void addCondition(PriorityTurnCondition turnCondition) {
//        this.turnConditions.add(turnCondition);
//    }
//    // allow a game to have 'verbs' that are implemented via VerbConditions which are not part of the main game
//    public void addVerb(String verbName) {
//        // add a get matcher
//        this.localVerbs.add(new VerbRegexToVerbMatch(verbName+"\\/\\S*", verbName));
//        // add a post matcher
//        this.localVerbs.add(new VerbRegexToVerbMatch(verbName, verbName));
//        userInputParser.addVerb(verbName);
//    }


}
