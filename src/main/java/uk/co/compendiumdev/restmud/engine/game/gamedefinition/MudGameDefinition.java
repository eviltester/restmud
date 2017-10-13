package uk.co.compendiumdev.restmud.engine.game.gamedefinition;

import uk.co.compendiumdev.restmud.engine.game.Collectables;
import uk.co.compendiumdev.restmud.engine.game.InventoryLocationObjects;
import uk.co.compendiumdev.restmud.engine.game.Locations;
import uk.co.compendiumdev.restmud.engine.game.VerbRegexToVerbMatch;
import uk.co.compendiumdev.restmud.engine.game.locations.*;
import uk.co.compendiumdev.restmud.engine.game.parser.UserInputParser;
import uk.co.compendiumdev.restmud.engine.game.parser.VerbToken;
import uk.co.compendiumdev.restmud.engine.game.scripting.PriorityTurnCondition;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.VerbCondition;
import uk.co.compendiumdev.restmud.engine.game.things.IdGenerator;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to define a game, to allow definitions to be saved, serialised and loaded on the fly.
 *
 * A 'MudGame' maintains state of a game, and is initialised with a definition.
 */
public class MudGameDefinition {

    private final Locations gameLocations = new Locations();
    private Map<Integer, List<VerbCondition>> verbConditions = new HashMap<>();
    private List<PriorityTurnCondition> turnConditions = new ArrayList<>();
    private final InventoryLocationObjects locationObjects = new InventoryLocationObjects();
    private final Collectables gameCollectables = new Collectables();

    private transient UserInputParser userInputParser;
    private String startLocationId;
    private IdGenerator theIdGenerator = new IdGenerator();
    private MudGameGateCollection gateManager = new MudGameGateCollection();
    private List<VerbRegexToVerbMatch> localVerbs=new ArrayList<>();
    private int totalScore;
    private String gameName="Unnamed Game";
    private String welcomeMessage="Welcome To The Game";
    private List<ScriptClause> startupRules= new ArrayList<>();


    public MudGameDefinition() {
        this.startupRules= new ArrayList<>();
    }

    public MudGameDefinition(UserInputParser userInputParser) {
        this();
        this.userInputParser = userInputParser;
    }

    public static MudGameDefinition create(UserInputParser userInputParser) {
        return new MudGameDefinition(userInputParser);

    }

    public void setGameName(String aName){
        gameName = aName;
    }

    public void setWelcomeMessage(String message){
        welcomeMessage = message;
    }

    public Locations gameLocations() {
        return this.gameLocations;
    }

    public void addCondition(VerbCondition verbCondition) {

        VerbToken verbToken = userInputParser.getVerbToken(verbCondition.getVerbName());
        if (verbToken == null) {
            throw new RuntimeException(String.format("Could not find verb [%s] mentioned in verb condition, have you added it?", verbCondition.getVerbName()));
        }

        List<VerbCondition> conditions = this.verbConditions.get(verbToken.getValue());
        if (conditions == null) {
            conditions = new ArrayList<VerbCondition>();
            this.verbConditions.put(verbToken.getValue(), conditions);
        }
        conditions.add(verbCondition);

    }

    public Map<Integer, List<VerbCondition>> getClonedVerbConditions() {
        Map<Integer, List<VerbCondition>> clonedVerbConditions = new HashMap<>();
        for(Integer key : this.verbConditions.keySet()){

            List<VerbCondition> newList = new ArrayList<>();
            clonedVerbConditions.put(key.intValue(), newList);

            for(VerbCondition cond : this.verbConditions.get(key)){
                newList.add(cond.getClonedCopy());
            }

        }
        return clonedVerbConditions;
    }

    public void setStartLocationId(String startLocationId) {
        this.startLocationId = startLocationId;
    }

    public String getStartLocationId() {
        return startLocationId;
    }

    public void addLocationObjectIn(MudLocationObject mudLocationObject, String toLocation) {
        locationObjects.addItem(mudLocationObject);
        gameLocations.get(toLocation).objects().addItem(mudLocationObject);
    }

    /***********************************************
     * GATE CREATION
     **********************************************/

    public MudLocationDirectionGate addGate(String locationID, String fromDirection, GateDirection whichWayDirection, GateStatus startingGateStatus) {
        String CALCULATE_TO_DESTINATION="";

        return addGate(theIdGenerator.generateId("gate"), locationID, fromDirection, CALCULATE_TO_DESTINATION, whichWayDirection, startingGateStatus);
    }

    public MudLocationDirectionGate addGate(String gateName, String locationID, String fromDirection, String theTooLocationID, GateDirection whichWayDirection, GateStatus startingGateStatus) {

        // find the from location
        MudLocation fromLocation = gameLocations().get(locationID);

        String toLocationId;
        String toLocationDirection="";

        if(theTooLocationID==null || theTooLocationID.length()==0) {
            // find the to Location
            toLocationId = fromLocation.destinationFor(fromDirection);
            MudLocation toLocation = gameLocations().get(toLocationId);
            toLocationDirection = toLocation.directionWhichLeadsTo(fromLocation.getLocationId());

        }else{
            toLocationId = theTooLocationID;
        }
        MudLocation toLocation = gameLocations().get(toLocationId);


        // create a gate for the game that sits between those locations
        //MudLocationDirectionGate gate = new MudLocationDirectionGate(fromLocation, fromDirection, toLocation, toLocationDirection, whichWayDirection, startingGateStatus);
        // for definitions we don't store object references, just the ids
        MudLocationDirectionGate gate = new MudLocationDirectionGate(fromLocation.getLocationId(), fromDirection, toLocation.getLocationId(), toLocationDirection, whichWayDirection, startingGateStatus);
        gate.gateIsNamed(gateName);

        gateManager.addGate(gate);

        return gate;

        //add the gate to the location

        // if BOTH_WAYS then find the to location and add a gate to that location


    }

    public IdGenerator getIdGenerator() {
        return this.theIdGenerator;
    }

    public Map<String, MudLocationDirectionGate> getGates() {
        return gateManager.getGates();
    }

    public void addCondition(PriorityTurnCondition turnCondition) {
        this.turnConditions.add(turnCondition);
    }


    public List<PriorityTurnCondition> getClonedPriorityTurnConditions() {
        List<PriorityTurnCondition> clonedPriorityTurnConditions = new ArrayList<>();
        for(PriorityTurnCondition cond : turnConditions){
            clonedPriorityTurnConditions.add(cond.getClonedCopy());
        }

        return clonedPriorityTurnConditions;
    }

    // allow a game to have 'verbs' that are implemented via VerbConditions which are not part of the main game
    public void addVerb(String verbName) {
        // add a get matcher
        this.localVerbs.add(new VerbRegexToVerbMatch(verbName+"\\/\\S*", verbName));
        // add a post matcher
        this.localVerbs.add(new VerbRegexToVerbMatch(verbName, verbName));
        userInputParser.addVerb(verbName);
    }

    public List<VerbRegexToVerbMatch> getLocalVerbs() {
        return localVerbs;
    }

    public List<VerbRegexToVerbMatch> getClonedCopyLocalVerbs() {
        List<VerbRegexToVerbMatch> clonedLocalVerbs=new ArrayList<>();
        for(VerbRegexToVerbMatch aVerb : localVerbs){
            clonedLocalVerbs.add(aVerb.getClonedCopy());
        }
        return clonedLocalVerbs;
    }

    public void addCollectable(MudCollectable collectable, MudLocation location) {
        gameCollectables.add(collectable);
        location.collectables().addItem(collectable);
    }

    public Collectables getCollectables() {
        return gameCollectables;
    }

    public InventoryLocationObjects getLocationObjects() {
        return locationObjects;
    }

    public IdGenerator idGenerator() {
        return theIdGenerator;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public MudGameEntityCreator creator() {
        return new GameDefinitionEntityCreator(this);
    }


    public String getGameName() {
        return gameName;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public MudGameDefinition addStartupRule(String rule, String argument) {
        this.startupRules.add(new ScriptClause(rule, argument));
        return this;
    }

    public List<ScriptClause> getClonedStartupRules() {
        List<ScriptClause> clonedStartupRules = new ArrayList<>();
        for(ScriptClause cond : this.startupRules){
            clonedStartupRules.add(cond.createCloneCopy());
        }

        return clonedStartupRules;
    }
}
