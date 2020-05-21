package uk.co.compendiumdev.restmud.commandlist;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommandListFile {

    public static List<Command> read(String filepath){
        List<Command> commands = new ArrayList<>();

        String fileContents="";

        try {
            fileContents = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading file " + filepath, e);
        }

        String[] parsedCommandList = fileContents.split(System.lineSeparator());
        for(String aLine : parsedCommandList){
            String theCommand[] = aLine.split(",");
            if(theCommand!=null && theCommand.length>0){
                if(theCommand[0]!=null && theCommand[0].length()>0){
                    String verb="";
                    String noun="";

                    verb = theCommand[0];
                    if(theCommand.length>1) {
                        if (theCommand[1] != null && theCommand[1].length() > 0) {
                            noun = theCommand[1];
                        }
                    }

                    Command aCommand = new Command(verb, noun);
                    commands.add(aCommand);
                }
            }
        }

        return commands;
    }
}
