package uk.co.compendiumdev.restmud.games.walkthroughs;


import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.TechWebTest101GameDefinitionGenerator;
import uk.co.compendiumdev.restmud.testing.walkthrough.AbstractWalkthroughTest;

import java.nio.file.Paths;

public class ZombieWalkthroughTest extends AbstractWalkthroughTest {
    
    
    @Test
    public void walkthrough(){

        GameInitializer theGameInit = new GameInitializer();
        MudGame game = theGameInit.getGame();
        MudGameDefinition defn = MudGameDefinition.create(game.getUserInputParser());
        new TechWebTest101GameDefinitionGenerator().define(defn);

        setupTheGame(defn);
                
        dsl.startWalkthrough();
        dsl.walkthroughStep("");
        dsl.walkthroughStep("# Walkthrough and Hints for Zombie Zork");


        outputWalkthroughWarning();

        dsl.walkthroughStep("\n## Hints\n");
        dsl.walkthroughStep("* the zombies will eventually get you, find something that scares them");


        dsl.walkthroughStep("\n## Walkthrough\n");

        successfullyVisitRoom("1", walkthrough("we start in room 1", "look", ""));
        successfully(walkthrough("I might want to open this", "examine", "very_strong_padlock"));
        failTo(walkthrough("I definitely want to open the padlock", "go", "n"));


        dsl.walkthroughStep("\n### I'll be in the house in a few moments\n");
        successfully(walkthrough("Hello Matt?", "examine", "welcomemat"));
        successfully(walkthrough("I wonder if Matt is under here", "lift", "welcomemat"));
        successfully(walkthrough("I thought I saw something", "look", ""));
        successfully(walkthrough("I did see something", "take", "lockpickwire"));
        successfully(walkthrough("Ha, I'll just pick the lock and be done with it", "use", "lockpickwire"));


        dsl.walkthroughStep("\n### A pleasant walk in the garden\n");
        successfullyVisitRoom("2", walkthrough("", "go", "s"));
        successfully(walkthrough("I thought I saw something", "look", ""));
        successfully(walkthrough("Is it illegal to look through other people's mail?", "examine", "mailbox"));
        successfully(walkthrough("If the police arrive I guess I'll be saved", "open", "mailbox"));
        successfully(walkthrough("Do the police not care about federal crimes?", "examine", "letter"));


        dsl.walkthroughStep("\n### Lost in the maze\n");
        successfullyVisitRoom("6", walkthrough("", "go", "s"));
        successfullyVisitRoom("7", walkthrough("", "go", "e"));
        successfullyVisitRoom("8", walkthrough("", "go", "w"));

        dsl.walkthroughStep("\n### How to scare zombies\n");
        successfully(walkthrough("I thought I saw something", "look", ""));
        successfully(walkthrough("I thought I saw something", "examine", "voodoodrum"));
        successfully(walkthrough("I thought I saw something", "use", "voodoodrum"));

        successfullyVisitRoom("7", walkthrough("", "go", "s"));
        successfullyVisitRoom("6", walkthrough("", "go", "s"));
        successfullyVisitRoom("4", walkthrough("", "go", "s"));

        dsl.walkthroughStep("\n### Explore The Grounds\n");
        successfully(walkthrough("", "look", ""));
        successfully(walkthrough("", "examine", "a_small_wall"));
        successfully(walkthrough("I found a what?", "look", ""));
        successfully(walkthrough("", "take", "frontdoorkey"));

        dsl.walkthroughStep("\n### What is on the other side of the house\n");
        successfullyVisitRoom("5", walkthrough("", "go", "w"));
        successfully(walkthrough("", "look", ""));
        successfully(walkthrough("Oh, so that is what zombies hate is it!", "examine", "scratchedwood"));
        successfullyVisitRoom("1", walkthrough("", "go", "s"));

        dsl.walkthroughStep("\n### The End\n");
        successfully(walkthrough("Save Yourself", "use", "frontdoorkey"));
        successfullyVisitRoom("inhouse",walkthrough("", "look", ""));



        dsl.walkthroughStep("That is about all I can do. So I'll stop now. Or is it!");

        // if I kept using the leverbutton and going down the corridor to the east
        // then I would get 10 points for each time I go through the teleporter to the east


        dsl.walkthroughStep("\n## Map\n");
        dsl.walkthroughStep("\n![](zombieZork.png)\n");


        dsl.walkthroughStep("---");
        dsl.outputWalkthrough();

        dsl.writeWalkthroughToFile(Paths.get(System.getProperty("user.dir"), "docs","zombie_zork_walkthrough.md").toString());
        dsl.writeCommandListToFile(Paths.get(System.getProperty("user.dir"), "docs","zombie_zork_walkthrough_commands.txt").toString());

    }





    }
