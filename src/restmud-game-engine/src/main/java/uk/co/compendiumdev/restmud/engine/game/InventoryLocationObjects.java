package uk.co.compendiumdev.restmud.engine.game;

import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


// TODO rename to not be an 'Inventory'
// used store 'things' - could be
public class InventoryLocationObjects {

    private final Map<String, MudLocationObject> inventoryList;

    public InventoryLocationObjects(){
        this.inventoryList = new ConcurrentHashMap<String, MudLocationObject>();
    }

    public boolean addItem(MudLocationObject theObject) {
        inventoryList.put(theObject.getObjectId(), theObject);
        return contains(theObject.getObjectId());
    }

    public List<MudLocationObject> itemsView(){
        return new ArrayList<MudLocationObject>(inventoryList.values());
    }

    public boolean removeItem(String thingId) {
        return inventoryList.remove(thingId)!=null;
    }

    public boolean contains(String thingId) {
        return inventoryList.containsKey(thingId);
    }


    public MudLocationObject get(String thingId) {
        return inventoryList.get(thingId);
    }

    public void setFrom(Map<String, MudLocationObject> from) {
        inventoryList.putAll(from);
    }

    public Map<String, MudLocationObject> getActualMap() {
        return inventoryList;
    }

    public Map<String,MudLocationObject> getClonedCopiedLocations() {
        Map<String, MudLocationObject> copyMap = new HashMap<String, MudLocationObject>();
        for(MudLocationObject locationObject : inventoryList.values()){
            copyMap.put(locationObject.getObjectId(), locationObject.createClonedCopy());
        }
        return copyMap;
    }
}
