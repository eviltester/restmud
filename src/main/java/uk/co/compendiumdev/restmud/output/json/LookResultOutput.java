package uk.co.compendiumdev.restmud.output.json;

import java.util.List;


public class LookResultOutput {

    public LookLocation location;
    public List<VisibleExit> exits;
    public IdDescriptionPair[] collectables;
    public InventoryReport treasureHoard;
    public LookPlayer[] otherPeopleHere;
    public List<BroadcastGameMessage> messages;
    public List<VisibleGate> visibleGates;
    public IdDescriptionPair[] visibleObjects;


    public boolean isObjectVisible(String objectId) {
        if(visibleObjects!=null){
            for( IdDescriptionPair anObject : visibleObjects){
                if(anObject.id.contentEquals(objectId)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCollectableVisible(String collectableId) {
        for( IdDescriptionPair aCollectable : collectables){
            if(aCollectable.id.contentEquals(collectableId)){
                return true;
            }
        }
        return false;
    }
}
