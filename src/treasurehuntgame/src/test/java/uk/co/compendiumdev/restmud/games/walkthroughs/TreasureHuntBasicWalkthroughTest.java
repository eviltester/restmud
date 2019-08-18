package uk.co.compendiumdev.restmud.games.walkthroughs;


import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.TreasureHuntBasicGameDefinitionGenerator;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;
import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restmud.testing.walkthrough.AbstractWalkthroughTest;

import java.nio.file.Paths;

public class TreasureHuntBasicWalkthroughTest extends AbstractWalkthroughTest {
    
    
    @Test
    public void walkthrough(){

        GameInitializer theGameInit = new GameInitializer();
        MudGame game = theGameInit.getGame();
        MudGameDefinition defn = MudGameDefinition.create(game.getUserInputParser());
        new TreasureHuntBasicGameDefinitionGenerator().define(defn);

        setupTheGame(defn);
                
        dsl.startWalkthrough();
        dsl.walkthroughStep("");
        dsl.walkthroughStep("# Walkthrough and Hints for Treasure Hunt Multi User Game");


        outputWalkthroughWarning();

        dsl.walkthroughStep("\n## Hints\n");
        dsl.walkthroughStep("* read signs");


        dsl.walkthroughStep("\n## Walkthrough\n");

        successfullyVisitRoom("1", walkthrough("we start in room 1", "look", ""));
//        successfully(walkthrough("I might want to open this", "examine", "very_strong_padlock"));
//        failTo(walkthrough("I definitely want to open the padlock", "go", "n"));


        dsl.walkthroughStep("\n### The Bomb Puzzle\n");

        successfully(walkthrough("", "score", ""));
        successfullyVisitRoom("2", walkthrough("I will solve the bomb puzzle", "go", "n"));
        successfully(walkthrough("", "examine", "smallroundthing"));
        successfully(walkthrough("", "take", "smallroundthing"));
        successfully(walkthrough("I could just wait, but I'll get points if I defuse it", "defuse", "smallroundthing"));
        ResultOutput result = successfully(walkthrough("", "score", ""));
        int score = result.users.get(0).score.intValue();
        Assert.assertEquals(100,score);


        dsl.walkthroughStep("\n### The Broken Transporter Puzzle\n");

        successfullyVisitRoom("1", walkthrough("I will solve the broken transporter puzzle", "go", "s"));
        successfullyVisitRoom("4", walkthrough("", "go", "s"));
        successfullyVisitRoom("4", walkthrough("Yeah, it isn't a puzzle, it's just broken", "go", "s"));


        dsl.walkthroughStep("\n### The Loose Stone Puzzle\n");

        successfullyVisitRoom("1", walkthrough("I will solve the loose stone puzzle", "go", "n"));
        successfullyVisitRoom("5", walkthrough("", "go", "w"));
        successfullyVisitRoom("6", walkthrough("", "go", "w"));
        failTo(walkthrough("I can't take the stone", "take", "loosestone"));
        successfully(walkthrough("But I can interact with it", "examine", "loosestone"));
        successfully(walkthrough("Seems unusual but, I'll try it", "lick", "loosestone"));
        result = successfully(walkthrough("Only lick it once", "score", ""));
        score = result.users.get(0).score.intValue();
        Assert.assertEquals(score,110);


        // Jump to puzzle room test
        dsl.walkthroughStep("\n### The Treasure Vase Puzzle\n");
        successfullyVisitRoom("5", walkthrough("", "go", "e"));
        successfullyVisitRoom("1", walkthrough("", "go", "e"));
        successfullyVisitRoom("3", walkthrough("", "go", "e"));
        successfully(walkthrough("", "examine", "puzzlelandsign"));
        successfully(walkthrough("", "say", "puzzle"));
        result = successfully(walkthrough("Entering puzzland is a puzzle", "score", ""));
        int newscore = result.users.get(0).score.intValue();
        Assert.assertEquals(newscore,score + 100);
        score = newscore;

        successfullyVisitRoom("18", walkthrough("", "go", "s"));
        successfullyVisitRoom("19", walkthrough("", "go", "s"));
        successfullyVisitRoom("20", walkthrough("", "go", "s"));
        successfullyVisitRoom("21", walkthrough("This is a simple monkey puzzle", "go", "s"));
        successfully(walkthrough("","take", "prizevase"));
        successfullyVisitRoom("20", walkthrough("","go", "n"));
        successfully(walkthrough("","use", "hammer"));
        result = successfully(walkthrough("Solved Puzzle, Get Score", "score", ""));

        newscore = result.users.get(0).score.intValue();
        Assert.assertTrue(newscore>score);
        Assert.assertEquals(newscore,score + 100);


        successfullyVisitRoom("19", walkthrough("","go", "n"));
        successfullyVisitRoom("18", walkthrough("","go", "n"));
        successfullyVisitRoom("3", walkthrough("","go", "n"));
        successfullyVisitRoom("1", walkthrough("","go", "w"));

        dsl.walkthroughStep("\n### The Zombie Graveyard\n");

        successfullyVisitRoom("5", walkthrough("","go", "w"));
        successfully(walkthrough("It looks like there is a room through there","examine", "rustedshutdoor"));
        dsl.walkthroughStep("\nI need to find a way in!\n");
        successfullyVisitRoom("1", walkthrough("","go", "e"));
        successfullyVisitRoom("3", walkthrough("","go", "e"));
        successfully(walkthrough("Better Open The door!","open", "e"));
        successfullyVisitRoom("11", walkthrough("","go", "e"));

        successfully(walkthrough("It looks like there is a clue on the sign","examine", "zombiemazesign"));
        successfully(walkthrough("","say", "brains"));
        successfullyVisitRoom("24", walkthrough("Where Am I?","look", ""));
        successfullyVisitRoom("27", walkthrough("","go", "s"));
        successfullyVisitRoom("26", walkthrough("","go", "w"));
        successfully(walkthrough("Is this the tomb of The Money God?","examine", "tomb"));
        successfully(walkthrough("Time for some magic","say", "moneygod"));

        successfully(walkthrough("I better find a way to protect myself from zombies","look", ""));
        successfullyVisitRoom("29", walkthrough("","go", "s"));
        successfullyVisitRoom("30", walkthrough("","go", "s"));
        successfully(walkthrough("","examine", "axeindeadbody"));
        successfully(walkthrough("","say", "groovy"));
        successfullyVisitRoom("25", walkthrough("","look", ""));
        successfully(walkthrough("Zombies hate loud noises","use", "voodoodrum"));
        successfullyVisitRoom("30", walkthrough("","go", "w"));
        successfullyVisitRoom("24", walkthrough("","go", "s"));

        successfully(walkthrough("How do I get out?","examine", "dirtyrock"));
        successfully(walkthrough("I will leave this place now","say", "sniarb"));
        successfullyVisitRoom("2", walkthrough("","look", ""));


        dsl.walkthroughStep("\n### The Star Shaped Rooms\n");
        successfullyVisitRoom("1", walkthrough("","go", "s"));
        successfullyVisitRoom("5", walkthrough("","go", "w"));
        successfullyVisitRoom("6", walkthrough("","go", "w"));
        successfullyVisitRoom("7", walkthrough("","go", "w"));

        successfully(walkthrough("Should I push the button?","examine", "starshapedbutton"));

        successfullyVisitRoom("8", walkthrough("I will leave that for now","go", "w"));
        successfullyVisitRoom("9", walkthrough("I will leave that for now","go", "w"));
        successfully(walkthrough("","examine", "starshapedsign"));

        successfullyVisitRoom("10", walkthrough("I will push that button","go", "w"));
        successfullyVisitRoom("11", walkthrough("","go", "w"));
        successfullyVisitRoom("3", walkthrough("","go", "w"));
        successfullyVisitRoom("1", walkthrough("","go", "w"));
        successfullyVisitRoom("5", walkthrough("","go", "w"));
        successfullyVisitRoom("6", walkthrough("","go", "w"));
        successfullyVisitRoom("7", walkthrough("","go", "w"));

        successfullyVisitRoom("31", walkthrough("I will use that button","use", "starshapedbutton"));
        successfullyVisitRoom("32", walkthrough("","go", "n"));
        successfullyVisitRoom("33", walkthrough("","go", "n"));
        
        successfully(walkthrough("Shiny Treasure","take", "shinystar"));
        successfullyVisitRoom("32", walkthrough("","go", "s"));
        successfully(walkthrough("","hoard", "shinystar"));
        successfullyVisitRoom("33", walkthrough("","go", "n"));
        successfully(walkthrough("I wonder if there is any more treasure","examine", "starsign"));
        successfully(walkthrough("","use", "starsign"));
        successfully(walkthrough("I am sure that if I kept using it I would get lucky again","use", "starsign"));















        dsl.walkthroughStep("That is about all I can do. So I'll stop now. Or is it!");

        // if I kept using the leverbutton and going down the corridor to the east
        // then I would get 10 points for each time I go through the teleporter to the east


        dsl.walkthroughStep("\n## Map\n");
        dsl.walkthroughStep("\n![](treasureHuntBasicGame.png)\n");


        dsl.walkthroughStep("---");
        dsl.outputWalkthrough();

        dsl.writeWalkthroughToFile(Paths.get(System.getProperty("user.dir"), "docs","treasure_hunt_basic_walkthrough.md").toString());
        dsl.writeCommandListToFile(Paths.get(System.getProperty("user.dir"), "docs","treasure_hunt_basic_walkthrough_commands.txt").toString());

    }





    }
