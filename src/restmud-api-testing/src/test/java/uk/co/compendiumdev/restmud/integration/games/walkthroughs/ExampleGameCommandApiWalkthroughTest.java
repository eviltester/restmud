package uk.co.compendiumdev.restmud.integration.games.walkthroughs;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.co.compendiumdev.integration.http.RestMudInMemoryConfig;
import uk.co.compendiumdev.restmud.api.RestMudAPI;
import uk.co.compendiumdev.restmud.commandlist.Command;
import uk.co.compendiumdev.restmud.commandlist.CommandExecutor;
import uk.co.compendiumdev.restmud.commandlist.CommandListFile;
import uk.co.compendiumdev.sparktesting.RestMudStarter;

import java.nio.file.Paths;
import java.util.List;

public class ExampleGameCommandApiWalkthroughTest {

    static String gameUrl = "http://localhost";
    static String registrationCode = "CHANGEME";
    static String username = "user" + System.currentTimeMillis();
    static String password = "password" + System.currentTimeMillis();

    static RestMudAPI api;
    static RestMudStarter starter;

    @BeforeClass
    public static void createAUser(){

        // setup a local instance here
        RestMudInMemoryConfig config = new RestMudInMemoryConfig()
                .port("1234")
                .playerMode("multi")
                .registrationCode("CHANGEME")
                .gameNamed("exampleDocumentedGame.json");

        starter = RestMudStarter.singleton(config);
        starter.startSparkAppIfNotRunning(Integer.parseInt(config.port()));

        gameUrl = gameUrl + ":" + config.port() + "/";

        api = new RestMudAPI(gameUrl);
        api.setRegistrationSecretCode(registrationCode);
        api.userNeedsToRegister(true);
        api.setForUser(username);
        api.setPassword(password);

        System.out.println(username);
        System.out.println(password);

        api.register();

    }

    @Test
    public void canPlayGameUsingTheAPIAsSingleUserFromCommandFile(){

        List<Command> commands = CommandListFile.read(Paths.get(System.getProperty("user.dir"), "..", "examplerestmudgame", "docs", "example_game_walkthrough_commands.txt").toString());

        CommandExecutor commandExecutor = new CommandExecutor(api);

        commandExecutor.execute(commands);
    }

    @AfterClass
    static public void stopTheServer(){
        starter.stopServer();
    }
}
