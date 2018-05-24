package uk.co.compendiumdev.restmud.web.config;

import uk.co.compendiumdev.restmud.engine.game.PlayerMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 20/01/2017.
 */
public class AppConfigFromArgs {
    public static AppConfig getAppConfig(String[] args) {
        AppConfig config = new AppConfig();
        List<String> processedKeys = new ArrayList<String>();

        String key="";
        String value="";

        //assume args has (-key value) pairs
        // e.g. -port 666
        // if we get `-port 666 1234` then we would set port to 1234
        // if we get `1234 -port` then we would have a key value pair and port would be set to 1234
        // support:
        // -port
        // -playermode single
        // -gamename name_of_game


        for(String arg : args){

            if(arg.startsWith("-")){
                key = arg.replaceFirst("-","");
            }else{
                value = arg;
            }

            if(key.length()>0 && value.length()>0){

                Boolean assumeProcessed = true;

                switch(key){
                    case "port":
                        config.setPort(value);
                        break;
                    case "playermode":
                        config.setPlayerMode(value);
                        if(config.playerMode() == PlayerMode.MULTI && !processedKeys.contains("gamename")){
                            // multiplayer has a different default game name
                            config.setGameName(AppConfig.DEFAULT_MULTI_USER_GAME);
                        }
                        break;
                    case "gamename":
                        config.setGameName(value);
                        break;
                    default:
                        assumeProcessed=false;
                        System.out.println(String.format("Could not process arg %s", key));
                }

                if(assumeProcessed){
                    processedKeys.add(key);
                    key="";
                    value="";
                }
            }

        }

        return config;
    }
}
