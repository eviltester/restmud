package uk.co.compendiumdev.restmud.engine.game.gamedefinition;

import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.scripting.PriorityTurnCondition;
import uk.co.compendiumdev.restmud.engine.game.scripting.VerbCondition;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;

/**
 * Creates entities which are automatically added to the game
 * so we don't create something and then forget to add it to the game
 */
public class GameDefinitionEntityCreator implements MudGameEntityCreator {
    private final MudGameDefinition defn;

    public GameDefinitionEntityCreator(MudGameDefinition defn) {
        this.defn = defn;
    }

    public MudLocation location(String locationId, String locationName, String locationDescription, String exits) {
        MudLocation location = new MudLocation(locationId.toLowerCase(), locationName, locationDescription, exits);
        defn.gameLocations().addLocation(location);
        return location;
    }

    public MudCollectable collectable(String collectableName, String description, String locationId) {
        MudCollectable collectable = new MudCollectable(collectableName.toLowerCase(), description);
        defn.addCollectable(collectable, defn.gameLocations().get(locationId.toLowerCase()));
        return collectable;
    }

    public MudLocationObject locationObject(String objectId, String objectDescription, String examineDescription, String locationId) {
        MudLocationObject obj = new MudLocationObject(objectId.toLowerCase(), objectDescription, examineDescription);
        defn.addLocationObjectIn(obj,locationId);
        return obj;
    }

    public VerbCondition verbCondition(String verb) {
        VerbCondition cond = new VerbCondition(verb);
        defn.addCondition(cond);     // remember to add the condition to the game
        return cond;

    }

    public PriorityTurnCondition priorityTurnCondition() {
        PriorityTurnCondition turn = new PriorityTurnCondition();
        defn.addCondition(turn);
        return turn;

    }

    public MudLocationObject dispenser(String locationId, String dispenserId, String dispenserDescription, String examineDescription) {
        MudLocationObject locationObj = new MudLocationObject(dispenserId.toLowerCase(),
                dispenserDescription,
                examineDescription);
        locationObj.setIsADispenser(true);

        defn.addLocationObjectIn(locationObj, locationId);
        return locationObj;
    }

}
