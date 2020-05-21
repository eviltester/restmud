package uk.co.compendiumdev.restmud.integration.games.treasureHuntBasicGame;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import uk.co.compendiumdev.restmud.api.RestMudAPI;

@Ignore("Integration test using heroku")
public class RestRoomsTest {

    // using a running version of TreasureHuntBasicGenerator game
    // make sure the RestRooms are working

    // TODO: change this to use local server and change game in server to match the tests
    static String gameUrl = "http://restmud.herokuapp.com/";
    static String registrationCode = "CHANGEME";
    static String username = "user" + System.currentTimeMillis();
    static String password = "password" + System.currentTimeMillis();

    static RestMudAPI api = new RestMudAPI(gameUrl);

    @BeforeClass
    public static void createAUser(){

        api.setRegistrationSecretCode(registrationCode);
        api.userNeedsToRegister(true);
        api.setForUser(username);
        api.setPassword(password);

        System.out.println(username);
        System.out.println(password);

        api.register();


    }

    @Test
    public void canUseRestRoomsSuccessfully(){

        api.look();
        api.go("N");
        api.go("E");
        api.go("E");
        api.genericVerbNoun("examine", "postexplain");

        int currentScore = api.getScore();



        api.http_POST_Request( "api/player/" + username, "{\"verb\" : \"examine\", \"nounphrase\" : \"postsign\"}");

        int score = api.getScore();
        Assert.assertTrue(String.format("%d > %d", score, currentScore), score > currentScore);
        currentScore = score;


        
        api.setCustomHeader("X-REST-MUD-POST-EXAMINE","anyvalue");
        api.http_POST_Request( "api/player/" + username, "{\"verb\" : \"examine\", \"nounphrase\" : \"postheader\"}");

        api.deleteCustomHeader("X-REST-MUD-POST-EXAMINE");

        score = api.getScore();
        Assert.assertTrue(String.format("%d > %d", score, currentScore), score > currentScore);
        currentScore = score;
        
        api.go("E");

        api.setCustomHeader("X-REST-MUD-GET-EXAMINE","anyvalue");
        api.http_GET_Request("api/player/" + username + "/examine/getheader");
        score = api.getScore();
        Assert.assertTrue(String.format("%d > %d", score, currentScore), score > currentScore);
        currentScore = score;

        api.deleteCustomHeader("X-REST-MUD-GET-EXAMINE");


        api.go("E");

        api.http_DELETE_Request("api/player/" + username + "/fight/smalltroll");
        score = api.getScore();
        Assert.assertTrue(String.format("%d > %d", score, currentScore), score > currentScore);
        currentScore = score;

        api.go("N");

        api.setCustomHeader("X-SUPER-FIGHTING-SKILLS-VERY-HARD","anyvalue");
        api.http_DELETE_Request("api/player/" + username + "/fight/largetroll");
        score = api.getScore();
        Assert.assertTrue(String.format("%d > %d", score, currentScore), score > currentScore);
        currentScore = score;


    }
}
