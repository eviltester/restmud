package uk.co.compendiumdev.restmud.games.serialisation;

import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.*;

import java.io.File;

/**
 * Created by Alan on 10/10/2017.
 */
public class CreateJsonFilesForGamesTest {

    @Test
    public void canWriteTreasureHunterBasicGameToResourceFile(){

        GameInitializer theGameInit = new GameInitializer();
        MudGame game = theGameInit.getGame();
        MudGameDefinition defn = MudGameDefinition.create(game.getUserInputParser());
        new TreasureHuntBasicGameDefinitionGenerator().define(defn);
        MudGameDefinitionSerialiser loader = new MudGameDefinitionSerialiser();
        File dir = new File(System.getProperty("user.dir"), "src/main/resources/games/");
        dir.mkdirs();
        loader.writeToFile(new File(dir, "treasureHunterGame.json"), defn);
    }
}
