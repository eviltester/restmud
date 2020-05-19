package uk.co.compendiumdev.restmud.engine.game;

import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationDirectionGate;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;

public class GameResetter {
    private final MudGame game;

    public GameResetter(final MudGame game) {
        this.game = game;
    }

    public void resetUsing(final MudGameDefinition defn) {

        // need to clear down any existing things first when reset game
        game.clearGame();

        // clone all the directions
        game.setDirections(defn.getClonedCopiedDirections());

        // clone all the collectables
        game.getGameCollectables().setFrom(defn.getCollectables().getClonedCopiedCollectables());

        // clone all the location objects
        game.getLocationObjects().setFrom(defn.getLocationObjects().getClonedCopiedLocations());

        // Add clones of the objects to the locations
        // to allow instantiation of multiple games from the same definition
        for(MudLocation definedLocation : defn.gameLocations().getLocations()){

            MudLocation gameLocation = definedLocation.getClonedCopy();
            game.getGameLocations().addLocation(gameLocation);

            // now add the objects from the defined location to the game location
            for(MudLocationObject definedLocationObject : definedLocation.objects().itemsView()){
                gameLocation.objects().addItem(game.getLocationObjects().get(definedLocationObject.getObjectId()));
            }

            // now add the collectables in the defined location to the game location collectables
            for(MudCollectable definedLocationCollectable : definedLocation.collectables().itemsView()){
                gameLocation.collectables().addItem(game.getGameCollectables().get(definedLocationCollectable.getCollectableId()));
            }
        }

        // cloned copy of the verbConditions not the actual set
        game.getVerbConditions().putAll(defn.getClonedVerbConditions());

        // cloned copy of the turnConditions not the actual full set
        game.getTurnConditions().addAll(defn.getClonedPriorityTurnConditions());

        // Clone the gates by creating new ones from the definition
        for(MudLocationDirectionGate gate : defn.getGates().values()) {
                MudLocationDirectionGate newgate = gate.createCopy();
                game.getGateManager().addGate(newgate);
        }

        // clone the verbs
        game.getLocalVerbs().addAll(defn.getLocalVerbs());
        game.getUserInputParser().addVerbs(defn.getLocalVerbs());

        game.setStartLocationId(defn.getStartLocationId());

        game.idGenerator().setFrom(defn.getIdGenerator());
        game.setTotalScore(defn.getTotalScore());


        // reset the users
        for(MudUser user : game.getUserManager().getUsers()){
            user.reset();
            user.setLocationId(game.getStartLocationId());
        }

        game.tokenizeScriptConditions();

        game.getStartupCommands().addAll(defn.getClonedStartupRules());
        game.getScriptingEngine().thenClauseCommandList().getTokenizer().tokenize(game.getStartupCommands());

        runStartupCommands();

        game.broadcastMessages().wizardBroadcaseMessage(defn.getWelcomeMessage());
    }

    private void runStartupCommands() {
        for(ScriptClause command : game.getStartupCommands()){
            game.getScriptingEngine().thenClauseCommandList().getCommand(command.getToken()).execute(command,game.getUserManager().getUser("wiz"));
        }
    }
}
