package uk.co.compendiumdev.restmud.games;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.ExampleDocumentedGameDefinitionGenerator;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.GameMessages;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;
import uk.co.compendiumdev.restmud.testing.dsl.GameTestDSL;

public class ExampleDocumentedGameTest {

    private static MudGame game;
    private static GameTestDSL dsl;

    /* Example Documented Game is deterministic and well documented - it should be used to automate gameplay paths */

    @BeforeClass
    static public void setupTheGame(){
        GameInitializer theGameInit = new GameInitializer();
        game = theGameInit.getGame();
        MudGameDefinition defn = MudGameDefinition.create(game.getUserInputParser());
        new ExampleDocumentedGameDefinitionGenerator().define(defn);
        game.initFromDefinition(defn);

        // generate does not add any users
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addDefaultUser("The Test User", "tester", "aPassword");

        // set the user back to the central room after each path
        game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1");

        dsl = new GameTestDSL(game);

    }



    @Test
    public void canSeeFirstRoomHasFourExits(){

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1");
        result = doVerb("tester","look", "");

        Assert.assertNotEquals("Look should be set after a look verb", null, result.look);
        Assert.assertEquals("There are four visible exits from room 1", 4, result.look.exits.size());
    }

    @Test
    public void northRoomTwoHasNoVisibleExitsButCanGoSouthBackToOne(){

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1");
        result = doVerb("tester","look", "");

        result = doVerb("tester", "go", "n");

        Assert.assertEquals("We should now be in room 2", "2", result.look.location.locationId);
        Assert.assertEquals("There are no visible exits from room 2", 0, result.look.exits.size());

        result = doVerb("tester", "go", "w");
        Assert.assertEquals(LastAction.FAIL, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should not be able to go any other directions from room 2", "2", result.look.location.locationId);

        result = doVerb("tester", "go", "s");
        Assert.assertEquals(LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should be able to go south through the secret exit", "1", result.look.location.locationId);
    }

    @Test
    public void eastRoomThreeHasAVisibleExitSWhichIsNotCodedFor(){

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1");
        result = doVerb("tester","look", "");

        result = doVerb("tester", "go", "e");

        Assert.assertEquals("We should now be in room 3", "3", result.look.location.locationId);
        Assert.assertEquals("There are two visible exits from room 3", 2, result.look.exits.size());

        result = doVerb("tester", "go", "s");
        Assert.assertEquals(LastAction.FAIL, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should not be be able to go south as it has local processing", "3", result.look.location.locationId);

        result = doVerb("tester", "go", "w");
        Assert.assertEquals(LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should be able to go west back to 1 though", "1", result.look.location.locationId);
    }

    @Test
    public void aGateToTheSouth(){

        ResultOutput result;

        // Room 1 to 4 is blocked by a two way gate defined in terms of room 4

        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1");
        result = doVerb("tester","look", "");

        result = doVerb("tester", "go", "s");
        Assert.assertEquals(LastAction.FAIL, result.resultoutput.lastactionstate);
        Assert.assertEquals("The Gate South should be closed initially", "1", result.look.location.locationId);

        result = doVerb("tester", "open", "s");
        Assert.assertEquals(LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should be able to open the gate", "1", result.look.location.locationId);

        result = doVerb("tester", "go", "s");
        Assert.assertEquals(LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should be able to go through the gate", "4", result.look.location.locationId);

        result = doVerb("tester", "go", "n");
        Assert.assertEquals(LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should be able to go through the gate", "1", result.look.location.locationId);

        result = doVerb("tester", "close", "s");
        Assert.assertEquals(LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should be able to close the gate", "1", result.look.location.locationId);

        result = doVerb("tester", "go", "s");
        Assert.assertEquals(LastAction.FAIL, result.resultoutput.lastactionstate);
        Assert.assertEquals("The Gate South should be closed again", "1", result.look.location.locationId);

        result = doVerb("tester", "open", "s");
        result = doVerb("tester", "go", "s");
        result = doVerb("tester", "close", "n");
        Assert.assertEquals(LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should be able to close the gate", "4", result.look.location.locationId);

        result = doVerb("tester", "go", "n");
        Assert.assertEquals(LastAction.FAIL, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should not be able to go back through the closed gate", "4", result.look.location.locationId);
    }

    @Test
    public void aGateToTheSouthButIDoNotUseThat(){

        ResultOutput result;

        // Room 1 to 4 is blocked by a two way gate defined in terms of room 4

        successfully(game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1"));
        successfully(doVerb("tester","look", ""));

        result = failTo(doVerb("tester", "go", "s"));
        Assert.assertEquals("The Gate South should be closed initially", "1", result.look.location.locationId);

        // can't close it, it is already closed
        result = failTo(doVerb("tester", "close", "s"));
        result = successfully(doVerb("tester", "open", "s"));

        // I just opened it, I can't do that again
        result = failTo(doVerb("tester", "open", "s"));
        result = successfully(doVerb("tester", "close", "s"));

        result = successfully(doVerb("tester", "go", "w"));
        Assert.assertEquals("Go west where there is no gate to the north", "5", result.look.location.locationId);

        // there is no exit to the north
        result = failTo(doVerb("tester", "open", "n"));
        result = failTo(doVerb("tester", "close", "n"));

    }



    @Test
    public void aOneWayGateOnlyGoesOneWAy(){

        ResultOutput result;

        // Room 1 to 4 is blocked by a two way gate defined in terms of room 4
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "4");
        result = doVerb("tester","look", "");
        Assert.assertEquals("The One way gate is defined in room 4 going E to 6", "4", result.look.location.locationId);

        result = doVerb("tester", "go", "3");
        Assert.assertEquals(LastAction.FAIL, result.resultoutput.lastactionstate);
        Assert.assertEquals("The Gate should be closed initially", "4", result.look.location.locationId);

        result = doVerb("tester", "open", "e");
        Assert.assertEquals(LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should be able to open the gate", "4", result.look.location.locationId);

        result = doVerb("tester", "go", "e");
        Assert.assertEquals(LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should be able to go through the open gate", "6", result.look.location.locationId);

        result = doVerb("tester", "close", "w");
        Assert.assertEquals("Location 6 should be be aware of a gate", LastAction.FAIL, result.resultoutput.lastactionstate);

        result = doVerb("tester", "go", "w");
        Assert.assertEquals(LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should be able to go west", "4", result.look.location.locationId);

        result = doVerb( "tester", "go", "e");
        Assert.assertEquals("The gate should still be open",LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("6", result.look.location.locationId);

        result = doVerb("tester", "go", "w");
        Assert.assertEquals("We should be able to go west", "4", result.look.location.locationId);
        result = doVerb("tester", "close", "e");
        Assert.assertEquals("We should be able to close this one way gate",LastAction.SUCCESS, result.resultoutput.lastactionstate);

        result = doVerb( "tester", "go", "e");
        Assert.assertEquals("The closed gate should stop us",LastAction.FAIL, result.resultoutput.lastactionstate);
        Assert.assertEquals("4", result.look.location.locationId);

        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "6");
        result = doVerb("tester","look","");
        Assert.assertEquals("Closed gate should not stop us from room 6", "6", result.look.location.locationId);
        result = doVerb("tester", "go", "w");
        Assert.assertEquals("The closed gate should not stop us",LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("We should be able to go west", "4", result.look.location.locationId);
    }

    @Test
    public void westRoomFiveHasATeleporterImplementedByAHiddenGate() {

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1");
        result = doVerb("tester", "look", "");

        result = doVerb("tester", "go", "w");
        Assert.assertEquals("The closed gate south",LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("teleporter room 5", "5", result.look.location.locationId);

        Assert.assertEquals("should have two exits listed", 2, result.look.exits.size());
        Assert.assertEquals("the gate should be secret", null, result.look.visibleGates);
        // east back into the main room
        result = doVerb("tester", "go", "e");
        Assert.assertEquals("teleporter room e to 1", "1", result.look.location.locationId);
        result = doVerb("tester", "go", "w");
        Assert.assertEquals("teleporter room e to 1", "5", result.look.location.locationId);
        // and south which is blocked
        result = doVerb("tester", "go", "s");
        Assert.assertEquals("The secret gate should stop us",LastAction.FAIL, result.resultoutput.lastactionstate);
        Assert.assertEquals("teleporter room", "5", result.look.location.locationId);

        result = doVerb("tester", "open", "s");
        Assert.assertEquals("The player can not open the teleporter",LastAction.FAIL, result.resultoutput.lastactionstate);

        result = doVerb("tester", "look", "");
        Assert.assertEquals("There should be a button here",1,result.look.visibleObjects.length);
        Assert.assertEquals("A teleporter button","teleporterbutton", result.look.visibleObjects[0].id);

        result = doVerb("tester", "use", "teleporterbutton");
        Assert.assertEquals("Button can be used",LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("A message should exist",1,result.gameMessages.messages.size());
        Assert.assertTrue("A message about pushing button should exist",result.gameMessages.messages.get(0).message.contains("button"));

        result = doVerb("tester", "go", "s");
        Assert.assertEquals("The secret gate should no longer stop us",LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("south means teleport east to room", "1", result.look.location.locationId);
        Assert.assertTrue("The description should mention the teleporter", result.resultoutput.lastactionresult.contains("the teleporter"));
        Assert.assertTrue("The description should mention a screeching noise", result.resultoutput.lastactionresult.contains("screeching noise"));

        result = doVerb("tester", "use", "teleporterbutton");
        Assert.assertEquals("can not use Button anywhere else",LastAction.FAIL, result.resultoutput.lastactionstate);

        result = doVerb("tester", "go", "w");
        Assert.assertEquals("teleporter room e to 1", "5", result.look.location.locationId);
        result = doVerb("tester", "go", "s");
        Assert.assertEquals("The gate should auto close",LastAction.FAIL, result.resultoutput.lastactionstate);
        Assert.assertTrue("The description should mention it is not active teleporter", result.resultoutput.lastactionresult.contains("the teleporter"));
        Assert.assertTrue("The description should mention it is not active teleporter", result.resultoutput.lastactionresult.contains("not active"));
    }

    @Test
    public void buttonsCanTeleportAndLookForced() {

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1");
        result = doVerb("tester", "look", "");

        result = doVerb("tester", "use", "corridorbutton");
        Assert.assertEquals("The button is pushed",LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("taking us to the corridor", "corridor", result.look.location.locationId);
        Assert.assertTrue("we should also have looked", result.look!=null);

        result = doVerb("tester", "use", "startroombutton");
        Assert.assertEquals("The button in the corridor is pushed",LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertTrue("we should not have looked", result.look==null);

        result = doVerb("tester", "look", "");
        Assert.assertEquals("taking us to the start room", "1", result.look.location.locationId);

    }

    @Test
    public void cannotExitToALocationThatDoesNotExist() {

        Assert.assertTrue("Location 7 does not exist", game.getGameLocations().get("7") ==null);

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "corridor");
        result = doVerb("tester", "go", "e");
        Assert.assertEquals("That location does not exist",LastAction.FAIL, result.resultoutput.lastactionstate);

        result = doVerb("tester", "look", "");
        Assert.assertEquals("we are still in the corridor", "corridor", result.look.location.locationId);

    }

    @Test
    public void cannotHoardWhenThereIsNoHoard() {

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "8");
        result = doVerb("tester", "look", "");

        Assert.assertNull("I can not hoard here", result.look.treasureHoard);

        result = doVerb("tester", "take", "cursed2");
        Assert.assertEquals("Take item",LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertFalse(game.getGameLocations().get("8").collectables().contains("cursed2"));

        result = doVerb("tester", "hoard", "cursed2");
        Assert.assertEquals("Can Not Hoard item",LastAction.FAIL, result.resultoutput.lastactionstate);

        MudUser user = game.getUserManager().getUser("tester");
        Assert.assertTrue(user.inventory().contains("cursed2"));

        result = doVerb("tester", "drop", "cursed2");
        Assert.assertEquals("Drop item",LastAction.SUCCESS, result.resultoutput.lastactionstate);

        Assert.assertFalse(user.inventory().contains("cursed2"));
        Assert.assertTrue(game.getGameLocations().get("8").collectables().contains("cursed2"));
    }

    @Test
    public void cannotTakeSomethingThatIsNotHere() {

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "8");
        result = doVerb("tester", "look", "");

        int iAmCarrying = game.getUserManager().getUser("tester").inventory().itemsView().size();

        Assert.assertFalse(game.getGameLocations().get("8").collectables().contains("junk1"));

        result = doVerb("tester", "take", "junk1");
        Assert.assertEquals("Can Not Take item",LastAction.FAIL, result.resultoutput.lastactionstate);

        result = doVerb("tester", "take", "thingThatDoesNotExist999");
        Assert.assertEquals("Can Not Take item that does not exist",LastAction.FAIL, result.resultoutput.lastactionstate);

        Assert.assertEquals("Have not carried anything",iAmCarrying, game.getUserManager().getUser("tester").inventory().itemsView().size());
    }

    @Test
    public void cannotDropSomethingThatIDoNotHave() {

        ResultOutput result;
        successfully(game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "8"));
        successfully(doVerb("tester", "look", ""));

        int iAmCarrying = game.getUserManager().getUser("tester").inventory().itemsView().size();

        Assert.assertTrue(game.getGameLocations().get("8").collectables().contains("cursed2"));
        result = doVerb("tester", "drop", "cursed2");
        Assert.assertEquals("Can Not Drop item",LastAction.FAIL, result.resultoutput.lastactionstate);

        result = doVerb("tester", "drop", "thingThatDoesNotExist999");
        Assert.assertEquals("Can Not Drop item that does not exist",LastAction.FAIL, result.resultoutput.lastactionstate);

        Assert.assertFalse(game.getGameLocations().get("8").collectables().contains("junk1"));
        result = doVerb("tester", "drop", "junk1");
        Assert.assertEquals("Can Not Drop item which I don'thold",LastAction.FAIL, result.resultoutput.lastactionstate);


        Assert.assertEquals("Have not carried anything",iAmCarrying, game.getUserManager().getUser("tester").inventory().itemsView().size());
    }

    @Test
    public void cannotHoardSomethingThatIDoNotHave() {

        ResultOutput result;
        successfully(game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "8"));
        successfully(doVerb("tester", "look", ""));

        int iAmCarrying = game.getUserManager().getUser("tester").inventory().itemsView().size();

        Assert.assertTrue(game.getGameLocations().get("8").collectables().contains("cursed2"));
        result = doVerb("tester", "hoard", "cursed2");
        Assert.assertEquals("Can Not Hoard item",LastAction.FAIL, result.resultoutput.lastactionstate);

        result = doVerb("tester", "hoard", "thingThatDoesNotExist999");
        Assert.assertEquals("Can Not Hoard item that does not exist",LastAction.FAIL, result.resultoutput.lastactionstate);

    }

    @Test
    public void canTakeAndHoard() {

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "8");
        result = doVerb("tester", "look", "");

        result =doVerb("tester","go","e");

        MudUser user = game.getUserManager().getUser("tester");
        int initialscore = user.getScore();

        Assert.assertNotNull("I can hoard here", result.look.treasureHoard);

        IdDescriptionPair[] collectables = result.look.collectables;
        Assert.assertEquals("I should see the three collectables", 3, collectables.length);

        result = doVerb("tester","inventory","");
        Assert.assertTrue(result.inventory!=null);
        int currentInventorySize = result.inventory.contents.length;

        // Am not carrying any of them
        Assert.assertFalse(user.inventory().contains("treasure1"));
        Assert.assertFalse(user.inventory().contains("cursed1"));
        Assert.assertFalse(user.inventory().contains("junk1"));

        int carryingCount = user.inventory().itemsView().size();

        // take them all
        for(IdDescriptionPair idPair : collectables){
            result = doVerb("tester","take",idPair.id);
            Assert.assertEquals("Take item "+idPair.id,LastAction.SUCCESS, result.resultoutput.lastactionstate);
        }

        Assert.assertEquals("Am carrying 3 more things",
                carryingCount+3, user.inventory().itemsView().size());

        Assert.assertTrue(user.inventory().contains("treasure1"));
        Assert.assertTrue(user.inventory().contains("cursed1"));
        Assert.assertTrue(user.inventory().contains("junk1"));

        result = doVerb("tester","inventory","");
        Assert.assertTrue(result.inventory!=null);
        Assert.assertEquals(currentInventorySize+3,result.inventory.contents.length);



        result = doVerb("tester","hoard","treasure1");
        Assert.assertEquals("Hoarded Treasure",LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("Score increase from hoarding", initialscore + 100, user.getScore());


        result = doVerb("tester","score","");

        Assert.assertTrue(result.users!=null);
        Assert.assertEquals("The Test User",result.users.get(0).displayName);
        Assert.assertEquals( Integer.valueOf(initialscore + 100),result.users.get(0).score);
        Assert.assertEquals("tester",result.users.get(0).userName);

        result = doVerb("tester","scores","");

        // only one user in the game
        Assert.assertTrue(result.users!=null);
        Assert.assertEquals("The Test User",result.users.get(0).displayName);
        Assert.assertEquals(Integer.valueOf(initialscore + 100),result.users.get(0).score);
        Assert.assertEquals("tester",result.users.get(0).userName);

        result = doVerb("tester","hoard","cursed1");
        Assert.assertEquals("Hoarded Cursed",LastAction.SUCCESS, result.resultoutput.lastactionstate);
        Assert.assertEquals("Score decrease from hoarding", initialscore + 150, user.getScore());


        result = doVerb("tester","score","");
        Assert.assertTrue(result.users!=null);
        Assert.assertEquals( Integer.valueOf(initialscore + 150),result.users.get(0).score);

        result = doVerb("tester","hoard","junk1");
        Assert.assertEquals("Cannot Hoarded Junk",LastAction.FAIL, result.resultoutput.lastactionstate);
        Assert.assertEquals("Score unchanged from failed hoarding", initialscore + 150, user.getScore());

        result = doVerb("tester", "look", "");
        Assert.assertEquals("9",result.look.location.locationId);

        // items should no longer be here
        Assert.assertEquals("items should no longer be here",0,result.look.collectables.length);

        // Am not carrying any of those hoarded
        Assert.assertFalse(user.inventory().contains("treasure1"));
        Assert.assertFalse(user.inventory().contains("cursed1"));
        Assert.assertTrue(user.inventory().contains("junk1"));

        // drop junk for some other sucker
        result = doVerb("tester","drop","junk1");
        Assert.assertEquals("Dropped Junk",LastAction.SUCCESS, result.resultoutput.lastactionstate);

        result = doVerb("tester", "look", "");
        Assert.assertFalse(user.inventory().contains("junk1"));
        Assert.assertEquals("junk should be here",1,result.look.collectables.length);
        Assert.assertEquals("junk is here","junk1",result.look.collectables[0].id);
    }

    private ResultOutput doVerb(String auser, String verb, String noun) {
        return dsl.doVerb(auser, verb, noun);
    }

    @Test
    public void canZapAndTakeAndHoard() {

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "10");
        result = doVerb("tester", "look", "");

        IdDescriptionPair[] collectables = result.look.collectables;
        Assert.assertEquals("There is nothing here", 0, collectables.length);

        result = successfully(doVerb("tester", "use", "zapherebutton"));

        collectables = result.look.collectables;
        Assert.assertEquals("There is somethine here now", 1, collectables.length);
        Assert.assertEquals("treasure2", collectables[0].id);

        successfully(doVerb("tester", "use", "zapherebutton"));
        Assert.assertEquals("It is still here", 1, collectables.length);
        Assert.assertEquals("treasure2", collectables[0].id);

        successfully(doVerb("tester", "take", "treasure2"));
        result = successfully(doVerb("tester", "go", "e"));
        Assert.assertEquals("taking us to room 11", "11", result.look.location.locationId);

        MudUser user = game.getUserManager().getUser("tester");
        int score = user.getScore();

        successfully(doVerb("tester", "hoard", "treasure2"));
        Assert.assertEquals(score + 100, user.getScore());

        result = doVerb("tester", "go", "w");
        collectables = result.look.collectables;
        Assert.assertEquals("There is nothing here", 0, collectables.length);

        result = successfully(doVerb("tester", "use", "zapherebutton"));

        collectables = result.look.collectables;
        Assert.assertEquals("There is something here now", 1, collectables.length);
        Assert.assertEquals("treasure2", collectables[0].id);

        successfully(doVerb("tester", "take", "treasure2"));
        successfully(doVerb("tester", "go", "e"));
        successfully(doVerb("tester", "hoard", "treasure2"));
        Assert.assertEquals(score + 200, user.getScore());

        // I could do this all day....
        successfully(doVerb("tester", "go", "w"));
        successfully(doVerb("tester", "use", "zapherebutton"));
        successfully(doVerb("tester", "take", "treasure2"));
        successfully(doVerb("tester", "go", "e"));
        successfully(doVerb("tester", "hoard", "treasure2"));
        Assert.assertEquals(score + 300, user.getScore());
    }

    private ResultOutput successfully(ResultOutput resultOutput) {
        return dsl.successfully(resultOutput);
    }


    @Test
    public void canInspectCollectables() {

        ResultOutput result;
        successfully(game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "10"));
        successfully(doVerb("tester", "go", "s"));
        result = successfully(doVerb("tester", "go", "e"));
        Assert.assertEquals("13",result.look.location.locationId);

        // cannot inspect something that is not here
        failTo((doVerb("tester", "inspect", "junk1")));
        failTo((doVerb("tester", "inspect", "athingthatdoesnotexist")));

        // I am not holding this
        failTo((doVerb("tester", "inspect", "inspectHoardable1")));
        result = successfully(doVerb("tester", "take", "inspectHoardable1"));

        MudUser user = game.getUserManager().getUser("tester");
        user.setScore(0);

        // I don't have enough points
        failTo((doVerb("tester", "inspect", "inspectHoardable1")));
        user.setScore(1000);
        result = successfully((doVerb("tester", "inspect", "inspectHoardable1")));

        Assert.assertEquals("inspecthoardable1",result.inspectReport.collectableId);
        Assert.assertEquals("A valuable treasure",result.inspectReport.collectableDescription);
        Assert.assertEquals("97",result.inspectReport.collectableHoardableScore);
        Assert.assertEquals("true",result.inspectReport.collectableIsHoardable);
        Assert.assertEquals("tester",result.inspectReport.playerName);
        Assert.assertTrue(result.inspectReport.costOfInspection.length()>0);
        Assert.assertEquals(user.getScore(),Integer.parseInt(result.inspectReport.playerScoreIsNow));
        Assert.assertEquals(1000, user.getScore()+Integer.parseInt(result.inspectReport.costOfInspection));
        Assert.assertNull(result.inspectReport.collectableAbilityPower);
        Assert.assertNull(result.inspectReport.collectableProvidesAbility);

        int aScore = user.getScore();
        successfully(doVerb("tester", "take", "inspectNotHoardable1"));
        result = successfully((doVerb("tester", "inspect", "inspectNotHoardable1")));
        Assert.assertEquals("inspectnothoardable1",result.inspectReport.collectableId);
        Assert.assertEquals("A very valuable treasure",result.inspectReport.collectableDescription);
        Assert.assertEquals("10000",result.inspectReport.collectableHoardableScore);
        Assert.assertEquals("false",result.inspectReport.collectableIsHoardable);
        Assert.assertEquals("tester",result.inspectReport.playerName);
        Assert.assertNull(result.inspectReport.collectableAbilityPower);
        Assert.assertNull(result.inspectReport.collectableProvidesAbility);
        Assert.assertTrue(result.inspectReport.costOfInspection.length()>0);
        Assert.assertTrue(aScore>user.getScore());

        successfully(doVerb("tester", "take", "inspectAbility1"));
        result = successfully((doVerb("tester", "inspect", "inspectAbility1")));
        Assert.assertEquals("inspectability1",result.inspectReport.collectableId);
        Assert.assertEquals("A very special valuable treasure",result.inspectReport.collectableDescription);
        Assert.assertEquals("500",result.inspectReport.collectableHoardableScore);
        Assert.assertEquals("true",result.inspectReport.collectableIsHoardable);
        Assert.assertEquals("tester",result.inspectReport.playerName);
        Assert.assertEquals("20",result.inspectReport.collectableAbilityPower);
        Assert.assertEquals("stink",result.inspectReport.collectableProvidesAbility);
        Assert.assertTrue(result.inspectReport.costOfInspection.length()>0);
        Assert.assertTrue(aScore>user.getScore());

        result = failTo((doVerb("tester", "inspect", "notinspect")));

    }

    private ResultOutput failTo(ResultOutput resultOutput) {
        return dsl.failTo(resultOutput);
    }


    @Test
    public void canExamineObjects() {

        ResultOutput result;
        successfully(game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "12"));
        successfully(doVerb("tester", "go", "s"));

        // cannot examine something that is not here
        Assert.assertTrue(failTo(doVerb("tester", "examine", "examineme")).
                resultoutput.lastactionresult.contains("don't see")
                ); // location object
        Assert.assertTrue(failTo(doVerb("tester", "examine", "examineThing1"))
                .resultoutput.lastactionresult.contains("don't see")); // collectable
        Assert.assertTrue(failTo(doVerb("tester", "examine", "aThingThatDoesNotExist"))
                .resultoutput.lastactionresult.contains("examine what?")); // what?

        successfully(doVerb("tester", "take", "examineThing2"));

        result = successfully(doVerb("tester", "go", "e"));
        Assert.assertEquals("15", result.look.location.locationId);

        // can examine something here
        Assert.assertTrue(successfully((doVerb("tester", "examine", "examineme"))).
                            resultoutput.lastactionresult.contains("examined it alright"));

        // can examine collectables but they have nothing to report
        Assert.assertTrue(successfully(doVerb("tester", "examine", "examineThing2")).
                resultoutput.lastactionresult.contains("nothing more to say"));
        Assert.assertTrue(successfully(doVerb("tester", "examine", "examineThing1")).
                resultoutput.lastactionresult.contains("nothing more to say"));
    }



    @Test
    public void illuminateDarken() {

        ResultOutput result;
        successfully(game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "14"));
        successfully(doVerb("tester", "go", "s"));

        successfully(doVerb("tester", "take", "aTorchOnTheFloor"));

        result = successfully(doVerb("tester", "go", "e"));
        Assert.assertEquals("17",result.look.location.locationId);

        MudUser user = game.getUserManager().getUser("tester");

        // it is dark, I cannot see
        Assert.assertFalse(user.canISeeInTheDark());
        Assert.assertTrue(game.getGameLocations().get(user.getLocationId()).isLocationDark());

        // I cannot take things in the dark
        result = failTo(doVerb("tester", "take", "aThingInTheDark"));

        //It is too dark to see"
        Assert.assertTrue(successfully(doVerb("tester", "look", "")).look.location.description.contains("It is too dark to see"));

        successfully(doVerb("tester", "illuminate", ""));

        Assert.assertTrue(user.canISeeInTheDark());
        Assert.assertTrue(game.getGameLocations().get(user.getLocationId()).isLocationDark());

        //It is too dark to see"
        Assert.assertFalse(successfully(doVerb("tester", "look", "")).
                look.location.description.contains("It is too dark to see"));

        Assert.assertTrue(user.inventory().get("aTorchOnTheFloor").getAbilityPower() > 0);

        //darken torch and make sure we can't see
        successfully(doVerb("tester", "darken", ""));

        Assert.assertTrue(successfully(doVerb("tester", "look", "")).
                            look.location.description.contains("It is too dark to see"));
        Assert.assertFalse(user.canISeeInTheDark());
        Assert.assertTrue(game.getGameLocations().get(user.getLocationId()).isLocationDark());

        successfully(doVerb("tester", "illuminate", ""));

        Assert.assertTrue(user.canISeeInTheDark());

        // I can pick things up when it is light
        result = successfully(doVerb("tester", "take", "aThingInTheDark"));
        Assert.assertTrue(user.inventory().contains("aThingInTheDark"));

        // keep looking until torch runs out and disappears
        int stopInfiniteLoop=0;
        while(user.inventory().get("aTorchOnTheFloor")!=null && user.inventory().get("aTorchOnTheFloor").getAbilityPower() > 0){
            successfully(doVerb("tester", "look", ""));
            stopInfiniteLoop++;
            Assert.assertTrue("Power on torch should run out", stopInfiniteLoop<100); // just in case the power does not drop, this will stop an infinite loop
        }

        // When it runs out we can't see
        Assert.assertTrue(successfully(doVerb("tester", "look", "")).
                look.location.description.contains("It is too dark to see"));
        Assert.assertFalse(user.canISeeInTheDark());
        Assert.assertTrue(game.getGameLocations().get(user.getLocationId()).isLocationDark());

        // go back and pick up the other torch which is already lit
        result = successfully(doVerb("tester", "go", "w"));
        Assert.assertEquals("16",result.look.location.locationId);

        successfully(doVerb("tester", "take", "aLitTorchOnTheFloor"));

        // come into the dark room and we can see
        result = successfully(doVerb("tester", "go", "e"));
        Assert.assertEquals("17",result.look.location.locationId);

        Assert.assertFalse(successfully(doVerb("tester", "look", "aLitTorchOnTheFloor")).
                look.location.description.contains("It is too dark to see"));
        Assert.assertTrue(user.canISeeInTheDark());
        Assert.assertTrue(game.getGameLocations().get(user.getLocationId()).isLocationDark());

        // create rule so we can't darken the prelit torch
        Assert.assertTrue(failTo(doVerb("tester", "darken", "aLitTorchOnTheFloor")).
                resultoutput.lastactionresult.contains("You leave the lit torch alone".toLowerCase()));

        // go back and get the used torch but cannot illuminate it because it has no power
        successfully(doVerb("tester", "drop", "aLitTorchOnTheFloor"));
        successfully(doVerb("tester", "go", "w"));
        successfully(doVerb("tester", "take", "aBurntOutTorch"));
        successfully(doVerb("tester", "go", "e"));

        Assert.assertTrue(successfully(doVerb("tester", "look", "")).
                look.location.description.contains("It is too dark to see"));
        Assert.assertFalse(user.canISeeInTheDark());

        Assert.assertTrue(failTo(doVerb("tester", "illuminate", "")).
                resultoutput.lastactionresult.contains("has no power left."));

        Assert.assertTrue(successfully(doVerb("tester", "look", "")).
                look.location.description.contains("It is too dark to see"));
        Assert.assertFalse(user.canISeeInTheDark());
    }

    @Test
    public void polishAndHoard() {

        ResultOutput result;
        successfully(game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "16"));
        successfully(doVerb("tester", "go", "s"));

        successfully(doVerb("tester", "take", "aThingToPolish"));
        successfully(doVerb("tester", "take", "aThingToNotPolish"));


        result = successfully(doVerb("tester", "go", "e"));
        Assert.assertEquals("19",result.look.location.locationId);


        successfully(doVerb("tester", "take", "aThingToPolishWith"));

        failTo(doVerb("tester", "polish", "aThingToNotPolish"));

        // polish it until I can polish no more

        MudUser user = game.getUserManager().getUser("tester");

        // just in case we made a mistake and there is a bug in
        // polish this prevents an infinite loop waiting for it
        int preventInfiniteLoopInTheEventOfABug = 100;

        while(user.inventory().contains("aThingToPolishWith")){

            successfully(doVerb("tester", "polish", "aThingToPolish"));

            preventInfiniteLoopInTheEventOfABug--;
            Assert.assertTrue("It should not take this long to polish something", preventInfiniteLoopInTheEventOfABug>0);

        }

        // now go hoard it and get more than 150 points
        successfully(doVerb("tester", "go", "w"));
        successfully(doVerb("tester", "go", "n"));
        successfully(doVerb("tester", "go", "n"));
        successfully(doVerb("tester", "go", "n"));
        successfully(doVerb("tester", "go", "n"));
        successfully(doVerb("tester", "go", "n"));
        result = successfully(doVerb("tester", "go", "e"));

        Assert.assertEquals("9",result.look.location.locationId);

        int score = user.getScore();

        successfully(doVerb("tester", "hoard", "aThingToPolish"));

        successfully(doVerb("tester", "score", ""));
        int newScore = user.getScore();
        Assert.assertTrue("Score should have increased", newScore > score);
        Assert.assertTrue("Polishing should have increased score", newScore > (score + 150));

    }


    @Test
    public void countersUpAndDown() {

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "8");
        result = successfully(doVerb("tester", "look", ""));


        result = successfully(doVerb("tester", "go", "w"));

        Assert.assertTrue(game.getGameLocations().get("20").objects().get("redbutton").isSecret());
        Assert.assertTrue(game.getGameLocations().get("20").objects().get("bluebutton").isSecret());

        result = successfully(doVerb("tester", "examine", "acounter"));
        Assert.assertFalse(game.getGameLocations().get("20").objects().get("redbutton").isSecret());
        Assert.assertFalse(game.getGameLocations().get("20").objects().get("bluebutton").isSecret());

        result = successfully(doVerb("tester", "examine", "acounter"));

        result = successfully(doVerb("tester", "look", ""));

        //push buttons up until ding and check score
        result = successfully(doVerb("tester", "push", "redbutton"));

        MudUser user = game.getUserManager().getUser("tester");
        int score = user.getScore();

        result = successfully(doVerb("tester", "push", "redbutton"));
        result = successfully(doVerb("tester", "push", "redbutton"));


        int newscore = user.getScore();
        Assert.assertEquals(score+20, newscore);

        result = successfully(doVerb("tester", "push", "bluebutton"));
        result = successfully(doVerb("tester", "push", "bluebutton"));
        result = successfully(doVerb("tester", "push", "bluebutton"));
        result = successfully(doVerb("tester", "push", "bluebutton"));


        result = successfully(doVerb("tester", "push", "redbutton"));


        newscore = user.getScore();
        Assert.assertEquals(score+20+20, newscore);

        result = successfully(doVerb("tester", "push", "redbutton"));
        result = successfully(doVerb("tester", "push", "redbutton"));



        newscore = user.getScore();
        Assert.assertEquals(score+20+20+20, newscore);

        result = failTo(doVerb("tester", "push", "redbutton"));

        result = successfully(doVerb("tester", "look", ""));

        result = successfully(doVerb("tester", "examine", "acounter"));
        Assert.assertTrue(game.getGameLocations().get("20").objects().get("redbutton").isSecret());
        Assert.assertTrue(game.getGameLocations().get("20").objects().get("bluebutton").isSecret());


    }

    @Test
    public void buttonsForFlag() {

        ResultOutput result;
        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "8");
        result = successfully(doVerb("tester", "look", ""));
        result = successfully(doVerb("tester", "go", "w"));

        Assert.assertTrue(result.look.isObjectVisible("blackbuttonwall"));
        Assert.assertFalse(result.look.isObjectVisible("greenbuttonfloor"));

        result = failTo(doVerb("tester", "push", "greenbuttonfloor"));
        result = successfully(doVerb("tester", "push", "blackbuttonwall"));

        Assert.assertTrue(result.look.isObjectVisible("blackbuttonwall"));
        Assert.assertTrue(result.look.isObjectVisible("greenbuttonfloor"));

        result = failTo(doVerb("tester", "push", "blackbuttonwall"));

        Assert.assertTrue(result.look.isObjectVisible("blackbuttonwall"));
        Assert.assertTrue(result.look.isObjectVisible("greenbuttonfloor"));

        result = successfully(doVerb("tester", "push", "greenbuttonfloor"));
        Assert.assertTrue(result.look.isObjectVisible("blackbuttonwall"));
        Assert.assertFalse(result.look.isObjectVisible("greenbuttonfloor"));

        result = failTo(doVerb("tester", "push", "greenbuttonfloor"));

        // try again

        result = successfully(doVerb("tester", "push", "blackbuttonwall"));

        Assert.assertTrue(result.look.isObjectVisible("blackbuttonwall"));
        Assert.assertTrue(result.look.isObjectVisible("greenbuttonfloor"));

        result = successfully(doVerb("tester", "push", "greenbuttonfloor"));
        Assert.assertTrue(result.look.isObjectVisible("blackbuttonwall"));
        Assert.assertFalse(result.look.isObjectVisible("greenbuttonfloor"));

    }

    @Test
    public void goSouthWhenThereIsNoExitButThereIsAVerbRule() {

        ResultOutput result;

        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "10");

        result = successfully(doVerb("tester", "look", ""));

        result = successfully(doVerb("tester", "go", "w"));

        // only one exit in 21 and it is to the east
        Assert.assertEquals("21",result.look.location.locationId);
        Assert.assertTrue(result.look.exits.size()==1);
        result = successfully(doVerb("tester", "go", "e"));
        Assert.assertEquals("10",result.look.location.locationId);
        result = successfully(doVerb("tester", "go", "w"));

        // but I can go south because of a verb rule
        result = successfully(doVerb("tester", "go", "south"));
        Assert.assertEquals("10",result.look.location.locationId);
        result = successfully(doVerb("tester", "go", "w"));

        result = successfully(doVerb("tester", "go", "South"));
        Assert.assertEquals("10",result.look.location.locationId);
        result = successfully(doVerb("tester", "go", "w"));

        result = successfully(doVerb("tester", "go", "SOUTH"));
        Assert.assertEquals("10",result.look.location.locationId);
        result = successfully(doVerb("tester", "go", "w"));

        result = successfully(doVerb("tester", "go", "S"));
        Assert.assertEquals("10",result.look.location.locationId);
        result = successfully(doVerb("tester", "go", "w"));
    }

    @Test
    public void thenShowGateLocationCollectableInLook(){

        ResultOutput result;

        result = game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "12");

        result = successfully(doVerb("tester", "look", ""));

        result = successfully(doVerb("tester", "go", "w"));


        Assert.assertEquals("22",result.look.location.locationId);


        Assert.assertFalse(result.look.isObjectVisible("hiddensign"));
        Assert.assertFalse(result.look.isCollectableVisible("anothing"));
        Assert.assertTrue(result.look.visibleGates==null);

        // gate should be hidden so we can't go that way or open the gate
        result = failTo(doVerb("tester", "go", "e"));
        result = failTo(doVerb("tester", "open", "e"));

        result = successfully(doVerb("tester", "examine", "asignonwall"));
        Assert.assertTrue(result.resultoutput.lastactionresult.contains("boo"));

        result = successfully(doVerb("tester", "say", "boo"));

        Assert.assertTrue(result.look.isObjectVisible("hiddensign"));
        Assert.assertTrue(result.look.isCollectableVisible("anothing"));
        Assert.assertTrue(result.look.visibleGates.size()==1);
        // can show gates in look even if hidden
        // can show 'collectables' that do not exist in look
        // can show location objects in look even if secret

        result = successfully(doVerb("tester", "look", ""));
        Assert.assertFalse(result.look.isObjectVisible("hiddensign"));
        Assert.assertFalse(result.look.isCollectableVisible("anothing"));

        // gate should be visible so we can open it and go in that direction
        result = successfully(doVerb("tester", "open", "e"));
        result = successfully(doVerb("tester", "go", "e"));
        Assert.assertEquals("12",result.look.location.locationId);
    }

    @Test
    public void canUseADispenserToGetATorch(){

        ResultOutput result;
        game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "14");
        successfully(doVerb("tester","look", ""));
        result = successfully(doVerb("tester","go", "w"));

        Assert.assertEquals("23",result.look.location.locationId);

        Assert.assertTrue(result.look.collectables.length==0);
        Assert.assertTrue(result.look.isObjectVisible("torchdispenser"));

        result = successfully(doVerb("tester","use", "torchdispenser"));
        result = successfully(doVerb("tester","look", ""));
        Assert.assertTrue(result.look.collectables.length==1);

        String torchId = result.look.collectables[0].id;

        result = successfully(doVerb("tester","take", torchId));
        result = successfully(doVerb("tester","look", ""));

        Assert.assertTrue(result.look.collectables.length==0);

        result = successfully(doVerb("tester","inventory", ""));

        boolean haveTorchInInventory = false;
        for( IdDescriptionPair anId : result.inventory.contents){
            if(anId.id.contentEquals(torchId)) {
                haveTorchInInventory = true;
            }
        }

        Assert.assertTrue(haveTorchInInventory);

        result = successfully(doVerb("tester","use", "torchdispenser"));
        result = successfully(doVerb("tester","use", "torchdispenser"));
        result = successfully(doVerb("tester","use", "torchdispenser"));

        result = successfully(doVerb("tester","look", ""));
        Assert.assertTrue(result.look.collectables.length==3);

    }

    @Test
    public void aDispenserCanDispenseAHoardableThing() {

        ResultOutput result;
        game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "14");
        successfully(doVerb("tester", "look", ""));
        result = successfully(doVerb("tester", "go", "w"));

        Assert.assertEquals("23", result.look.location.locationId);

        result = successfully(doVerb("tester", "use", "golddispenser"));

        result = successfully(doVerb("tester", "look", ""));
        String goldId="";
        for( IdDescriptionPair anId : result.look.collectables){
            if(anId.id.startsWith("gold")) {
                goldId = anId.id;
            }
        }

        result = successfully(doVerb("tester", "take", goldId));

        game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "9");
        result = successfully(doVerb("tester", "look", ""));
        Assert.assertEquals("9",result.look.location.locationId);

        MudUser user = game.getUserManager().getUser("tester");
        int score = user.getScore();

        successfully(doVerb("tester", "hoard", goldId));

        successfully(doVerb("tester", "score", ""));
        int newScore = user.getScore();
        Assert.assertTrue("Score should have increased", newScore > score);

    }

    @Test
    public void playerCanCheckTheirMessages() {
        ResultOutput result;
        result = successfully(doVerb("tester", "messages", ""));

        Assert.assertTrue(result.resultoutput.getGameMessages()!=null);
        Assert.assertEquals(0, result.resultoutput.getGameMessages().messages.size());

        game.broadcastMessages().wizardBroadcaseMessage("hello");

        // use of ConcurrentLinkedDeque seems to make this intermittent because it isn't guaranteed synchronousness
        // might have to have a message id instead of a timestamp, we might miss messages if it is fast
        // added assertions

        GameMessages messages = GameMessages.getEmpty();
//        int maxLoopLimit = 10;

//        while(messages.messages.size() == 0 && maxLoopLimit>0){
            result = successfully(doVerb("tester", "messages", ""));
//            messages = result.resultoutput.getGameMessages();
//            maxLoopLimit--;
//            if(messages.messages.size()==0){
//                try {
//                    Thread.sleep(200);
//               } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

//        System.out.println("looped == " + (10 - maxLoopLimit));
        Assert.assertTrue(result.resultoutput.getGameMessages()!=null);
        Assert.assertEquals(1, result.resultoutput.getGameMessages().messages.size());
//        Assert.assertEquals(9,maxLoopLimit);


    }

}
