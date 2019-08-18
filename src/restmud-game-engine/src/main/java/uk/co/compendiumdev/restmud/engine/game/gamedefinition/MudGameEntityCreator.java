package uk.co.compendiumdev.restmud.engine.game.gamedefinition;

import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.scripting.PriorityTurnCondition;
import uk.co.compendiumdev.restmud.engine.game.scripting.VerbCondition;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;

/**
 * An interface to allow creation of test DSLs or other classes which can create games
 */
public interface MudGameEntityCreator {
    MudLocation location(String locationId, String locationName, String locationDescription, String exits);

    MudCollectable collectable(String collectableName, String description, String locationId);

    MudLocationObject locationObject(String objectId, String objectDescription, String examineDescription, String locationId);

    VerbCondition verbCondition(String verb);

    PriorityTurnCondition priorityTurnCondition();

    MudLocationObject dispenser(String locationId, String dispenserId, String dispenserDescription, String examineDescription);
}
