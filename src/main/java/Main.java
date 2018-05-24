import uk.co.compendiumdev.args.ArgsToMap;
import uk.co.compendiumdev.cli.*;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.TechWebTest101GameDefinitionGenerator;

import java.util.Map;
import java.util.Scanner;

public class Main {

    /**
     * Example of a main class that could start a game
     * @param args
     */
    /*
    public static void main(String [] args){



        Map<String, CommandLineArg> commandLineArgs = ArgsToMap.get(args);

        CliGame cliGame = new CliGame(commandLineArgs);

        cliGame.processStartupCLiArgs();

        // if the command line args were information
        // rather than game setup then stop here
        if(!cliGame.canPlayGame()){
            System.exit(0);
        }

        cliGame.initialiseGame(new TechWebTest101GameDefinitionGenerator());

        Scanner input = new Scanner(System.in);

        cliGame.startGameLoop(input);

    }
    */
}
