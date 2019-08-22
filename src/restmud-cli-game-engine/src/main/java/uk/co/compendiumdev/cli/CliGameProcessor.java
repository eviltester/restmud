package uk.co.compendiumdev.cli;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

import java.util.Map;

public class CliGameProcessor {
    private final MudGame game;
    private final Map<String, CommandLineArg> commandLineArgs;

    public CliGameProcessor(MudGame game, Map<String, CommandLineArg> commandLineArgs) {
        this.game = game;
        this.commandLineArgs = commandLineArgs;
    }

    public static CliGameProcessor start(MudGame game, Map<String, CommandLineArg> commandLineArgs) {

        return new CliGameProcessor(game, commandLineArgs);
    }

    public ResultOutput processInput(String inputLine) {
        // parse the command from input
        String[] parsed = inputLine.split(" ");
        String verb="look";
        String noun="";
        if(parsed.length>0 && parsed[0]!=null){verb=parsed[0];}

        // join everything else together as a noun e.g. examine a small wall become a_small_wall as the noun
        if(parsed.length>1){
            // create the noun
            for(int x= 1; x< parsed.length; x++){
                if(parsed[x]!=null){
                    if(noun.length()>0){
                        noun=noun+"_";
                    }
                    noun = noun + parsed[x];
                }
            }
        }

        // process command in game
        // TODO - avoid the need for null here
        return game.getCommandProcessor().processTheVerbInGame("playerone", verb, noun, null);
    }

    public String getGameName() {
        if(game!=null) {
            return game.getGameName();
        }else{
            return "Unkown Game";
        }
    }
}
