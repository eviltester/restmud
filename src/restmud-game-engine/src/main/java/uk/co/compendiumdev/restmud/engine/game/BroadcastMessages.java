package uk.co.compendiumdev.restmud.engine.game;


import uk.co.compendiumdev.restmud.engine.game.playerevents.BroadcastGameMessage;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.GameMessages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BroadcastMessages {

    // concurrent queue was giving me intermittent access errors so trying a different collection
    // https://stackoverflow.com/questions/17578299/java-concurrent-array-list-access
    private final List<BroadcastGameMessage> broadcasts = Collections.synchronizedList(new ArrayList<BroadcastGameMessage>());
    //private ConcurrentLinkedDeque<BroadcastGameMessage> broadcasts = new ConcurrentLinkedDeque<>();

    public void add(String message) {
        broadcasts.add(new BroadcastGameMessage(System.currentTimeMillis(), message));
    }

    public void clear() {
        broadcasts.clear();
    }

    public void trimTo(int retainThisMany) {
        while(broadcasts.size()>retainThisMany){
            broadcasts.remove(0);
            // for deque
            // broadcasts.removeFirst();
        }
    }

    public GameMessages getMessagesSince(long startingTimestamp) {



        List<BroadcastGameMessage> gameMessages = new ArrayList<>();

        synchronized(broadcasts) {
            for (BroadcastGameMessage gameMessage : broadcasts) {
                if (gameMessage.timestamp >= startingTimestamp) {
                    gameMessages.add(gameMessage);
                }
            }
        }

        return GameMessages.getEmpty().addAdditionalMessages(gameMessages);
    }
}
