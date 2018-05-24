package uk.co.compendiumdev.restmud.engine.game.playerevents;

public class BroadcastGameMessage {

    public final long timestamp;
    public final String message;

    public BroadcastGameMessage(long timestamp, String message){
        this.timestamp = timestamp;
        this.message = message;
    }

}
