package uk.co.compendiumdev.restmud.games.walkthroughs;


import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;
import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restmud.testing.walkthrough.AbstractWalkthroughTest;

import java.nio.file.Paths;

public class ExampleDocumentedGameWalkthroughTest extends AbstractWalkthroughTest {
    
    // TODO this is a test map and must always pass and it should cover all conditions possible for game creation

    @Test
    public void walkthrough(){

        setupTheGame("exampleDocumentedGame.json");
        
        dsl.startWalkthrough();
        dsl.walkthroughStep("");
        dsl.walkthroughStep("# Walkthrough and Hints for example_game");


        outputWalkthroughWarning();


        dsl.walkthroughStep("\n## Hints\n");
        dsl.walkthroughStep("* use the corridorbutton");
        dsl.walkthroughStep("* hoard treasure not junk");
        dsl.walkthroughStep("* the zapherebutton zaps the same treasure to the location");
        dsl.walkthroughStep("* this is a static map so is easy to automate");
        dsl.walkthroughStep("* this is a good map to switch off the GUI and just use the api");


        dsl.walkthroughStep("\n## Map\n");
        dsl.walkthroughStep("\n![](exampleDocumentedGame.png)\n");

        dsl.walkthroughStep("\n## Walkthrough\n");

        ResultOutput result = walkthrough("we start in room 1",
                                "look", "");
        Assert.assertEquals("1", result.look.location.locationId);

        result = walkthrough("north leads into room 2",
                            "go", "n");
        Assert.assertEquals("2", result.look.location.locationId);
        walkthrough("", "look", "");

        result = walkthrough("nothing here, we can go back to room 1",
                    "go", "s");
        Assert.assertEquals("1", result.look.location.locationId);
        walkthrough("", "look", "");

        result = walkthrough("we can go east into room 3",
                                "go", "e");
        Assert.assertEquals("3", result.look.location.locationId);
        walkthrough("", "look", "");

        result = walkthrough("game says we can go south, but we can't really",
                "go", "s");
        Assert.assertEquals("3", result.look.location.locationId);

        result = walkthrough("we have to go west back to the start room",
                "go", "w");
        Assert.assertEquals("1", result.look.location.locationId);
        walkthrough("", "look", "");


        result = walkthrough("we can go west into room 5",
                "go", "w");
        Assert.assertEquals("5", result.look.location.locationId);

        result = walkthrough("oooh a button, use the button to start the teleporter in the south",
                    "look", "");
        result = walkthrough("",
                "examine", "teleporterbutton");
        result = walkthrough("use the button to start the teleporter in the south",
                "use", "teleporterbutton");

        result = walkthrough("go south through the teleporter to go back to the start",
                        "go", "s");
        Assert.assertEquals("1", result.look.location.locationId);
        walkthrough("", "look", "");

        result = walkthrough("go west to get back to the telerporter room",
                    "go", "w");
        Assert.assertEquals("5", result.look.location.locationId);
        walkthrough("", "look", "");

        result = walkthrough("but the teleporter to the south is not on, we would have to push the button again if we want to go south",
                    "go", "s");
        Assert.assertEquals("5", result.look.location.locationId);

        // I can just walk back to the start room by going east
        result = walkthrough("I can just walk back to the start room by going east",
                        "go", "e");
        Assert.assertEquals("1", result.look.location.locationId);
        walkthrough("", "look", "");

        dsl.walkthroughStep("\nNow I will experiment with the gates.\n");

        result = walkthrough("I can't go south because the gate is closed",
                "go", "s");
        Assert.assertEquals("1", result.look.location.locationId);

        result = walkthrough("I better open the gate to the south",
                "open", "s");
        Assert.assertEquals("1", result.look.location.locationId);

        result = walkthrough("Then I can go through",
                "go", "s");
        Assert.assertEquals("4", result.look.location.locationId);
        walkthrough("", "look", "");

        result = walkthrough("If I close the gate I can't go back, until I open it again",
                "close", "n");
        Assert.assertEquals("4", result.look.location.locationId);
        result = walkthrough("",
                "go", "n");
        Assert.assertEquals("4", result.look.location.locationId);

        result = walkthrough("There is a gate to the east",
                "go", "e");
        Assert.assertEquals("4", result.look.location.locationId);


        result = walkthrough("I have to open it first",
                "open", "e");
        Assert.assertEquals("4", result.look.location.locationId);
        result = walkthrough("",
                "go", "e");
        Assert.assertEquals("6", result.look.location.locationId);
        walkthrough("", "look", "");

        result = walkthrough("It is a one way gate so I can't close it from this side and it doesn't stop me going west",
                "close", "w");

        result = walkthrough("I need to go back to the start room to get to the next part of the map",
                "go", "w");
        Assert.assertEquals("4", result.look.location.locationId);
        walkthrough("", "look", "");


        result = walkthrough("",
                "open", "n");
        Assert.assertEquals("4", result.look.location.locationId);
        result = walkthrough("",
                "go", "n");
        Assert.assertEquals("1", result.look.location.locationId);
        walkthrough("", "look", "");


        result = walkthrough("I can use the corridorbutton to teleport to the next part of the map",
                        "look", "");
        Assert.assertEquals("1", result.look.location.locationId);
        result = walkthrough("",
                "examine", "corridorbutton");
        result = walkthrough("",
                "use", "corridorbutton");
        Assert.assertEquals("corridor", result.look.location.locationId);
        walkthrough("", "look", "");


        result = walkthrough("Even though the location says I can go east, that location does not exist",
                "go", "e");
        Assert.assertEquals("corridor", result.look.location.locationId);

        result = walkthrough("I guess I have to go south",
                "go", "s");
        Assert.assertEquals("8", result.look.location.locationId);
        walkthrough("", "look", "");



        result = walkthrough("My score is zero so I don't really want to pick up the cursed item",
                "score", "");
        Assert.assertEquals(0,result.users.get(0).score.intValue());

        result = walkthrough("I will go east to a treasure hoard room",
                "go", "e");
        Assert.assertEquals("9", result.look.location.locationId);
        walkthrough("", "look", "");

        result = walkthrough("I will take the shiny treasure that can be hoarded, and hoard it to increase my score",
                "take", "treasure1");
        result = walkthrough("",
                "hoard", "treasure1");
        result = walkthrough("I will take the shiny treasure that can be hoarded, and hoard it to increase my score",
                "score", "");

        int currentScore = result.users.get(0).score.intValue();
        Assert.assertTrue(currentScore>0);

        result = walkthrough("I can take the other treasure, but cannot hoard it, so my score does not increase",
                "take", "junk1");
        result = walkthrough("",
                "hoard", "junk1");
        result = walkthrough("",
                "score", "");
        Assert.assertEquals(currentScore, result.users.get(0).score.intValue());

        result = walkthrough("I am carrying junk now",
                "inventory", "");
        Assert.assertEquals(1,result.inventory.contents.length);

        result = walkthrough("I may as well just drop that junk",
                "drop", "junk1");
        result = walkthrough("",
                "inventory", "");
        Assert.assertEquals(0,result.inventory.contents.length);

        result = walkthrough("I will go west and continue to explore to the south",
                "go", "w");
        Assert.assertEquals("8", result.look.location.locationId);
        walkthrough("", "look", "");
        result = walkthrough("",
                "go", "s");
        Assert.assertEquals("10", result.look.location.locationId);
        walkthrough("", "look", "");

        result = walkthrough("That button looks important",
                "examine", "zapherebutton");
        result = walkthrough("I can use the button to bring a treasure item here",
                "use", "zapherebutton");
        result = walkthrough("I will now take the treasure","take","treasure2");

        //for(IdDescriptionPair item : result.look.collectables){
        //    walkthrough("", "take", item.id);
        //}

        result = walkthrough("There must be a hoard nearby - try to the east",
                "go", "e");
        walkthrough("", "look", "");
        result = walkthrough("", "inventory", "");
        for(IdDescriptionPair hoardThis : result.inventory.contents){
            walkthrough("", "hoard", hoardThis.id);
        }
        result = walkthrough("If I keep this up I'll get a super high score",
                "score", "");
        Assert.assertTrue(currentScore < result.users.get(0).score.intValue());
        currentScore = result.users.get(0).score.intValue();

        result = walkthrough("I could to that all day hoarding the same treasure",
                "go", "w");
        Assert.assertEquals("10", result.look.location.locationId);
        walkthrough("", "look", "");
        result = walkthrough("", "go", "w");
        Assert.assertEquals("21", result.look.location.locationId);
        walkthrough("", "look", "");
        result = walkthrough("I can smell a secret exit to the south",
                "go", "s");
        Assert.assertEquals("10", result.look.location.locationId);

        result = walkthrough("Let us explore more",
                "go", "s");
        Assert.assertEquals("12", result.look.location.locationId);
        walkthrough("", "look", "");

        result = walkthrough("",
                "go", "e");
        Assert.assertEquals("13", result.look.location.locationId);
        walkthrough("", "look", "");

        result = walkthrough("I need a score to inspect things to learn more about them",
                "score", "");
        Assert.assertTrue( result.users.get(0).score.intValue() > 0);

        result = walkthrough("I can inspect things I am not carrying",
                "inspect", "inspectability1");
        successfully(result);
        result = walkthrough("",
                "take", "inspecthoardable1");
        result = walkthrough("",
                "inspect", "inspecthoardable1");
        successfully(result);


        result = walkthrough("I will find things I can examine",
                "go", "w");
        result = walkthrough("",
                "go", "s");
        Assert.assertEquals("14", result.look.location.locationId);
        walkthrough("", "look", "");
        result = walkthrough("",
                "go", "e");
        Assert.assertEquals("15", result.look.location.locationId);
        walkthrough("", "look", "");
        successfully(walkthrough("",
                "examine", "examineme"));

        result = walkthrough("There is a dark room around here somewhere",
                "go", "w");
        result = walkthrough("",
                "go", "s");
        Assert.assertEquals("16", result.look.location.locationId);
        walkthrough("", "look", "");
        result = walkthrough("",
                "go", "e");
        Assert.assertEquals("17", result.look.location.locationId);
        walkthrough("I think I found it", "look", "");
        result = walkthrough("",
                "go", "w");
        walkthrough("I can illuminate when I have a torch, but it runs out", "take", "atorchonthefloor");
        result = walkthrough("",
                "illuminate", "");
        result = walkthrough("",
                "go", "e");
        Assert.assertEquals("17", result.look.location.locationId);
        walkthrough("", "look", "");
        successfully(walkthrough("I can only take things when I can see ",
                            "take", "aThingInTheDark"));


        result = walkthrough("It is possible to polish things that are hoardable and make them even more valuable",
                "go", "w");
        successfully(walkthrough("", "look", ""));
        result = walkthrough("",
                "go", "s");
        Assert.assertEquals("18", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));
        successfully(walkthrough("", "take", "athingtopolish"));
        result = walkthrough("",
                "go", "e");
        Assert.assertEquals("19", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));
        successfully(walkthrough("", "take", "athingtopolishwith"));
        successfully(walkthrough("", "polish", "athingtopolish"));


        result = walkthrough("I remember reading about some dispensers of cool stuff",
                "go", "w");
        Assert.assertEquals("18", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));
        result = walkthrough("", "go", "n");
        Assert.assertEquals("16", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));
        result = walkthrough("", "go", "n");
        Assert.assertEquals("14", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));
        result = walkthrough("", "go", "w");
        Assert.assertEquals("23", result.look.location.locationId);
        successfully(walkthrough("Ah, here they are", "look", ""));


        result = walkthrough("I can use the dispensers for infinite stuff",
                "use", "golddispenser");
        successfully(walkthrough("", "use", "golddispenser"));
        successfully(walkthrough("", "use", "golddispenser"));
        successfully(walkthrough("", "use", "golddispenser"));
        successfully(walkthrough("", "use", "golddispenser"));
        successfully(walkthrough("", "use", "golddispenser"));
        successfully(walkthrough("", "use", "golddispenser"));

        result = walkthrough("I will now take all the gold", "look", "");

        for(IdDescriptionPair item : result.look.collectables){
            walkthrough("", "take", item.id);
        }


        result = walkthrough("I heard tell of a room with buttons where I can increase my score",
                "go", "e");
        Assert.assertEquals("14", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));
        result = walkthrough("", "go", "n");
        Assert.assertEquals("12", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));
        result = walkthrough("", "go", "n");
        Assert.assertEquals("10", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));
        result = walkthrough("", "go", "n");
        Assert.assertEquals("8", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));
        result = walkthrough("", "go", "w");
        successfully(walkthrough("Ah, here it is", "look", ""));
        Assert.assertEquals("20", result.look.location.locationId);

        result = walkthrough("",
                "score", "");
        currentScore = result.users.get(0).score.intValue();

        result = walkthrough("Apparently red is up and blue is down",
                "examine", "acounter");
        result = walkthrough("",
                "push", "redbutton");
        result = walkthrough("",
                "push", "redbutton");
        result = walkthrough("",
                "push", "redbutton");

        result = walkthrough("When I hear a ding my score has increased",
                "score", "");
        Assert.assertTrue(currentScore < result.users.get(0).score.intValue());
        currentScore = result.users.get(0).score.intValue();

        dsl.walkthroughStep("Could I probably push the blue button and possibly score again?");


        result = walkthrough("But there is a room where I do stuff and things are revealed",
                "go", "e");
        Assert.assertEquals("8", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));
        result = walkthrough("", "go", "s");
        Assert.assertEquals("10", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));
        result = walkthrough("", "go", "s");
        Assert.assertEquals("12", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));
        result = walkthrough("", "go", "w");
        Assert.assertEquals("22", result.look.location.locationId);
        successfully(walkthrough("Here is the room that shows things, but I am locked in", "look", ""));
        successfully(walkthrough("", "examine", "asignonwall"));
        successfully(walkthrough("", "say", "boo"));
        successfully(walkthrough("", "open", "e"));

        result = walkthrough("But there is a room where I do stuff and things are revealed",
                "go", "e");
        Assert.assertEquals("12", result.look.location.locationId);
        successfully(walkthrough("", "look", ""));

        dsl.walkthroughStep("And with that I have done most of the things. I could keep dispensing and increasing score, but that is really it.");


        dsl.walkthroughStep("");
        dsl.walkthroughStep("---");
        dsl.outputWalkthrough();

        dsl.writeWalkthroughToFile(Paths.get(System.getProperty("user.dir"), "docs","example_game_walkthrough.md").toString());
        dsl.writeCommandListToFile(Paths.get(System.getProperty("user.dir"), "docs","example_game_walkthrough_commands.txt").toString());

    }



}
