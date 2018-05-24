package uk.co.compendiumdev.restmud.web.config;

import uk.co.compendiumdev.restmud.engine.game.PlayerMode;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;

import java.util.List;

/**
 * Created by Alan on 20/01/2017.
 */
public class AppConfig {

    // can hard code a particular game for release, or just use the default.json
    public final static String DEFAULT_SINGLE_USER_GAME = ""; //"test_game_basic"; // "treasure_hunt_basic"; // "test_game_basic";
    public final static String DEFAULT_MULTI_USER_GAME = ""; // "test_game_basic"; // "treasure_hunt_basic"; //"example_game";           //treasure_hunt_basic

    private int port=4567; // default port
    private PlayerMode playerMode = PlayerMode.SINGLE;
    private String gameName = ""; // By default use default.json or whatever is there DEFAULT_SINGLE_USER_GAME;

    public void setPort(String portStr) {
        try {
            this.port = Integer.parseInt(portStr);
        }catch(Exception e){
            System.out.println(String.format("Could not parse %s into a port integer value", portStr));
        }
    }

    public void setPlayerMode(String playerMode) {
        if(playerMode.toLowerCase().contains("multi")){
            this.playerMode = PlayerMode.MULTI;
        }
        if(playerMode.toLowerCase().contains("single")){
            this.playerMode = PlayerMode.SINGLE;
        }
    }

    public PlayerMode playerMode() {
        return playerMode;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int port() {
        return port;
    }

    public String gameName() {

        if(gameName.length()==0){
            MudGameDefinitionSerialiser games = new MudGameDefinitionSerialiser();
            List<String> gamesList = games.getListOfBuiltInGames();
            if(gamesList.contains("default.json")){
                return "default.json";
            }else{
                return gamesList.get(0);
            }
        }

        return gameName;
    }
}
