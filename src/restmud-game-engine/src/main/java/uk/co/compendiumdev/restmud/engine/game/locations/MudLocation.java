package uk.co.compendiumdev.restmud.engine.game.locations;

import uk.co.compendiumdev.restmud.engine.game.Inventory;
import uk.co.compendiumdev.restmud.engine.game.InventoryLocationObjects;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LookLocation;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.VisibleExit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MudLocation {
    private final String locationId;
    private final String shortName;
    private final String description;
    private final String routesAndExitsDefinition;
    private boolean allowsTreasureHoarding;
    private final Map<String, MudLocationExit> exits = new HashMap<>();
    private final Inventory collectables = new Inventory();

    private boolean isInDarkness;
    private final InventoryLocationObjects theObjects = new InventoryLocationObjects();
    private String reason;


    public MudLocation(String locationId, String shortName, String description, String routesAndExits) {
        this.locationId = locationId.toLowerCase();
        this.shortName = shortName;
        this.description = description;
        this.allowsTreasureHoarding = false;
        this.isInDarkness = false;
        this.routesAndExitsDefinition = routesAndExits;

        exits.putAll(new MudLocationParser(locationId).parse(routesAndExits));

    }

    public MudLocation setCanHoardTreasureHere(boolean canOrNot) {
        allowsTreasureHoarding = canOrNot;
        return this;
    }

    public boolean canHoardTreasureHere() {
        return allowsTreasureHoarding;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getDescription() {
        return description;
    }

    public boolean canGo(String direction) {
        return (exitFor(direction) != null);
    }

    public MudLocationExit exitFor(String direction){
        return exits.get(direction.toLowerCase());
    }

    public MudLocationExit exitWhichLeadsTo(String destination) {

        for (Map.Entry<String, MudLocationExit> anExit : exits.entrySet()) {
            MudLocationExit exitLeadsTo = anExit.getValue();
            if (exitLeadsTo.getDestinationId().toLowerCase().contentEquals(destination.toLowerCase())) {
                return exitLeadsTo;
            }
        }
        return null;
    }

    public Inventory collectables() {
        return collectables;
    }

    public String getShortName() {
        return shortName;
    }

    public List<VisibleExit> getVisibleExits() {
        List<VisibleExit> theExits = new ArrayList<>();
        for (MudLocationExit anExit : exits.values()) {
            if (anExit.isVisible()) {
                theExits.add(new VisibleExit(anExit.getDirection()));
            }
        }
        return theExits;
    }

    public synchronized boolean moveCollectableToPlayerInventory(String collectableId, Inventory inventory) {
        return this.collectables.moveItemTo(collectableId, inventory);
    }

    public synchronized boolean moveCollectableFromPlayerInventory(String collectableId, Inventory inventory) {
        return inventory.moveItemTo(collectableId, this.collectables);
    }

    public LookLocation getLookLocation() {
        return new LookLocation(locationId, shortName, description, isInDarkness);
    }


    public MudLocation makeDark() {
        setIsInDarkness(true);
        return this;
    }

    public MudLocation makeLight() {
        setIsInDarkness(false);
        return this;
    }

    private void setIsInDarkness(boolean isInDarkness){
        this.isInDarkness = isInDarkness;
    }

    public boolean isLocationDark() {
        return isInDarkness;
    }

    public InventoryLocationObjects objects() {
        return theObjects;
    }

    public MudLocation getClonedCopy() {
        MudLocation cloned = new MudLocation(this.locationId, this.shortName, this.description, this.routesAndExitsDefinition);
        cloned.setCanHoardTreasureHere(this.allowsTreasureHoarding);
        cloned.setIsInDarkness(this.isInDarkness);
        return cloned;
    }

    public MudLocation because(String reason) {
        this.reason = reason;
        return this;
    }

    public void addExit(String direction, String toLocation) {
        exits.put(direction, new MudLocationExit(locationId, direction, toLocation));
    }

    public void addExit(MudLocationExit anExit) {
        exits.put(anExit.getDirection(), anExit);
    }

    public void removeExit(String direction) {
        exits.remove(direction);
    }
}
