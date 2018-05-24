package uk.co.compendiumdev.cli;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.GameDefinitionPopulator;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.TechWebTest101GameDefinitionGenerator;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

import java.util.Map;
import java.util.Scanner;

public class CliGame {
    private final CliGameConfig config;
    private final Map<String, CommandLineArg> commandLineArgs;
    private boolean playGame;
    private CliGameProcessor gameProcessor;
    private CliResultPrinter output;

    public CliGame( Map<String, CommandLineArg> commandLineArgs) {
        this.config = new CliGameConfig();
        this.commandLineArgs = commandLineArgs;
        this.playGame = true;
    }

    public boolean processStartupCLiArgs() {

        // handle information command line args here
        if(commandLineArgs.containsKey("-v")){
            System.out.println(config.getVersionText());
            playGame=false;
        }

        if(commandLineArgs.containsKey("-h") || commandLineArgs.containsKey("-help") || commandLineArgs.containsKey("/?")){
            System.out.println(config.getCliArgumentHelpText(commandLineArgs.containsKey("-verbose")));
            playGame=false;
        }

        return playGame;
    }

    public boolean canPlayGame() {
        return playGame;
    }

    public void initialiseGame(GameDefinitionPopulator defnPopulator) {
        // initialise game
        GameInitializer theGameInit = new GameInitializer();
        theGameInit.setDisplayStartupMessages(false);
        MudGame game = theGameInit.getGame();
        MudGameDefinition defn = MudGameDefinition.create(game.getUserInputParser());

        defnPopulator.define(defn);
        defn.setUserInputParser(game.getUserInputParser());
        theGameInit.generate(defn);
        theGameInit.addDefaultUser("The Test User", "playerone", "aPassword");

        this.gameProcessor = CliGameProcessor.start(game, commandLineArgs);
        this.output = new CliResultPrinter(commandLineArgs);
        this.output.initialise();

    }

    public void initialiseGame(String resourceFileName) {
        // initialise game
        GameInitializer theGameInit = new GameInitializer();
        theGameInit.setDisplayStartupMessages(false);

        theGameInit.generate(resourceFileName);

        MudGame game = theGameInit.getGame();

        theGameInit.addDefaultUser("The Test User", "playerone", "aPassword");

        this.gameProcessor = CliGameProcessor.start(game, commandLineArgs);
        this.output = new CliResultPrinter(commandLineArgs);
        this.output.initialise();

    }

    public void startGameLoop(Scanner input) {
        String inputLine = "";


        SimpleLogFile commandLogFile = new SimpleLogFile("commands.txt");

        if(commandLineArgs.containsKey("-commandslog")){
            commandLogFile.withBackups(!commandLineArgs.containsKey("-nobackups")).create();
        }

        printHelpText();

        System.out.println("> ");

        while(!inputLine.contentEquals("QUIT-GAME") && input.hasNextLine()){


            inputLine = input.nextLine();


            commandLogFile.append(inputLine + "\n");

            if(inputLine.equalsIgnoreCase("QUIT-GAME")){
                System.exit(0);
            }else{
                if(inputLine.equalsIgnoreCase("help")){
                    printHelpText();

                }else {

                    output.reportCommand(inputLine);
                    ResultOutput result = gameProcessor.processInput(inputLine);
                    output.reportResult(result);
                }
            }

            System.out.println("> ");

        };
    }

    private void printHelpText() {
        System.out.println(config.getHelpText(gameProcessor.getGameName()));
    }
}
