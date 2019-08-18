package uk.co.compendiumdev.args;

import uk.co.compendiumdev.cli.CommandLineArg;

import java.util.HashMap;
import java.util.Map;

public class ArgsToMap {
    public static Map<String, CommandLineArg> get(String[] args) {
        Map<String, CommandLineArg> commandLineArgs = new HashMap<>();

        // parse command line arguments here
        for(String arg : args){
            CommandLineArg cliarg = CommandLineArg.createFrom(arg);
            if(cliarg!=null){
                commandLineArgs.put(cliarg.argument(), cliarg);
            }
        }

        return commandLineArgs;
    }
}
