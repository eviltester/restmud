package uk.co.compendiumdev.restmud.engine.game;

import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.locations.*;
import uk.co.compendiumdev.restmud.engine.game.parser.BuiltInVerbListBuilder;
import uk.co.compendiumdev.restmud.engine.game.parser.UserInputParser;
import uk.co.compendiumdev.restmud.engine.game.scripting.*;
import uk.co.compendiumdev.restmud.engine.game.things.IdGenerator;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;
import uk.co.compendiumdev.restmud.engine.game.verbmodes.VerbMode;
import uk.co.compendiumdev.restmud.engine.game.verbmodes.VerbModes;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.*;

import java.util.*;

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

    private final GameCommandProcessor commandProcessor;

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

    public void clearGame() {

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


    public GameCommandProcessor getCommandProcessor() {
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

    public Directions getDirections() {
        return directions;
    }

    public RestMudScriptingEngine getScriptingEngine() {
        return scriptingEngine;
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

    private void resetter(MudGame game, MudGameDefinition defn){
        new GameResetter(game).resetUsing(defn);
    }

    public void resetGame() {
        resetter(this, this.gameDefinition);
    }

    public void resetFrom(MudGameDefinition defn) {
        this.gameDefinition = defn;
        resetGame();
    }


    public void setDirections(final Directions clonedCopiedDirections) {
        directions = clonedCopiedDirections;
    }

    public Collection<PriorityTurnCondition> getTurnConditions() {
        return turnConditions;
    }

    public List<ScriptClause> getStartupCommands() {
        return startupCommands;
    }
}
