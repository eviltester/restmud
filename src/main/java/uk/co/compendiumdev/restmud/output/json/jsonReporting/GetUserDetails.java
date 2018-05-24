package uk.co.compendiumdev.restmud.output.json.jsonReporting;

public class GetUserDetails {
    public final String displayName;
    public final String userName;
    public final String authToken;
    public final Integer score;

    public GetUserDetails(String userName, String displayName, String authToken){
        this(userName, displayName, authToken, null);
    }

    public GetUserDetails(String userName, String displayName, String authToken, Integer score) {
        this.userName = userName;
        this.displayName = displayName;
        this.authToken = authToken;
        this.score = score;
    }
}
