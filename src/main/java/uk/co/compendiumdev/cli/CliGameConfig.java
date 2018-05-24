package uk.co.compendiumdev.cli;

public class CliGameConfig {

    public final String CLI_ENGINE_VERSION = "1.0";

    private static final String COPYRIGHT_STRING = "Copyright 2018 Compendium Developments Ltd - Alan Richardson - http://EvilTester.com\n";

    public CliGameConfig() {

    }

    public String getVersionText() {
        return String.format("RestMud CLI Game Engine version %s\n%s\n", CLI_ENGINE_VERSION, COPYRIGHT_STRING);
    }

    public String getCliArgumentHelpText(boolean moreinfo) {

        StringBuilder clihelp = new StringBuilder();

        clihelp.append("\n");
        clihelp.append("RestMud CLI Command Line Help:\n");
        clihelp.append("\n");
        clihelp.append("-h   show this help information (also -help and /?)\n");
        clihelp.append("-v   engine version number\n");
        clihelp.append("-cols=80 - the text wrap limit for the output ie. 80 columns (min 30)\n");
        clihelp.append("-verbose   show more info\n");
        clihelp.append("-txtlog   write your game log to a text file as you play\n");

        if(moreinfo){
            clihelp.append("-txtlog=filename.txt   write your game log to filename.txt\n");
            clihelp.append("-jsonlog   output raw json for testing\n");
            clihelp.append("-jsonlog=filename.json   output json to specific files\n");
            clihelp.append("-nobackup   do not create backup log files\n");
            clihelp.append("-commandslog   create a commands.txt log of commands entered\n");
        }

        clihelp.append("\n");

        clihelp.append(getVersionText());

        clihelp.append("\n");

        return clihelp.toString();
    }

    public String getHelpText(String gameName) {

        StringBuilder clihelp = new StringBuilder();
        clihelp.append(gameName + "\n");
        clihelp.append("\n");
        clihelp.append("RestMud In Game Help:\n");
        clihelp.append("\n");
        clihelp.append("This is a verb/noun text adventure.\n");
        clihelp.append("\n");
        clihelp.append("Use simple verbs like 'look', 'go', 'use', 'examine'\n");
        clihelp.append("e.g. 'go e' to go east.\n");
        clihelp.append("\n");
        clihelp.append("enter 'QUIT-GAME' to exit the game\n");
        clihelp.append("\n");
        clihelp.append(getVersionText());

        return clihelp.toString();
    }
}
