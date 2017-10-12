package uk.co.compendiumdev.restmud.games.serialisation;

import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.*;

import java.io.File;

public class CreateJsonFilesForDefaultGamesTest {


    @Test
    public void canWriteDefaultTestGameToResourceFile(){

        GameInitializer theGameInit = new GameInitializer();
        MudGame game = theGameInit.getGame();
        MudGameDefinition defn = MudGameDefinition.create(game.getUserInputParser());
        DefaultGameDefinitionGenerator.define(defn);
        MudGameDefinitionSerialiser loader = new MudGameDefinitionSerialiser();
        loader.writeToFile(new File(System.getProperty("user.dir"),"src/main/resources/games/defaultenginegame.json"), defn);

    }

}
