package uk.co.compendiumdev.restmud.bottest;

import org.junit.Ignore;
import org.junit.Test;
import uk.co.compendiumdev.restmud.api.RestMudAPI;
import uk.co.compendiumdev.restmud.commandlist.Command;
import uk.co.compendiumdev.restmud.commandlist.CommandExecutor;
import uk.co.compendiumdev.restmud.commandlist.CommandListFile;

import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Alan on 31/01/2017.
 */
public class Walkthrough_using_REST_API_Test {


    private final String REST_MUD_URL = "http://localhost:4567";

    @Ignore("Manually run when example_game is running in single player mode")
    @Test
    public void canPlayExampleGameUsingTheAPIAsSingleUserFromCommandFile(){
        RestMudAPI api = new RestMudAPI(REST_MUD_URL).setForUser("alan").setPassword("password");
        //RestMudAPI api = new RestMudAPI(REST_MUD_URL).setForUser("user");

        List<Command> commands = CommandListFile.read(Paths.get(System.getProperty("user.dir"), "..", "..", "docs", "example_game_walkthrough_commands.txt").toString());

        CommandExecutor commandExecutor = new CommandExecutor(api);

        commandExecutor.execute(commands);

    }

    @Ignore("Manually run when test_basic_game is running in single player mode")
    @Test
    public void canPlayTestBasicGameUsingTheAPIAsSingleUserFromCommandFile(){
        //RestMudAPI api = new RestMudAPI(REST_MUD_URL).setForUser("alan").setPassword("password");
        RestMudAPI api = new RestMudAPI(REST_MUD_URL).setForUser("user");

        List<Command> commands = CommandListFile.read(Paths.get(System.getProperty("user.dir"), "..", "..", "docs", "test_game_basic_walkthrough_commands.txt").toString());

        CommandExecutor commandExecutor = new CommandExecutor(api);

        commandExecutor.execute(commands);

        api.reportStats();

    }

    @Ignore("Manually run when test_basic_game is running in single player mode")
    @Test
    public void canPlayZombieZorkGameUsingTheAPIAsSingleUserFromCommandFile(){
        RestMudAPI api = new RestMudAPI(REST_MUD_URL).setForUser("user");

        List<Command> commands = CommandListFile.read(Paths.get(System.getProperty("user.dir"), "..", "..", "docs", "zombie_zork_walkthrough_commands.txt").toString());

        CommandExecutor commandExecutor = new CommandExecutor(api);

        commandExecutor.execute(commands);

    }


    @Ignore("Treasure Hunt Game running in single player mode")
    @Test
    public void canTreasureHuntGameUsingTheAPIAsSingleUserFromCommandFile(){
        RestMudAPI api = new RestMudAPI(REST_MUD_URL).setForUser("user");

        List<Command> commands = CommandListFile.read(Paths.get(System.getProperty("user.dir"), "..", "..", "docs", "treasure_hunt_basic_walkthrough_commands.txt").toString());

        CommandExecutor commandExecutor = new CommandExecutor(api);

        commandExecutor.execute(commands);


    }
}
