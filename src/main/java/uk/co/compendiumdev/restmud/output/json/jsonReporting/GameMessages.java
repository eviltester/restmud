package uk.co.compendiumdev.restmud.output.json.jsonReporting;

import uk.co.compendiumdev.restmud.engine.game.playerevents.BroadcastGameMessage;

import java.util.ArrayList;
import java.util.List;

public class GameMessages {

    public List<BroadcastGameMessage> messages;

    public GameMessages(List<BroadcastGameMessage> messages){
        this.messages = messages;
    }

    public static GameMessages getEmpty() {
        return new GameMessages(new ArrayList<>());
    }

    public GameMessages addAdditionalMessages(List<BroadcastGameMessage> extramessages){
        messages.addAll(extramessages);
        return this;
    }
}
