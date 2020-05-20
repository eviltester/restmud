package uk.co.compendiumdev.datageneration;

import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class CacheGamesListTest {

    @Test
    public void hackInACachedList() throws FileNotFoundException {
        // todo: fix resource directory reading in a jar so we don't do this
        // have not been able to read list of resources when packaged into a jar
        // so create a cached list written by the test

        MudGameDefinitionSerialiser serializer = new MudGameDefinitionSerialiser();
        List<String> games = serializer.getListOfBuiltInGames();

        final File file = new File(System.getProperty("user.dir") + "/src/main/resources/games/cachedgameslist.txt");
        System.out.println(file.getAbsolutePath());
        if(file.exists()){
            file.delete();
        }
        PrintWriter output = new PrintWriter(file);
        for(String gameName : games){
            if(gameName.trim().endsWith(".json")) {
                output.println(gameName.trim());
            }
        }
        output.println("defaultenginegame.json"); // add the default game from the engine
        output.close();
    }
}
