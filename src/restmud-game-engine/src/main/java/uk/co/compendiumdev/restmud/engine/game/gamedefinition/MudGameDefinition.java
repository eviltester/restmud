package uk.co.compendiumdev.restmud.engine.game.gamedefinition;

import uk.co.compendiumdev.restmud.engine.game.Collectables;
import uk.co.compendiumdev.restmud.engine.game.InventoryLocationObjects;
import uk.co.compendiumdev.restmud.engine.game.Locations;
import uk.co.compendiumdev.restmud.engine.game.VerbRegexToVerbMatch;
import uk.co.compendiumdev.restmud.engine.game.locations.*;
import uk.co.compendiumdev.restmud.engine.game.parser.UserInputParser;
import uk.co.compendiumdev.restmud.engine.game.parser.Verb;
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

    private final Directions directions = new Directions();
    private final Locations gameLocations = new Locations();
    private Map<Integer, List<VerbCondition>> verbConditions = new HashMap<>();
    private List<PriorityTurnCondition> turnConditions = new ArrayList<>();
    private final InventoryLocationObjects locationObjects = new InventoryLocationObjects();
    private final Collectables gameCollectables = new Collectables();

    // transient so not serialised
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

    public void setUserInputParser(UserInputParser userInputParser) {
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
            // do not force user to use adVerb directly, allow creation when adding a condition
            addVerb(verbCondition.getVerbName());
            verbToken = userInputParser.getVerbToken(verbCondition.getVerbName());
        }

        // handle synonyms as well
        for(String synonym : verbCondition.getSynonyms()){
            Verb verb = userInputParser.getVerb(synonym);
            if(verb==null){
                addVerb(synonym);   // we don't add it as a synonym because this is rule specific
                                    // not universal for the game, but we do add it as a verb
            }
        }


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

    /**
     * gate should only need a name and start status to create, and then add extra information
     * @param gatename
     * @param initialStatus
     * @return
     */
    public MudLocationDirectionGate addGate(final String gatename, final GateStatus initialStatus) {
        MudLocationDirectionGate gate = new MudLocationDirectionGate(gatename, initialStatus);
        gateManager.addGate(gate);
        return gate;
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

        // todo: allow creating generic messages if verb used but doesn't match a condition
        // todo: allow creating properties on objects e.g.
        //      define an object as a hat with property wearable
        //      when wear objectHasProperty 'wearable', setObjectProperty 'worn'
        // todo: allow look conditions for when inventory items have specific properties
        //      e.g. look if
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

    public Directions getClonedCopiedDirections() {
        return directions.createClonedCopy();
    }


}
