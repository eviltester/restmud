package uk.co.compendiumdev.restmud.output.json.jsonReporting;

import uk.co.compendiumdev.restmud.engine.game.playerevents.BroadcastGameMessage;
import uk.co.compendiumdev.restmud.engine.game.scripting.AttributePair;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void setLocation(LookLocation location) {
        this.location = location;
    }

    public void setVisibleExits(List<VisibleExit> visibleExits) {
        this.exits = visibleExits;
    }

    public void setVisibleCollectables(IdDescriptionPair[] visibleCollectables) {
        this.collectables = visibleCollectables;
    }

    public void setTreasureHoardContents(InventoryReport treasureHoardContents) {
        this.treasureHoard = treasureHoardContents;
    }

    public void setOtherPeopleHere(LookPlayer[] otherPeopleHere) {
        this.otherPeopleHere = otherPeopleHere;
    }

    public void addEventMessage(BroadcastGameMessage message) {

        // only create if there are messages to avoid it appearing in the output if it is empty
        if (messages == null) {
            messages = new ArrayList<BroadcastGameMessage>();
        }
        messages.add(message);
    }

    public void setVisibleGates(List<VisibleGate> visibleGates) {
        this.visibleGates = visibleGates;
    }

    public void setVisibleObjects(IdDescriptionPair[] visibleObjects) {
        this.visibleObjects = visibleObjects;
    }

    public void addAdditionalCollectables(List<AttributePair> collectablesToAdd) {

        if (collectablesToAdd == null) {
            return;
        }

        if (collectablesToAdd.size() == 0) {
            return;
        }

        // create the array if necessary
        if (collectables == null) {
            collectables = new IdDescriptionPair[collectablesToAdd.size()];
        }

        List<IdDescriptionPair> pairs = new ArrayList<>();            // easier to work with it as a list
        pairs.addAll(Arrays.asList(collectables));

        for (AttributePair collectableToAdd : collectablesToAdd) {
            pairs.add(new IdDescriptionPair(collectableToAdd.name, collectableToAdd.value));
        }

        collectables = new IdDescriptionPair[pairs.size()];
        pairs.toArray(collectables);   // add it back into the array
    }

    public void addAdditionalLocationObjects(List<AttributePair> locationObjectsToAdd) {
        if (locationObjectsToAdd == null) {
            return;
        }

        if (locationObjectsToAdd.size() == 0) {
            return;
        }

        // create the array if necessary
        if (visibleObjects == null) {
            visibleObjects = new IdDescriptionPair[locationObjectsToAdd.size()];
        }

        List<IdDescriptionPair> pairs = new ArrayList<>();            // easier to work with it as a list
        pairs.addAll(Arrays.asList(visibleObjects));

        for (AttributePair locationObjToAdd : locationObjectsToAdd) {
            pairs.add(new IdDescriptionPair(locationObjToAdd.name, locationObjToAdd.value));
        }

        visibleObjects = new IdDescriptionPair[pairs.size()];
        pairs.toArray(visibleObjects);   // add it back into the array
    }

    public void addAdditionalVisibleExits(List<VisibleExit> exitsToAdd) {

        if (exitsToAdd == null) {
            return;
        }

        if (exitsToAdd.size() == 0) {
            return;
        }

        // create the array if necessary
        if (exits == null) {
            exits = new ArrayList<>();
        }

        exits.addAll(exitsToAdd);
    }

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
