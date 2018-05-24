package uk.co.compendiumdev.restmud.engine.game.scripting;

import uk.co.compendiumdev.restmud.engine.game.playerevents.BroadcastGameMessage;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 05/06/2016.
 */
public class ProcessConditionReturn {

    private final List<LastAction> actions;
    private final List<String> messages;
    private final List<AttributePair> collectables;
    private final List<AttributePair> locationObjects;
    private boolean forceLook;

    public ProcessConditionReturn(){
        actions = new ArrayList<>();
        messages = new ArrayList<>();
        collectables = new ArrayList<>();
        locationObjects = new ArrayList<>();
        forceLook = false;
    }

    public void addNewAction(LastAction action) {
        actions.add(action);
    }

    public void addNewMessage(String message) {
        messages.add(message);
    }

    public boolean hasAnyMessages() {
        return messages.size()>0;
    }

    public List<BroadcastGameMessage> messagesAsBroadcaseMessagesList() {
        List<BroadcastGameMessage> ret = new ArrayList<>();

        for(String message : messages){
            ret.add(new BroadcastGameMessage(System.currentTimeMillis(), message));
        }

        return ret;
    }

    public boolean isLookForced(){
        return forceLook;
    }

    public void weShouldForceALook(){
        forceLook=true;
    }



    public boolean hasAnyActions() {
        return actions.size()>0;
    }

    public LastAction getLastAction() {

        String lastActionState = LastAction.SUCCESS;
        StringBuilder lastActionMessage = new StringBuilder();

        for(LastAction action : actions){
            if(action.lastactionstate.contentEquals(LastAction.FAIL)){
                lastActionState = LastAction.FAIL;
            }
            lastActionMessage.append(action.lastactionresult);
        }

        // combine all the last actions into an action
        return new LastAction(lastActionState, lastActionMessage.toString());
    }

    public void addMessages(List<String> broadcastGameMessages) {
        messages.addAll(broadcastGameMessages);
    }

    public List<String> getMessagesAsList() {
        return messages;
    }

    public void addCollectable(AttributePair attributePair) {
        this.collectables.add(attributePair);
    }

    public void addLocationObject(AttributePair attributePair) {
        this.locationObjects.add(attributePair);
    }

    public boolean hasAnyCollectables() {
        return  collectables.size()>0;
    }

    public boolean hasAnyLocationObjects() {
        return  locationObjects.size()>0;
    }

    public List<AttributePair> getCollectables() {
        return collectables;
    }

    public List<AttributePair> getLocationObjects() {
        return locationObjects;
    }

    public void addCollectables(List<AttributePair> collectables) {
        this.collectables.addAll(collectables);
    }

    public void addLocationObjects(List<AttributePair> objects) {
        this.locationObjects.addAll(objects);
    }

    public void append(ProcessConditionReturn anotherRet) {
        addMessages(anotherRet.getMessagesAsList());
        addActions(anotherRet.getActionsAsList());
        addCollectables(anotherRet.getCollectables());
        addLocationObjects(anotherRet.getLocationObjects());
        if(anotherRet.isLookForced()){
            weShouldForceALook();
        }
    }

    private void addActions(List<LastAction> actionsAsList) {
        actions.addAll(actionsAsList);
    }

    private List<LastAction> getActionsAsList() {
        return actions;
    }
}
