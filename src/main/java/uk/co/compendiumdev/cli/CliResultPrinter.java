package uk.co.compendiumdev.cli;

import com.google.gson.Gson;
import uk.co.compendiumdev.cli.textoutput.TextLayoutEngine;
import uk.co.compendiumdev.cli.textoutput.TextSpacingWrapper;
import uk.co.compendiumdev.cli.textoutput.TextTruncateWrapper;
import uk.co.compendiumdev.restmud.engine.game.playerevents.BroadcastGameMessage;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LookPlayer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.VisibleExit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CliResultPrinter {
    private final Map<String, CommandLineArg> commandLineArgs;
    private boolean amDebugging=false;
    private String jsonLogFileName="jsonlog.txt";
    private boolean createJsonLogFile;
    private String textLogFileName="txtlog.txt";
    private boolean createTextLogFile=false;
    private boolean createBackupsOfLogFiles=true;
    private int textWrapPosition;
    private boolean verboseOutput=false;
    SimpleLogFile textLogFile;
    SimpleLogFile jsonLogFile;

    public CliResultPrinter(Map<String, CommandLineArg> commandLineArgs) {
        this.commandLineArgs = commandLineArgs;
        if(commandLineArgs.containsKey("-verbose")){
            this.verboseOutput=true;
        }
        this.textWrapPosition = 80;
        if(commandLineArgs.containsKey("-cols")){
            int cols = Integer.valueOf(commandLineArgs.get("-cols").value());
            if(cols > 30){
                this.textWrapPosition = cols;
            }
        }
        if(commandLineArgs.containsKey("-nobackups")){
            this.createBackupsOfLogFiles = false;
        }
        if(commandLineArgs.containsKey("-txtlog")){
            this.createTextLogFile = true;
            // undocumented =
            String givenName = commandLineArgs.get("-txtlog").value();
            if(givenName.length()>0) {
                this.textLogFileName = givenName;
            }
        }
        if(commandLineArgs.containsKey("-jsonlog")){
            this.createJsonLogFile = true;
            // undocumented =
            String givenName = commandLineArgs.get("-jsonlog").value();
            if(givenName.length()>0) {
                this.jsonLogFileName = givenName;
            }
        }
        // super secret command line flag for testing only
        if(commandLineArgs.containsKey("-debug")){
            amDebugging = true;
        }

    }

    public void initialise() {

        // create the log files if `-txtlog`
        if(createTextLogFile) {
            textLogFile = new SimpleLogFile(textLogFileName).withBackups(createBackupsOfLogFiles).create();
        }


        // create the log files if `-jsonlog`
        if(createJsonLogFile) {
            jsonLogFile = new SimpleLogFile(jsonLogFileName).withBackups(createBackupsOfLogFiles).create();
        }
    }



    class Txt{
        private final StringBuilder output;

        public Txt(){
            this.output = new StringBuilder();
        }

        public void line(String text) {
            this.output.append(text + "\n");
        }

        @Override
        public String toString() {
            return this.output.toString();
        }

        public void blankLine() {
            this.output.append("\n");
        }
    }

    public void reportResult(ResultOutput result) {

        Txt txt = new Txt();
        HtmlElementRemover htmlRemover = new HtmlElementRemover();
        TextLayoutEngine wrap = new TextSpacingWrapper().at(textWrapPosition);


        // output results of the command
        // only add this if verbose
        if(verboseOutput) {
            txt.blankLine();
            txt.line("**Status: " + result.resultoutput.lastactionstate + "**");

        }
        txt.blankLine();
        txt.line(wrap.split(htmlRemover.tidy(result.resultoutput.lastactionresult)));





        Gson gson = new Gson();
        // debugging

        if(amDebugging) {
            System.out.println(wrap.split(gson.toJson(result)));
        }
        if(createJsonLogFile){
            jsonLogFile.append("\n");
            jsonLogFile.append(gson.toJson(result));
        }



        if(result.gameMessages != null)
            if(result.gameMessages.messages != null && result.gameMessages.messages.size()>0){
            txt.blankLine();
            for( BroadcastGameMessage message : result.gameMessages.messages){
                txt.line(wrap.split(htmlRemover.tidy("- " + message.message)));
            }
        }

        if(result.look != null){

            if(result.look.location != null && result.look.location.isInDarkness){
                txt.blankLine();
                txt.line("You are in darkness");
            }else{

                // output the description
                if(result.look.location != null &&
                        result.look.location.description !=null &&
                        result.look.location.description.length() > 0) {
                    txt.blankLine();
                    txt.line(wrap.split(htmlRemover.tidy(result.look.location.description)));
                }

                if(result.look.collectables!=null && result.look.collectables.length>0){
                    txt.blankLine();
                    txt.line("You can see:");
                    txt.blankLine();
                    for(IdDescriptionPair collectable : result.look.collectables){
                        txt.line(String.format("- %s (%s)", collectable.description, collectable.id));
                    }
                }
                if(result.look.visibleObjects!=null && result.look.visibleObjects.length>0) {
                    txt.blankLine();
                    txt.line("You can also see:");
                    txt.blankLine();
                    for (IdDescriptionPair anObject : result.look.visibleObjects) {
                        txt.line(String.format("- %s (%s)", anObject.description, anObject.id));
                    }
                }

                if(result.look.otherPeopleHere!=null && result.look.otherPeopleHere.length>0) {
                    txt.blankLine();
                    txt.line("You can also see people:");
                    txt.blankLine();
                    for (LookPlayer aPlayer : result.look.otherPeopleHere) {
                        txt.line(String.format("- %s", aPlayer.displayName));
                    }
                }

                if(result.look.messages != null && result.look.messages.size()>0) {
                    txt.blankLine();
                    txt.line("You are also aware that:");
                    txt.blankLine();
                    for (BroadcastGameMessage message : result.look.messages) {
                        txt.line(wrap.split(htmlRemover.tidy("- " + message.message)));
                    }
                }

                if(result.look.treasureHoard !=null){
                    txt.blankLine();
                    txt.line("There is a treasure hoard here.");
                }

                if(result.look.exits !=null && result.look.exits.size() >0){
                    txt.blankLine();
                    txt.line("Exits:");
                    txt.blankLine();
                    for (VisibleExit anExit : result.look.exits) {
                        txt.line(String.format("- %s", anExit.direction));
                    }
                }
            }
        }

        if(result.inventory != null){
            if(result.inventory.contents != null & result.inventory.contents.length>0) {
                txt.blankLine();
                txt.line("You are carrying:");
                txt.blankLine();
                for (IdDescriptionPair anItem : result.inventory.contents) {
                    txt.line(String.format("- %s (%s)", anItem.description, anItem.id));
                }
            }
        }

        if(result.inspectReport!=null){
            txt.blankLine();
            txt.line("You Inspected:");
            txt.blankLine();
            txt.line("- " + result.inspectReport.collectableId);
            txt.line("- " + result.inspectReport.collectableDescription);
            txt.line("- Inspect cost: " + result.inspectReport.costOfInspection);
            txt.line("- Ability Power: " + result.inspectReport.collectableAbilityPower);
            txt.line("- Score on Hoarding: " + result.inspectReport.collectableHoardableScore);
            txt.line("- Is Hoardable: " + result.inspectReport.collectableIsHoardable);
            txt.line("- Current Score: " + result.inspectReport.playerScoreIsNow);
            txt.line("- Provides Ability: " + result.inspectReport.collectableProvidesAbility);
        }

        // output to the console
        System.out.println(txt.toString());
        if(createTextLogFile) {
            textLogFile.append("\n");
            textLogFile.append(txt.toString());
        }

    }



    public void reportCommand(String inputLine) {
        Txt txt = new Txt();

        txt.blankLine();
        String timeat = timestamp();
        if(this.verboseOutput){
            txt.line("> _TimeStamp: " + timeat +"_");
        }
        txt.line(String.format("> %s", inputLine));

        System.out.println(txt.toString());

        if(createTextLogFile){
            textLogFile.append("\n");
            textLogFile.append(txt.toString());
        }
        if(createJsonLogFile){
            jsonLogFile.append("\n");
            jsonLogFile.append(String.format("{\"input\":%s, \"timestamp\":%s}", new Gson().toJson(inputLine), new Gson().toJson(timeat)));
        }
    }

    private String timestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }


}
