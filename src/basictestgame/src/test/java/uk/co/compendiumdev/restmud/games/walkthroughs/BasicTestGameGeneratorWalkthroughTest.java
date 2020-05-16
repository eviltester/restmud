package uk.co.compendiumdev.restmud.games.walkthroughs;


import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;
import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restmud.testing.walkthrough.AbstractWalkthroughTest;

import java.nio.file.Paths;

public class BasicTestGameGeneratorWalkthroughTest extends AbstractWalkthroughTest {
    
    
    @Test
    public void walkthrough(){

        setupTheGame("basicTestGame.json");
                
        dsl.startWalkthrough();
        dsl.walkthroughStep("");
        dsl.walkthroughStep("# Walkthrough and Hints for test_game_basic");

        outputWalkthroughWarning();


        dsl.walkthroughStep("\n* for more information on the game visit [The Official RestMud HomePage](http://compendiumdev.co.uk/page/restmud)\n");

        dsl.walkthroughStep("\n## Hints\n");
        dsl.walkthroughStep("* examine all signs");
        dsl.walkthroughStep("* look in description html for 'spans' that are examinable");
        dsl.walkthroughStep("* if you take something that isn't yours then confess quickly");
        dsl.walkthroughStep("* use things with ids");
        dsl.walkthroughStep("* map the maze");
        dsl.walkthroughStep("* if the game says to do something then do it e.g. 'wobble' or 'confess'");
        dsl.walkthroughStep("* secret ways often mean points");
        dsl.walkthroughStep("* some secret ways never run out of points");
        dsl.walkthroughStep("* polish before hoarding");


        dsl.walkthroughStep("\n## Walkthrough\n");

        successfullyVisitRoom("1", walkthrough("we start in room 1",
                                "look", ""));


        successfully(walkthrough("I always examine signs on walls", "examine", "ahint"));

        dsl.walkthroughStep("\n## Room 2 is dark\n");
        successfullyVisitRoom("2", walkthrough("north leads into room 2",
                            "go", "n"));
        walkthrough("oh oh, it is dark here", "look", "");
        walkthrough("amend the url to go back south /go/s", "go", "s");
        successfullyVisitRoom("1", walkthrough("to get back to room 1", "look", ""));

        dsl.walkthroughStep("\n## Room 3 is more than it seems - so come back later\n");
        successfullyVisitRoom("3", walkthrough("east leads into room 3 - east room",
                "go", "e"));

        walkthrough("not much to do here at the moment", "look", "");
        failTo(walkthrough("I can't go anywhere yet.", "go", "e"));
        walkthrough("I'll examine that sign though", "examine", "mazesign");
        dsl.walkthroughStep("I'm not ready to use the mazesign yet, I'll do that later");
        successfullyVisitRoom("1", walkthrough("I will come back here later", "go", "w"));
        successfully(walkthrough("", "look", ""));


        dsl.walkthroughStep("\n## Room 4 has a door\n");
        successfullyVisitRoom("4",walkthrough("", "go", "s"));
        failTo(walkthrough("I need to open the door before I go east", "go", "e"));
        successfully(walkthrough("", "open", "e"));
        successfullyVisitRoom("6", walkthrough("", "go", "e"));
        successfully(walkthrough("", "look", ""));
        successfullyVisitRoom("4",walkthrough("Hmm, can't go east no more", "go", "w"));
        successfully(walkthrough("", "look", ""));
        successfully(walkthrough("I wonder what the sign says", "examine", "adirsign"));
        successfullyVisitRoom("1", walkthrough("I'll come back later then", "go", "n"));

        dsl.walkthroughStep("\n## Room 5 is a hoard room\n");
        successfully(walkthrough("", "look", ""));
        successfullyVisitRoom("5", walkthrough("west leads into room 5 - hoard room",
                "go", "w"));
        successfully(walkthrough("I do not have anything to hoard though.", "look", ""));
        successfully(walkthrough("I'll examine that sign though", "examine", "hintsigne"));
        walkthrough("Hmm, let me visit room 3 again then", "go", "e");
        successfullyVisitRoom("3", walkthrough("", "go", "e"));


        dsl.walkthroughStep("\n## Room 3 is so much more than it seemed now\n");
        successfully(walkthrough("but I can't see anything", "look", ""));
        successfullyVisitRoom("7", walkthrough("I will trust the sign", "go", "e"));

        dsl.walkthroughStep("\n## Oooh, a secret room\n");
        successfully(walkthrough("", "look", ""));
        failTo(walkthrough("I can't wobble levers", "wobble", "alever"));
        successfully(walkthrough("Examine everything", "examine", "alever"));
        successfully(walkthrough("Examine everything", "examine", "leverbutton"));
        successfully(walkthrough("Examine everything", "use", "leverbutton"));
        successfully(walkthrough("Not sure what opened where, but I'll take the torch just in case", "take", "torch_1"));
        successfullyVisitRoom("3", walkthrough("I think I'm done here", "go", "w"));
        successfullyVisitRoom("1", walkthrough("I think I'm done here", "go", "w"));
        successfullyVisitRoom("4", walkthrough("I think I'm done here", "go", "s"));
        successfullyVisitRoom("6",walkthrough("I already opened this door", "go", "e"));
        successfully(walkthrough("", "look", ""));

        dsl.walkthroughStep("\n## The lever button opened a secret door\n");
        ResultOutput result = walkthrough("I wonder how my score is doing",  "score", "");
        int currentScore = result.users.get(0).score.intValue();
        successfullyVisitRoom("8", walkthrough("", "go", "e"));
        successfully(walkthrough("", "look", ""));
        successfully(walkthrough("", "examine", "wobblyswitch"));
        successfully(walkthrough("It says wobble it, I'll 'wobble' it", "wobble", "wobblyswitch"));
        result = walkthrough("My score should be more",  "score", "");
        Assert.assertTrue(currentScore<result.users.get(0).score.intValue());
        currentScore = result.users.get(0).score.intValue();
        successfullyVisitRoom("9", walkthrough("I'll be on my way then", "go", "e"));


        dsl.walkthroughStep("\n## Elves don't like people taking their stuff\n");
        successfully(walkthrough("", "look", ""));
        successfully(walkthrough("", "examine", "atinypipe"));
        successfully(walkthrough("Oooh, I'll feel guilty but I'll take it", "take", "atinypipe"));
        successfullyVisitRoom("10", walkthrough("I'll wander about for a while", "go", "e"));  //10
        // if I wander about then I Get bashed and cannot confess
       // successfullyVisitRoom("9", walkthrough("", "go", "w")); //9
        //successfullyVisitRoom("10", walkthrough("", "go", "e")); //8
        //successfullyVisitRoom("9", walkthrough("", "go", "w")); //7
        //successfullyVisitRoom("10", walkthrough("", "go", "e")); //6
        //successfullyVisitRoom("9", walkthrough("", "go", "w")); //5
        //successfullyVisitRoom("10", walkthrough("", "go", "e")); //4
        //successfullyVisitRoom("9", walkthrough("", "go", "w")); //3
        //successfullyVisitRoom("10", walkthrough("", "go", "e")); //2
        //result = walkthrough("Ow, that hurt",  "score", "");    // 1
        //Assert.assertTrue(currentScore>result.users.get(0).score.intValue());
        //currentScore = result.users.get(0).score.intValue();

        dsl.walkthroughStep("\n## I wish I could be forgiven \n");
        successfully(walkthrough("", "look", ""));
        successfully(walkthrough("This looks promising", "examine", "asinsign"));
        successfully(walkthrough("This looks promising", "confess", "atinypipe"));
        result = walkthrough("Who says crime doesn't pay - bwahahahah!",  "score", "");
        Assert.assertTrue(currentScore<result.users.get(0).score.intValue());
        currentScore = result.users.get(0).score.intValue();


        dsl.walkthroughStep("\n## I can go no further east - or can I ?\n");
        successfullyVisitRoom("11", walkthrough("", "go", "e"));
        successfully(walkthrough("Too dark too see though", "look", ""));
        successfully(walkthrough("I should light my torch", "illuminate", ""));
        successfully(walkthrough("I can see now", "look", ""));
        successfully(walkthrough("The sign said I could go east", "go", "e"));
        result = walkthrough("Not sure what happened there",  "score", "");
        Assert.assertTrue(currentScore<result.users.get(0).score.intValue());
        currentScore = result.users.get(0).score.intValue();
        successfully(walkthrough("I will save my torch until I need it again", "darken", ""));
        successfullyVisitRoom("4", walkthrough("That must have been a teleporter", "look", "")); //2


        dsl.walkthroughStep("\n## I am ready for the maze\n");
        successfullyVisitRoom("1", walkthrough("", "go", "n"));
        successfullyVisitRoom("3", walkthrough("", "go", "e"));
        successfully(walkthrough("", "use", "mazelever"));
        successfullyVisitRoom("14", walkthrough("", "look", ""));
        successfullyVisitRoom("17", walkthrough("", "go", "w"));
        successfully(walkthrough("too dark", "look", ""));
        successfully(walkthrough("I should light my torch", "illuminate", ""));
        successfully(walkthrough("And see what I can see", "look", ""));
        successfully(walkthrough("", "use", "amazelever17"));
        // Let torch run out and get one from the dispenser later
        //successfully(walkthrough("I should save my torch", "darken", ""));
        successfullyVisitRoom("18", walkthrough("", "look", ""));
        successfully(walkthrough("Oooh a sign", "examine", "endmazebuttonsign"));
        successfully(walkthrough("Oooh a button", "examine", "endmazebutton"));
        successfullyVisitRoom("20", walkthrough("", "go", "e"));
        successfully(walkthrough("And see what I can see", "take", "the_secret_of_the_maze_prize"));
        successfullyVisitRoom("18", walkthrough("", "go", "w"));
        successfully(walkthrough("", "use", "endmazebutton"));
        successfullyVisitRoom("3", walkthrough("", "look", ""));


        dsl.walkthroughStep("\n## I should hoard my treasure\n");
        successfullyVisitRoom("1", walkthrough("", "go", "w"));
        successfullyVisitRoom("5", walkthrough("", "go", "w"));
        successfullyVisitRoom("12", walkthrough("", "go", "s"));
        successfully(walkthrough("I can get a new torch", "use", "torchdispenser"));
        successfully(walkthrough("But torch isn't treasure", "take", "torch_2"));
        successfullyVisitRoom("5", walkthrough("", "go", "n"));
        successfullyVisitRoom("1", walkthrough("", "go", "e"));
        successfullyVisitRoom("2", walkthrough("", "go", "n"));
        successfully(walkthrough("I should light my torch and see", "illuminate", ""));
        successfully(walkthrough("", "look", ""));
        successfully(walkthrough("", "take", "cloth_1"));
        successfullyVisitRoom("1", walkthrough("", "go", "s"));
        successfullyVisitRoom("5", walkthrough("", "go", "w"));
        successfully(walkthrough("", "polish", "the_secret_of_the_maze_prize"));
        while(walkthrough("",  "polish", "the_secret_of_the_maze_prize").resultoutput.lastactionstate.contentEquals(LastAction.SUCCESS)){
        }
        successfully(walkthrough("", "hoard", "the_secret_of_the_maze_prize"));
        result = walkthrough("High Score!",  "score", "");
        Assert.assertTrue(currentScore<result.users.get(0).score.intValue());
        currentScore = result.users.get(0).score.intValue();

        dsl.walkthroughStep("\n## There must be more treasure\n");
        successfullyVisitRoom("12", walkthrough("", "go", "s"));
        successfullyVisitRoom("13", walkthrough("This is more like it", "go", "e"));
        successfully(walkthrough("", "take", "shiny_gold_ring"));
        successfullyVisitRoom("12", walkthrough("", "go", "w"));
        successfullyVisitRoom("5", walkthrough("", "go", "n"));
        successfully(walkthrough("", "hoard", "shiny_gold_ring"));

        result = walkthrough("Higher Score!",  "score", "");
        Assert.assertTrue(currentScore<result.users.get(0).score.intValue());
        currentScore = result.users.get(0).score.intValue();

        dsl.walkthroughStep("That is about all I can do. So I'll stop now. Or is it!");

        // if I kept using the leverbutton and going down the corridor to the east
        // then I would get 10 points for each time I go through the teleporter to the east


        dsl.walkthroughStep("\n## Map\n");
        dsl.walkthroughStep("\n![](basicTestGame.png)\n");


        dsl.walkthroughStep("---");
        dsl.outputWalkthrough();

        dsl.writeWalkthroughToFile(Paths.get(System.getProperty("user.dir"), "docs","test_game_basic_walkthrough.md").toString());
        dsl.writeCommandListToFile(Paths.get(System.getProperty("user.dir"), "docs","test_game_basic_walkthrough_commands.txt").toString());

    }





    }
