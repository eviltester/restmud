package uk.co.compendiumdev.restmud.output.json.jsonReporting;


public class LookPlayer {
    public final String displayName;
    public final String userName;

    public LookPlayer(String userName, String displayName) {
        this.userName = userName;
        this.displayName = displayName;
    }
}
