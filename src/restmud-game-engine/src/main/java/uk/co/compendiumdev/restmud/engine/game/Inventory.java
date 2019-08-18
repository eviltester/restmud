package uk.co.compendiumdev.restmud.engine.game;

import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


// used store 'things' - could be
public class Inventory {

    private final Map<String, MudCollectable> inventoryList;

    public Inventory(){
        this.inventoryList = new ConcurrentHashMap<String, MudCollectable>();
    }

    public boolean addItem(MudCollectable collectable) {
        inventoryList.put(collectable.getCollectableId().toLowerCase(), collectable);
        return contains(collectable.getCollectableId());
    }

    public List<MudCollectable> itemsView(){
        return new ArrayList<MudCollectable>(inventoryList.values());
    }

    public boolean removeItem(String collectableId) {
        return inventoryList.remove(collectableId.toLowerCase())!=null;
    }

    public boolean removeItem(MudCollectable collectable) {
        return removeItem(collectable.getCollectableId());
    }


    public boolean contains(String thingId) {
        return inventoryList.containsKey(thingId.toLowerCase());
    }

    public boolean contains(MudCollectable collectable) {
        return contains(collectable.getCollectableId().toLowerCase());
    }

    public MudCollectable get(String thingId){
        return inventoryList.get(thingId.toLowerCase());
    }

    public String asStringListOfDescriptions(String prefix) {

            StringBuilder output = new StringBuilder();

            output.append(prefix);

            int count = 0;
            for(MudCollectable item : itemsView()){

                String formatToAppend = ", %s";

                if(count==0){
                    formatToAppend = " %s";
                }

                output.append(String.format(formatToAppend, item.getDescription()));

                count++;
            }

            if(count==0){
                output.append(" Nothing.");
            }

            return output.toString();

    }

    public IdDescriptionPair[] asIdDescriptionPairs() {

        ArrayList<IdDescriptionPair> pairs = new ArrayList<>();

        for(MudCollectable item : itemsView()){
            pairs.add(new IdDescriptionPair(item.getCollectableId(),item.getDescription() ));
        }

        IdDescriptionPair[] retArray = new IdDescriptionPair[pairs.size()];
        return pairs.toArray(retArray);
    }

    public synchronized boolean moveItemTo(String inventoryId, Inventory inventory) {
        boolean retCode = false;
        // remove collectable from location
        MudCollectable theThing = inventoryList.get(inventoryId.toLowerCase());
        if (inventoryList.remove(inventoryId.toLowerCase())!=null) {
            // add collectable to player inventory
            retCode = inventory.addItem(theThing);
        }

        return retCode;
    }
}
