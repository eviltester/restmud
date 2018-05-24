package uk.co.compendiumdev.restmud.games.serialisation;

import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.TechWebTest101GameDefinitionGenerator;

import java.io.File;

public class CreateJsonFilesForGamesTest {


    @Test
    public void canWriteTechWebTest101GameToResourceFile(){

        GameInitializer theGameInit = new GameInitializer();
        MudGame game = theGameInit.getGame();
        MudGameDefinition defn = MudGameDefinition.create(game.getUserInputParser());
        new TechWebTest101GameDefinitionGenerator().define(defn);
        MudGameDefinitionSerialiser loader = new MudGameDefinitionSerialiser();
        File dir = new File(System.getProperty("user.dir"), "src/main/resources/games/");
        dir.mkdirs();
        loader.writeToFile(new File(dir, "techWebTest101Game.json"), defn);
    }
}
