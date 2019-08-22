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

    private GameCommandProcessor commandProcessor;

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
        commandProcessor = new GameCommandProcessor(this);

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


    /************************
     * COMMAND PROCESSING
     */

    public ResultOutput processGetUserDetails(String username) {

        return getCommandProcessor().processGetUserDetails(username);
    }

    public ResultOutput processTheVerbInGame(String username, String verbToHandle, String theNounPhrase, RestMudHttpRequestDetails httpRequestDetails) {
        return getCommandProcessor().processTheVerbInGame(username, verbToHandle, theNounPhrase, httpRequestDetails);
    }

    /***********************************************
     * WIZARD COMMANDS
     **********************************************/

    public ResultOutput teleportUserTo(String username, String locationId) {
        return getCommandProcessor().wizardCommands().teleportUserTo(username, locationId);
    }

    // turn on the lights in the location
    public ResultOutput lightLocation(String locationId) {
        return getCommandProcessor().wizardCommands().lightLocation(locationId);
    }

    public ResultOutput darkenLocation(String locationId) {
        return getCommandProcessor().wizardCommands().darkenLocation(locationId);
    }

    public ResultOutput closeGate(String fromLocation, String toLocation) {
        return getCommandProcessor().wizardCommands().closeGate(fromLocation, toLocation);
    }

    public ResultOutput openGate(String fromLocation, String toLocation) {
        return getCommandProcessor().wizardCommands().openGate(fromLocation, toLocation);
    }

    private GameCommandProcessor getCommandProcessor() {
        return commandProcessor;
    }

    /***********************************************
     * VERB PARSING
     **********************************************/

    public RestMudScriptingEngine scriptingEngine(){
        return scriptingEngine;
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

        broadcastMessages().wizardBroadcaseMessage(getWelcomeMessage());
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


    public RestMudScriptingEngine getScriptingEngine() {
        return scriptingEngine;
    }
}
