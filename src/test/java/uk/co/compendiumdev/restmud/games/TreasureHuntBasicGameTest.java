package uk.co.compendiumdev.restmud.games;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.TreasureHuntBasicGameDefinitionGenerator;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;
import uk.co.compendiumdev.restmud.testing.dsl.GameTestDSL;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TreasureHuntBasicGameTest {

    private static MudGame game;
    private static GameTestDSL dsl;

    /* Example Documented Game is deterministic and well documented - it should be used to automate gameplay paths */

    @BeforeClass
    static public void setupTheGame(){

        // TODO - this is all a bit convoluted the definition seems to depend on a game for the User Input Parser
        // TODO - definition should be entirely independent of 'game'
        GameInitializer theGameInit = new GameInitializer();
        game = theGameInit.getGame();
        MudGameDefinition defn = MudGameDefinition.create(game.getUserInputParser());
        new TreasureHuntBasicGameDefinitionGenerator().define(defn);
        game.initFromDefinition(defn);

        // generate does not add any users
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addDefaultUser("The Test User", "tester", "aPassword");
        theGameInit.addDefaultUser("Another Test User", "tester2", "aPassword");

        game.teleportUserTo("tester", "1"); // set the user back to the central room after each path

        dsl = new GameTestDSL(game);

    }


    private ResultOutput doVerb(String auser, String verb, String noun) {
        return dsl.doVerb(auser, verb, noun);
    }

    private ResultOutput successfully(ResultOutput resultOutput) {
        return dsl.successfully(resultOutput);
    }
    private ResultOutput failTo(ResultOutput resultOutput) {
        return dsl.failTo(resultOutput);
    }


    @Test
    public void toBeAbleToEnterPuzzleLandYouHaveToSayPuzzle(){
        // otherwise bots get lost in there
        ResultOutput result;
        result = game.teleportUserTo("tester","3");
        result = doVerb("tester","look", "");
        result = failTo(doVerb("tester","go", "s"));
        result = successfully(doVerb("tester","examine", "puzzlelandsign"));

        result = doVerb("tester","score", "");
        Integer score = result.users.get(0).score;

        result = successfully(doVerb("tester","say", "puzzle"));

        result = doVerb("tester","score", "");
        Integer newscore = result.users.get(0).score;
        Assert.assertTrue(newscore > score);
        Assert.assertEquals(newscore.intValue(), (score.intValue()+100));

        successfullyNavigatedTo("18", doVerb("tester","go", "s"));

        successfullyNavigatedTo("3", doVerb("tester","go", "n"));

        result = failTo(doVerb("tester","say", "puzzle"));

        successfullyNavigatedTo("18", doVerb("tester","go", "s"));

    }

    @Test
    public void solveTheBombPuzzleByDefusingTheBomb(){

        ResultOutput result;
        result = game.teleportUserTo("tester","2");
        result = doVerb("tester","look", "");

        // the bomb puzzle is completely rule based

        result = doVerb("tester","score", "");
        Integer score = result.users.get(0).score;

        successfully(doVerb("tester","examine", "smallroundthing"));
        successfully(doVerb("tester","take", "smallroundthing"));
        successfully(doVerb("tester","defuse", "smallroundthing"));

        result = doVerb("tester","score", "");
        Integer newscore = result.users.get(0).score;
        Assert.assertTrue(newscore > score);
        Assert.assertEquals(newscore.intValue(), (score.intValue()+100));

        // ensure we can't pick up the bomb again
        failTo(doVerb("tester","examine", "smallroundthing"));
        failTo(doVerb("tester","take", "smallroundthing"));

    }

    @Test
    public void solveTheBombPuzzleByWaitingAndLettingTheBombExplode(){

        ResultOutput result;
        result = game.teleportUserTo("tester2","2");
        result = doVerb("tester2","look", "");

        // the bomb puzzle is completely rule based

        result = doVerb("tester2","score", "");
        Integer score = result.users.get(0).score;

        successfully(doVerb("tester2","examine", "smallroundthing"));
        successfully(doVerb("tester2","take", "smallroundthing"));
        for(int turn=1; turn<10;turn++) {
            successfully(doVerb("tester2", "look", ""));
        }

        result = doVerb("tester2","score", "");
        Integer newscore = result.users.get(0).score;
        Assert.assertTrue(newscore > score);
        Assert.assertEquals(newscore.intValue(), (score.intValue()+5));

        // ensure we can't pick up the bomb again
        failTo(doVerb("tester","examine", "smallroundthing"));
        failTo(doVerb("tester","take", "smallroundthing"));
    }

    @Test
    public void theBrokenTransporterDoesNothing(){

        ResultOutput result;
        result = game.teleportUserTo("tester2","4");
        result = doVerb("tester2","look", "");

        // the bomb puzzle is completely rule based

        result = successfully(doVerb("tester2","go", "s"));

        Assert.assertEquals("4", result.look.location.locationId);
    }

    @Test
    public void theLickingTheStoneSection(){

        ResultOutput result;
        result = game.teleportUserTo("tester2","6");
        result = doVerb("tester2","look", "");

        result = failTo(doVerb("tester2","take", "loosestone"));
        result = successfully(doVerb("tester2","examine", "loosestone"));

        result = successfully(doVerb("tester2","score", ""));
        Integer score = result.users.get(0).score;

        result = successfully(doVerb("tester2","lick", "loosestone"));

        result = successfully(doVerb("tester2","score", ""));
        Integer newscore = result.users.get(0).score;
        Assert.assertTrue(newscore > score);
        Assert.assertEquals(newscore.intValue(), (score.intValue()+10));

        // do it again and lose points
        result = failTo(doVerb("tester2","lick", "loosestone"));

        result = successfully(doVerb("tester2","score", ""));
        newscore = result.users.get(0).score;
        Assert.assertTrue(newscore > score);
        Assert.assertEquals(newscore.intValue(), (score.intValue()+5));

    }



            /*
            Puzzle Corridor  has rooms off it with self contained puzzles for points
            Each Puzzle can only be completed once by each player

            vase puzzle - a monkey hand trap
               x there is a hammer chained to the wall
               x use hammer - without having vase - use it to do what? it isn't hammering time unless there is something to hammer
               x sign in vase puzzle room "treasure in vase, one per player"
               x take treasure - but it is in the vase
               x examine vase - at the bottom of vase is a treasure, and you want it
               x take vase
                   x try to take more than one vase and lose points
               x take treasure - the opening of the vase is too small to get the treasure out
               x go n in 21 - to the hammer room
               x use hammer - when having vase then it smashes and you get treasure

            Review:
                - coverage of conditions
                - actual test coverage

            Test Coverage
                - basic flow where pick it up smash it good
                - try to get vase twice
                - try to get a vase after smashing

         */



    @Test
    public void canNavigateToTheJewelryRoom() {

        ResultOutput result;
        result = game.teleportUserTo("tester2", "6");
        result = successfullyNavigatedTo("22", doVerb("tester2", "go", "s"));
        result = successfullyNavigatedTo("6", doVerb("tester2", "go", "n"));
    }


    @Test
    public void canUseTheJewelryDispenser() {

        ResultOutput result;
        result = game.teleportUserTo("tester2", "6");
        result = successfullyNavigatedTo("22", doVerb("tester2", "go", "s"));
        int numberOfThingsHere = result.look.collectables.length;

        result = successfully(doVerb("tester2", "use", "jeweldispenser"));
        result = successfully(doVerb("tester2", "look", ""));
        int newNumberOfThingsHere = result.look.collectables.length;
        Assert.assertTrue(newNumberOfThingsHere > numberOfThingsHere);

        String thingId="";

        // and can hoard the jewel
        for(IdDescriptionPair collectable : result.look.collectables){
            if(collectable.id.startsWith("jewel_")){
                thingId = collectable.id;
                break;
            }
        }

        result = successfully(doVerb("tester2","take", thingId));

        result = game.teleportUserTo("tester2", "1");

        result = successfully(doVerb("tester2","score", ""));
        Integer score = result.users.get(0).score;

        result = successfully(doVerb("tester2","hoard", thingId));

        result = successfully(doVerb("tester2","score", ""));
        Integer newScore = result.users.get(0).score;
    }

    @Test
    public void canNavigateToTheCheapJewelryRoom() {

        ResultOutput result;
        result = game.teleportUserTo("tester2", "14");
        result = successfullyNavigatedTo("23", doVerb("tester2", "go", "n"));
        result = successfullyNavigatedTo("14", doVerb("tester2", "go", "s"));
    }


    @Test
    public void canUseTheJewelryDispenserInRoom23() {

        ResultOutput result;
        result = game.teleportUserTo("tester2", "23");
        result = successfully(doVerb("tester2", "look", ""));
        int numberOfThingsHere = result.look.collectables.length;

        result = successfully(doVerb("tester2", "use", "ajeweldispenser"));
        result = successfully(doVerb("tester2", "look", ""));
        int newNumberOfThingsHere = result.look.collectables.length;
        Assert.assertTrue(newNumberOfThingsHere > numberOfThingsHere);

        String thingId="";

        // and can hoard the jewel
        for(IdDescriptionPair collectable : result.look.collectables){
            if(collectable.id.startsWith("jewel_")){
                thingId = collectable.id;
                break;
            }
        }

        result = successfully(doVerb("tester2","take", thingId));

        result = game.teleportUserTo("tester2", "1");

        result = successfully(doVerb("tester2","score", ""));
        Integer score = result.users.get(0).score;

        result = successfully(doVerb("tester2","hoard", thingId));

        result = successfully(doVerb("tester2","score", ""));
        Integer newScore = result.users.get(0).score;
        Assert.assertTrue(newScore > score);
    }

    @Test
    public void canGoWestFrom9AndGetScoreWhenTeleport() {

        ResultOutput result;
        result = game.teleportUserTo("tester2", "8");

        result = successfully(doVerb("tester2","score", ""));
        Integer score = result.users.get(0).score;

        result = successfullyNavigatedTo("9", doVerb("tester2","go", "w"));

        result = successfully(doVerb("tester2","score", ""));
        Integer newScore = result.users.get(0).score;

        Assert.assertTrue(newScore > score);
    }


    @Test
    public void canUseTheMegaClothDispenserInRoom9ToGetACloth() {

        ResultOutput result;
        result = game.teleportUserTo("tester2", "9");
        result = successfully(doVerb("tester2", "look", ""));
        int numberOfThingsHere = result.look.collectables.length;

        result = successfully(doVerb("tester2", "use", "megaclothdispenser"));

        result = successfully(doVerb("tester2", "look", ""));
        int newNumberOfThingsHere = result.look.collectables.length;
        Assert.assertTrue(newNumberOfThingsHere > numberOfThingsHere);

        //megaclothdispenser
        String thingId = takeACollectableStartingWith("tester2", "cloth_");

    }

    private String takeACollectableStartingWith(String userName, String thingStartsWith) {
        ResultOutput result;
        result = successfully(doVerb(userName, "look", ""));
        int newNumberOfThingsHere = result.look.collectables.length;

        String thingId="";

        // and can hoard the jewel
        for(IdDescriptionPair collectable : result.look.collectables){
            if(collectable.id.startsWith(thingStartsWith)){
                thingId = collectable.id;
                break;
            }
        }

        result = successfully(doVerb(userName,"take", thingId));
        return thingId;
    }


    @Test
    public void canNavigateToTheVasePuzzleRoomAndBack(){

         ResultOutput result;
         result = game.teleportUserTo("tester2","3");
         result = successfullyNavigatedTo("3", doVerb("tester2","look", ""));
         result = successfully(doVerb("tester2","say", "puzzle"));
         result = successfullyNavigatedTo("18", doVerb("tester2","go", "s"));
         result = successfullyNavigatedTo("19",doVerb("tester2","go", "s"));
         result = successfullyNavigatedTo("20",doVerb("tester2","go", "s"));
         result = successfullyNavigatedTo("21",doVerb("tester2","go", "s"));

         result = successfullyNavigatedTo("20",doVerb("tester2","go", "n"));
         result = successfullyNavigatedTo("19",doVerb("tester2","go", "n"));
         result = successfullyNavigatedTo("18",doVerb("tester2","go", "n"));
         result = successfullyNavigatedTo("3",doVerb("tester2","go", "n"));
     }

     // - there is a hammer chained to the wall
     // - use hammer - without having vase - use it to do what? it isn't hammering time unless there is something to hammer
     @Test
     public void canExamineHammerAndNotUseItWithoutVase(){
         ResultOutput result;
         result = game.teleportUserTo("tester2","20");
         result = successfullyNavigatedTo("20", doVerb("tester2","look", ""));

         result = successfully(doVerb("tester2","examine", "hammer"));
         result = failTo(doVerb("tester2","use", "hammer"));
         Assert.assertFalse(result.resultoutput.lastactionresult.contains("know how to use"));
     }


    @Test
    public void canTakeTreasureAndGetWithHammer(){

        game.teleportUserTo("tester","20");
        successfullyNavigatedTo("20", doVerb("tester","look", ""));
        successfullyNavigatedTo("21", doVerb("tester","go", "s"));

        successfully(doVerb("tester","take", "prizevase"));

        ResultOutput result = successfully(doVerb("tester","score", ""));
        Integer score = result.users.get(0).score;

        successfullyNavigatedTo("20", doVerb("tester","go", "n"));
        successfully(doVerb("tester","use", "hammer"));

        result = successfully(doVerb("tester","score", ""));
        Integer newscore = result.users.get(0).score;
        Assert.assertTrue(newscore > score);
        Assert.assertEquals(newscore.intValue(), score.intValue()+100);
    }

    @Test
    public void cannotUseHammerTwice(){

        game.getUserManager().add(new MudUser("hammerhammer", "hammerhammer", "password"));
        ResultOutput result = game.teleportUserTo("hammerhammer", "21");
        result = successfullyNavigatedTo("21", doVerb("hammerhammer","look", ""));

        result = successfully(doVerb("hammerhammer","take", "prizevase"));
        successfullyNavigatedTo("20", doVerb("hammerhammer","go", "n"));

        result = successfully(doVerb("hammerhammer","score", ""));
        Integer score = result.users.get(0).score;

        result = successfully(doVerb("hammerhammer","use", "hammer"));

        result = successfully(doVerb("hammerhammer","score", ""));
        Integer newscore = result.users.get(0).score;
        Assert.assertTrue(newscore > score);
        score = newscore;

        result = failTo(doVerb("hammerhammer","use", "hammer"));

        result = successfully(doVerb("hammerhammer","score", ""));
        newscore = result.users.get(0).score;
        Assert.assertTrue(newscore < score);
        Assert.assertEquals(newscore.intValue(), score.intValue()-10);

    }


    // - sign in vase puzzle room "treasure in vase, one per player"
    @Test
    public void canExamineVaseSign(){

        game.getUserManager().add(new MudUser("signReader", "signReader", "password"));
        ResultOutput result = game.teleportUserTo("signReader", "21");
        result = successfullyNavigatedTo("21", doVerb("signReader","look", ""));

        result = successfully(doVerb("signReader","examine", "vasePuzzleSign"));

        Assert.assertTrue(result.resultoutput.lastactionresult.contains("only one per player"));
    }

    // - examine vase - at the bottom of vase is a treasure, and you want it
    @Test
    public void prizeVaseHasDifferentCustomExamineMessageAfterTaking(){
        game.getUserManager().add(new MudUser("examinerofvase", "examinerofvase", "password"));
        ResultOutput result = game.teleportUserTo("examinerofvase", "21");
        result = successfullyNavigatedTo("21", doVerb("examinerofvase","look", ""));

        result = successfully(doVerb("examinerofvase","examine", "prizevase"));
        Assert.assertTrue(result.resultoutput.lastactionresult.contains("the vases look the same"));

        result = successfully(doVerb("examinerofvase","take", "prizevase"));

        result = successfully(doVerb("examinerofvase","examine", "prizevase"));
        Assert.assertTrue(result.resultoutput.lastactionresult.contains("the most valuable"));
    }


    // - try to take more than one vase and lose points
    @Test
    public void canOnlyPickUpAVaseOnce(){
         game.getUserManager().add(new MudUser("vaseonce", "vaseonce", "password"));
        ResultOutput result = game.teleportUserTo("vaseonce", "21");
        result = successfullyNavigatedTo("21", doVerb("vaseonce","look", ""));

        result = successfully(doVerb("vaseonce","take", "prizevase"));

        result = successfully(doVerb("vaseonce","score", ""));
        Integer score = result.users.get(0).score;

        // can only pick up one
        result = failTo(doVerb("vaseonce","take", "prizevase"));
        Assert.assertTrue(result.resultoutput.lastactionresult.contains("already picked one up"));

        result = successfully(doVerb("vaseonce","score", ""));
        Integer newscore = result.users.get(0).score;
        Assert.assertTrue(newscore < score);
        Assert.assertEquals(newscore.intValue(), (score.intValue()-5));
        score = newscore;
    }

    // fail to take treasure after having already taken a vase, but no longer carrying one
    @Test
    public void cannotTakeVaseAfterSmashingAlready(){
        game.getUserManager().add(new MudUser("smashtreasuretakertwice", "smashtreasuretakertwice", "password"));
        game.teleportUserTo("smashtreasuretakertwice", "21");

        successfully(doVerb("smashtreasuretakertwice","take", "prizevase"));

        successfullyNavigatedTo("20", doVerb("smashtreasuretakertwice","go", "n"));
        successfully(doVerb("smashtreasuretakertwice","use", "hammer"));

        ResultOutput result = successfully(doVerb("smashtreasuretakertwice","score", ""));
        Integer score = result.users.get(0).score;

        successfullyNavigatedTo("21", doVerb("smashtreasuretakertwice","go", "s"));

        result = failTo(doVerb("smashtreasuretakertwice","take", "prizevase"));
        Assert.assertTrue(result.resultoutput.lastactionresult.contains("already picked one up"));

        result = successfully(doVerb("smashtreasuretakertwice","score", ""));
        Integer newscore = result.users.get(0).score;
        Assert.assertTrue(newscore < score);
        Assert.assertEquals(newscore.intValue(), (score.intValue()-5));
        score = newscore;
    }


    // fail to take treasure without having already taken a vase
    @Test
    public void cannotTakeTreasureWithoutTakingAVase(){

        game.getUserManager().add(new MudUser("vasefirst", "vasefirst", "password"));
        ResultOutput result = game.teleportUserTo("vasefirst", "21");
        result = successfullyNavigatedTo("21", doVerb("vasefirst","look", ""));

        result = failTo(doVerb("vasefirst","take", "prizevasetreasure"));
        Assert.assertTrue(result.resultoutput.lastactionresult.contains("you really need to take a vase first"));
    }

    // - take treasure - the opening of the vase is too small to get the treasure out
    // fail to take treasure after having taken a vase
    @Test
    public void cannotTakeTreasureOutOfVaseVase(){

        game.getUserManager().add(new MudUser("stuckinvase", "stuckinvase", "password"));
        game.teleportUserTo("stuckinvase", "21");
        successfullyNavigatedTo("21", doVerb("stuckinvase","look", ""));
        successfully(doVerb("stuckinvase","take", "prizevase"));
        ResultOutput result = failTo(doVerb("stuckinvase", "take", "prizevasetreasure"));
        Assert.assertTrue(result.resultoutput.lastactionresult.contains("grab the treasure at the bottom"));
    }


    // can drop a vase if carrying it
    @Test
    public void canDropAVaseAndTakeItAgainOnce(){
        game.getUserManager().add(new MudUser("vasedrop", "vasedrop", "password"));
        ResultOutput result = game.teleportUserTo("vasedrop", "21");
        result = successfullyNavigatedTo("21", doVerb("vasedrop","look", ""));

        result = successfully(doVerb("vasedrop","score", ""));
        Integer score = result.users.get(0).score;

        result = successfully(doVerb("vasedrop","take", "prizevase"));
        result = successfully(doVerb("vasedrop","drop", "prizevase"));
        result = successfully(doVerb("vasedrop","take", "prizevase"));

        result = successfully(doVerb("vasedrop","score", ""));
        Integer newscore = result.users.get(0).score;
        Assert.assertEquals(newscore, score);
    }

    // can not drop a vase if not carrying it
    @Test
    public void cannotDropAVaseImNotCarrying(){
        game.getUserManager().add(new MudUser("vasedropnot", "vasedropnot", "password"));
        ResultOutput result = game.teleportUserTo("vasedropnot", "21");
        result = successfullyNavigatedTo("21", doVerb("vasedropnot","look", ""));

        result = successfully(doVerb("vasedropnot","score", ""));
        Integer score = result.users.get(0).score;

        result = failTo(doVerb("vasedropnot","drop", "prizevase"));
        Assert.assertTrue(result.resultoutput.lastactionresult.contains("vase to drop"));

        result = successfully(doVerb("vasedropnot","score", ""));
        Integer newscore = result.users.get(0).score;
        Assert.assertEquals(newscore, score);
    }


    // fail to take treasure after having already taken a vase, but no longer carrying one
    @Test
    public void customMessageForTryingToTakeATreasureAfterSmash(){
        game.getUserManager().add(new MudUser("smashtreasuretaker", "smashtreasuretaker", "password"));
        game.teleportUserTo("smashtreasuretaker", "21");

        successfully(doVerb("smashtreasuretaker","take", "prizevase"));
        successfullyNavigatedTo("20", doVerb("smashtreasuretaker","go", "n"));
        successfully(doVerb("smashtreasuretaker","use", "hammer"));

        successfullyNavigatedTo("21", doVerb("smashtreasuretaker","go", "s"));

        ResultOutput result = failTo(doVerb("smashtreasuretaker", "take", "prizevasetreasure"));
        Assert.assertTrue(result.resultoutput.lastactionresult.contains("you need to take a vase first"));
    }



    // tests for adding a zombie maze into the treasure hunt game - this is basically taken from zombie zork but smaller
    @Test
    public void canEnterAndLeaveZombieMaze(){
        String tUser = "zombiemazer";

        game.getUserManager().add(new MudUser("zombiemazer", tUser, "password"));
        game.teleportUserTo(tUser, "11");

        // can examine a zombie maze sign in location 11
        successfully(doVerb(tUser,"examine", "zombiemazesign"));
        ResultOutput result = successfullyNavigatedTo("11", doVerb(tUser, "look", ""));

        // when say brains I am transported to the zombie maze
        successfully(doVerb(tUser,"say", "brains"));
        result = successfullyNavigatedTo("24", doVerb(tUser, "look", ""));

        // there is a rusty gate beyond which I can see a corridor but I can't open the gate
        successfully(doVerb(tUser,"examine", "rustygate"));
        // open rustygate - nope, can't do that it is rusty and not openable
        failTo(doVerb(tUser,"open", "rustygate"));
        failTo(doVerb(tUser,"go", "n"));
        failTo(doVerb(tUser,"take", "rustygate"));

        // there is a rock at the entrance and if I Examine it I can see that I can say "brains" backwards to leave "sniarb"
        successfully(doVerb(tUser,"examine", "dirtyrock"));

        successfully(doVerb(tUser,"say", "brainsbackwards"));
        result = successfullyNavigatedTo("24", doVerb(tUser, "look", ""));

        successfully(doVerb(tUser,"say", "sniarb"));
        result = successfullyNavigatedTo("2", doVerb(tUser, "look", ""));



    }

    // see a barred door in location 5
    @Test
    public void canExamineDoorInLocationFive() {
        String tUser = "zombiedoorexaminer";

        game.getUserManager().add(new MudUser("zombiedoro", tUser, "password"));
        game.teleportUserTo(tUser, "5");

        // can examine a zombie maze sign in location 11
        successfully(doVerb(tUser, "examine", "rustedshutdoor"));
        ResultOutput result = successfullyNavigatedTo("5", doVerb(tUser, "look", ""));

        failTo(doVerb(tUser, "open", "rustedshutdoor"));
        result = successfullyNavigatedTo("5", doVerb(tUser, "look", ""));

        failTo(doVerb(tUser, "open", "s"));
        result = successfullyNavigatedTo("5", doVerb(tUser, "look", ""));

        failTo(doVerb(tUser, "go", "s"));
        result = successfullyNavigatedTo("5", doVerb(tUser, "look", ""));

    }

    // wander around maze - if caught by zombie then lose points and zap to the start
    @Test
    public void canGetCaughtByZombies() {
        String tUser = "zombiedeath";

        game.getUserManager().add(new MudUser("zombiedodro", tUser, "password"));
        game.teleportUserTo(tUser, "11");

        // when say brains I am transported to the zombie maze
        successfully(doVerb(tUser,"say", "brains"));
        ResultOutput result = successfullyNavigatedTo("24", doVerb(tUser, "look", ""));

        int turns = 0;
        String locationId = "24";

        while(locationId.contentEquals("24") && turns<20){
            result = successfully(doVerb(tUser,"look", ""));
            locationId = result.look.location.locationId;
            turns++;
        }

        successfullyNavigatedTo("2", doVerb(tUser, "look", ""));

    }

    @Test
    public void canNavigateTheMaze() {
        String tUser = "zombieninja";

        game.getUserManager().add(new MudUser("ninja", tUser, "password"));

        // by teleporting directly into the maze I don't have to worry about zombies
        game.teleportUserTo(tUser, "24");

        String routes = "s:27, w:26, n:26, w:29, s:30, s:24";
        routes += ",s:27, e:29, s:30, s:24";
        routes += ",s:27, n:28, w:27, e:29, e:27, w:26, e:29";
        routes += ",n:26, s:29, w:27, s:29";
        routes += ",w:27, n:28, n:27, n:28, e:30, n:28, s:30, e:28, e:30, w:29";

        String[] route = routes.split(",");

        String currentLocation = "24";


        Map<String,String> traversed = new HashMap<>();

        traversed.put("24", "wne"); // we can't go these ways so fudge the results
        
        for(String aRoute : route){
            String []path = aRoute.trim().split(":");

            String dir = path[0];
            String to = path[1];

            String locExits = "";

            if(traversed.containsKey(currentLocation)){
                locExits = traversed.get(currentLocation);
            }

            successfully(doVerb(tUser,"go", dir));

            locExits+=dir;
            traversed.put(currentLocation,locExits);

            ResultOutput result = successfullyNavigatedTo(to, doVerb(tUser, "look", ""));
            currentLocation = to;
        }

        String locations[] = {"24", "26", "27", "28", "29", "30"};

        // check we visited all locations
        for(String loc : locations){
            System.out.println("Did we visit " + loc + "?");
            Assert.assertTrue("should have visited " + loc, traversed.containsKey(loc));

            String exits = traversed.get(loc);
            System.out.println("Took Exits - " + exits + ".");

            Assert.assertTrue(loc + " e?", exits.contains("e"));
            Assert.assertTrue(loc + " w?", exits.contains("w"));
            Assert.assertTrue(loc + " n?", exits.contains("n"));
            Assert.assertTrue(loc + " s?", exits.contains("s"));

        }


    }

    // ZOMBIE MAZE: TODOs
    // there are some puzzles to solve in the maze
    // allow examining the different things mentioned in the locations
        // pile of bones
        // gravestones
        // tombstones
            // say voodoo to jump to the voodoo drum
        // dead body
            // say groovy to jump to the voodoo drum
        // tomb


    @Test
    public void canExamineThingsInTheZombieMaze() {
        String tUser = "zombieexplorer";

        game.getUserManager().add(new MudUser("explorer", tUser, "password"));

        // by teleporting directly into the maze I don't have to worry about zombies
        game.teleportUserTo(tUser, "27");
        ResultOutput result = successfully(doVerb(tUser, "examine", "gravestones"));

        game.teleportUserTo(tUser, "28");
        result = successfully(doVerb(tUser, "examine", "tombstones"));

        game.teleportUserTo(tUser, "26");
        result = successfully(doVerb(tUser, "examine", "tomb"));

        game.teleportUserTo(tUser, "29");
        result = successfully(doVerb(tUser, "examine", "pileofbones"));

        game.teleportUserTo(tUser, "30");
        result = successfully(doVerb(tUser, "examine", "deadbody"));
        result = successfully(doVerb(tUser, "examine", "axeindeadbody"));
    }

    @Test
    public void canNavigateToTheDrumBySayingThings() {
        String tUser = "zombiedrummer";

        game.getUserManager().add(new MudUser("drummer", tUser, "password"));

        // by teleporting directly into the maze I don't have to worry about zombies
        game.teleportUserTo(tUser, "28");
        ResultOutput result = successfully(doVerb(tUser, "say", "voodoo"));

        result = successfullyNavigatedTo("25", doVerb(tUser, "look", ""));

        result = successfullyNavigatedTo("28", doVerb(tUser, "go", "n"));
        result = successfully(doVerb(tUser, "say", "voodoo"));
        result = successfullyNavigatedTo("28", doVerb(tUser, "go", "e"));

        result = successfullyNavigatedTo("30", doVerb(tUser, "go", "s"));

        result = successfully(doVerb(tUser, "say", "groovy"));
        result = successfullyNavigatedTo("25", doVerb(tUser, "look", ""));
        result = successfullyNavigatedTo("30", doVerb(tUser, "go", "w"));
    }

    @Test
    public void canStopTheZombiesByUsingTheDrum() {
        String tUser = "zombiedrummer1";

        game.getUserManager().add(new MudUser("drummer", tUser, "password"));

        game.teleportUserTo(tUser, "11");

        // when say brains I am transported to the zombie maze
        successfully(doVerb(tUser,"say", "brains"));
        ResultOutput result = successfullyNavigatedTo("24", doVerb(tUser, "look", ""));

        result = successfullyNavigatedTo("27", doVerb(tUser, "go", "s"));
        result = successfullyNavigatedTo("28", doVerb(tUser, "go", "n"));
        result = successfullyNavigatedTo("25", doVerb(tUser, "say", "voodoo"));

        result = successfully(doVerb(tUser,"score",""));
        Integer score = result.users.get(0).score;

        // use drum and score increases by 100
        result = successfully(doVerb(tUser, "use", "voodoodrum"));
        result = successfully(doVerb(tUser,"score",""));
        Integer newscore = result.users.get(0).score;
        Assert.assertEquals( Integer.valueOf(score + 100),newscore);

        score = newscore;

        // use drum again and score does not increase
        result = successfully(doVerb(tUser, "use", "voodoodrum"));
        result = successfully(doVerb(tUser,"score",""));
        newscore = result.users.get(0).score;
        Assert.assertEquals( Integer.valueOf(score),newscore);

        // so long as I keep using the drum the zombies don't get me
        for(int x=0; x<100;x++) {
            result = successfully(doVerb(tUser, "use", "voodoodrum"));
            result = successfully(doVerb(tUser, "score", ""));
            newscore = result.users.get(0).score;
            Assert.assertEquals(Integer.valueOf(score), newscore);
        }
        
        // but if I stop then they do
        for(int x=0; x<20;x++) {
            result = successfully(doVerb(tUser, "look", ""));
            result = successfully(doVerb(tUser, "score", ""));
            newscore = result.users.get(0).score;
        }

        Assert.assertTrue("Score should have been reduced when eaten by zombies", newscore < score);

    }

    @Test
    public void canGetPointsWhenStopZombiesInTheNickOfTime() {
        String tUser = "zombiedrummer2";

        game.getUserManager().add(new MudUser("drummer", tUser, "password"));

        game.teleportUserTo(tUser, "11");

        // when say brains I am transported to the zombie maze
        successfully(doVerb(tUser,"say", "brains"));
        ResultOutput result = successfullyNavigatedTo("24", doVerb(tUser, "look", ""));

        result = successfullyNavigatedTo("27", doVerb(tUser, "go", "s"));
        result = successfullyNavigatedTo("28", doVerb(tUser, "go", "n"));
        result = successfullyNavigatedTo("25", doVerb(tUser, "say", "voodoo"));
        result = successfully(doVerb(tUser, "use", "voodoodrum"));


        Integer score = game.getUserManager().getUser(tUser).getScore();

        // use drum in nick of time @ 17, 18 and score increases by 50
        for(int x=0; x<16;x++) {
            result = successfully(doVerb(tUser, "look", ""));
        }
        result = successfully(doVerb(tUser, "use", "voodoodrum"));
        int newscore = game.getUserManager().getUser(tUser).getScore();

        Assert.assertTrue("Score increased due to stunt work", newscore == score+50);


        score = newscore;
        for(int x=0; x<17;x++) {
            result = successfully(doVerb(tUser, "look", ""));
        }
        result = successfully(doVerb(tUser, "use", "voodoodrum"));
        newscore = game.getUserManager().getUser(tUser).getScore();

        Assert.assertTrue("Score increased due to stunt work", newscore == score+50);


        // last minute score 500 @ 19
        score = newscore;
        for(int x=0; x<18;x++) {
            result = successfully(doVerb(tUser, "look", ""));
        }
        result = successfully(doVerb(tUser, "use", "voodoodrum"));
        newscore = game.getUserManager().getUser(tUser).getScore();

        Assert.assertTrue("Score increased due to stunt work", newscore == score+500);


    }

    @Test
    public void canCreateTreauresInGraveyard() {

        String tUser = "zombietreasure";

        game.getUserManager().add(new MudUser("treasurehunter", tUser, "password"));

        game.teleportUserTo(tUser, "26");

        ResultOutput result = successfully(doVerb(tUser, "look", ""));

        result = successfully(doVerb(tUser, "examine", "tomb"));


        int treasureCount = game.getGameCollectables().count();

        result = successfully(doVerb(tUser, "say", "moneygod"));

        int treasureCountNow = game.getGameCollectables().count();

        Assert.assertTrue("treasure count should increase", treasureCountNow > treasureCount);

        treasureCount = treasureCountNow;

        for(int x=0; x<14; x++) {
            result = successfully(doVerb(tUser, "say", "moneygod"));
            Assert.assertEquals("treasure count should not increase", treasureCountNow, game.getGameCollectables().count());
        }
        result = successfully(doVerb(tUser, "look", ""));
        result = successfully(doVerb(tUser, "say", "moneygod"));

        treasureCountNow = game.getGameCollectables().count();
        Assert.assertTrue("treasure count should increase", treasureCountNow > treasureCount);



    }

    // TODO: create a set of rooms off the dark corridor 7 with a treasure generator and a hoard

    @Test
    public void canNavigateAroundTheStarRooms() {

        String tUser = "starexplorer";

        MudUser thisUser = game.getUserManager().add(new MudUser(tUser, tUser, "password"));

        game.teleportUserTo(tUser, "31");

        ResultOutput result = successfullyNavigatedTo("32", doVerb(tUser, "go", "n"));
        result = successfullyNavigatedTo("34", doVerb(tUser, "go", "w"));
        result = successfullyNavigatedTo("32", doVerb(tUser, "go", "e"));
        result = successfullyNavigatedTo("35", doVerb(tUser, "go", "e"));
        result = successfullyNavigatedTo("32", doVerb(tUser, "go", "w"));
        result = successfullyNavigatedTo("33", doVerb(tUser, "go", "n"));


        result = successfully(doVerb(tUser, "take", "shinystar"));

        int score = thisUser.getScore();

        result = successfullyNavigatedTo("32", doVerb(tUser, "go", "s"));

        result = successfully(doVerb(tUser, "hoard", "shinystar"));

        int newscore = thisUser.getScore();

        Assert.assertEquals("score should have increased", score+200, newscore);

        result = successfullyNavigatedTo("31", doVerb(tUser, "go", "s"));
        result = successfullyNavigatedTo("7", doVerb(tUser, "go", "s"));


    }

    @Test
    public void canGenerateMoneyAroundTheStarRoomsByUsingSign() {

        String tUser = "staruser";

        MudUser thisUser = game.getUserManager().add(new MudUser(tUser, tUser, "password"));

        game.teleportUserTo(tUser, "33");

        ResultOutput result = successfully(doVerb(tUser, "examine", "starsign"));

        int itemsCount = game.getGameCollectables().count();



        // - - generate
        result = successfully(doVerb(tUser, "use", "starsign"));
        int newItemsCount = game.getGameCollectables().count();
        Assert.assertTrue(String.format("expected more treasures: was %d and is now %d",itemsCount, newItemsCount), newItemsCount > itemsCount);
        itemsCount = newItemsCount;

        // 0  - no
        result = successfully(doVerb(tUser, "use", "starsign"));
        Assert.assertEquals(newItemsCount, game.getGameCollectables().count());


        // 1  - no
        result = successfully(doVerb(tUser, "use", "starsign"));
        Assert.assertEquals(newItemsCount, game.getGameCollectables().count());

        // 2  - no
        result = successfully(doVerb(tUser, "use", "starsign"));
        Assert.assertEquals(newItemsCount, game.getGameCollectables().count());

        // 3  - yes
        result = successfully(doVerb(tUser, "use", "starsign"));
        newItemsCount = game.getGameCollectables().count();
        Assert.assertTrue(String.format("expected more treasures: was %d and is now %d",itemsCount, newItemsCount), newItemsCount > itemsCount);
        itemsCount = newItemsCount;

        // -3  no
        result = successfully(doVerb(tUser, "use", "starsign"));
        Assert.assertEquals(itemsCount, game.getGameCollectables().count());

        // -2 no
        result = successfully(doVerb(tUser, "use", "starsign"));
        Assert.assertEquals(itemsCount, game.getGameCollectables().count());

        // -1  no
        result = successfully(doVerb(tUser, "use", "starsign"));
        Assert.assertEquals(itemsCount, game.getGameCollectables().count());

        //0   no
        result = successfully(doVerb(tUser, "use", "starsign"));
        Assert.assertEquals(itemsCount, game.getGameCollectables().count());

        // 1
        result = successfully(doVerb(tUser, "use", "starsign"));
        Assert.assertEquals(itemsCount, game.getGameCollectables().count());

        //2
        result = successfully(doVerb(tUser, "use", "starsign"));
        Assert.assertEquals(itemsCount, game.getGameCollectables().count());

        //3
        result = successfully(doVerb(tUser, "use", "starsign"));
        newItemsCount = game.getGameCollectables().count();
        Assert.assertTrue(String.format("expected more treasures: was %d and is now %d",itemsCount, newItemsCount), newItemsCount > itemsCount);
        itemsCount = newItemsCount;

    }

    @Test
    public void canEnterStarRoomsByPressingAButton() {

        String tUser = "staruser";

        MudUser thisUser = game.getUserManager().add(new MudUser(tUser, tUser, "password"));

        game.teleportUserTo(tUser, "7");

        ResultOutput result = successfully(doVerb(tUser, "examine", "starshapedbutton"));
        result = successfullyNavigatedTo("31", doVerb(tUser, "use", "starshapedbutton"));
    }

    @Test
    public void canLearnAboutStarRoomsByExaminingSign() {

        String tUser = "starusersignreader";

        MudUser thisUser = game.getUserManager().add(new MudUser(tUser, tUser, "password"));

        game.teleportUserTo(tUser, "9");

        ResultOutput result = successfully(doVerb(tUser, "examine", "starshapedsign"));

    }


    @Test
    public void canScorePointsInRestRoomByPostingExamineSigns(){

        String tUser = "postuser";

        MudUser thisUser = game.getUserManager().add(new MudUser(tUser, tUser, "password"));

        game.teleportUserTo(tUser, "37");

        successfully(dsl.doVerb(tUser, "examine", "postexplain", new RestMudHttpRequestDetails("get")));

        int score = thisUser.getScore();

        failTo(dsl.doVerb(tUser, "examine", "postsign", new RestMudHttpRequestDetails("get")));

        Assert.assertEquals(score, thisUser.getScore());

        successfully(dsl.doVerb(tUser, "examine", "postsign", new RestMudHttpRequestDetails("post")));

        Assert.assertTrue(thisUser.getScore() > score);

        // but I can only do that once

        score = thisUser.getScore();
        failTo(dsl.doVerb(tUser, "examine", "postsign", new RestMudHttpRequestDetails("post")));

        Assert.assertTrue(thisUser.getScore() == score);


    }

    @Test
    public void canNavigateAndLearnAboutTheRestRooms() {

        String tUser = "restroomexplorer";

        MudUser thisUser = game.getUserManager().add(new MudUser(tUser, tUser, "password"));

        game.teleportUserTo(tUser, "2");

        successfullyNavigatedTo("36", doVerb(tUser, "go","e"));

        successfully(doVerb(tUser,"examine", "restroomsexplained"));

        successfullyNavigatedTo("37", doVerb(tUser, "go","e"));

        successfully(doVerb(tUser,"examine", "postexplain"));
        successfully(doVerb(tUser,"examine", "postheaderexplain"));

        successfullyNavigatedTo("38", doVerb(tUser, "go","e"));
        successfully(doVerb(tUser,"examine", "getheaderexplain"));

        successfullyNavigatedTo("39", doVerb(tUser, "go","e"));
        successfully(doVerb(tUser,"examine", "fightsmalltrollsign"));

        successfullyNavigatedTo("40", doVerb(tUser, "go","n"));
        successfully(doVerb(tUser,"examine", "fightlargetrollsign"));


        // and back to the start
        successfullyNavigatedTo("39", doVerb(tUser, "go","s"));
        successfullyNavigatedTo("38", doVerb(tUser, "go","w"));
        successfullyNavigatedTo("37", doVerb(tUser, "go","w"));
        successfullyNavigatedTo("36", doVerb(tUser, "go","w"));
        successfullyNavigatedTo("2", doVerb(tUser, "go","w"));


    }

    @Test
    public void canScorePointsInRestRoomByPostingWithCustomHeaderExamineSigns(){

        String tUser = "postuserexplained";

        MudUser thisUser = game.getUserManager().add(new MudUser(tUser, tUser, "password"));

        game.teleportUserTo(tUser, "37");

        successfully(dsl.doVerb(tUser, "examine", "postheaderexplain", new RestMudHttpRequestDetails("get")));

        int score = thisUser.getScore();

        failTo(dsl.doVerb(tUser, "examine", "postheader", new RestMudHttpRequestDetails("get")));

        Assert.assertEquals(score, thisUser.getScore());

        failTo(dsl.doVerb(tUser, "examine", "postheader", new RestMudHttpRequestDetails("post")));
        Assert.assertEquals(score, thisUser.getScore());

        HashSet<String> headers = new HashSet<String>();
        RestMudHttpRequestDetails specialRequest = new RestMudHttpRequestDetails(headers, "post");

        failTo(dsl.doVerb(tUser, "examine", "postheader", specialRequest));
        Assert.assertEquals(score, thisUser.getScore());


        headers = new HashSet<String>();
        headers.add("X-REST-MUD-POST-EXAMINE:yes");
        specialRequest = new RestMudHttpRequestDetails(headers, "post");

        successfully(dsl.doVerb(tUser, "examine", "postheader", specialRequest));

        Assert.assertTrue(thisUser.getScore() > score);

        // but I can only do that once

        score = thisUser.getScore();
        failTo(dsl.doVerb(tUser, "examine", "postheader", specialRequest));

        Assert.assertTrue(thisUser.getScore() == score);
    }

    @Test
    public void canScorePointsInRestRoomByGETWithCustomHeaderExamineSigns(){

        String tUser = "getuserexplained";

        MudUser thisUser = game.getUserManager().add(new MudUser(tUser, tUser, "password"));

        game.teleportUserTo(tUser, "38");

        successfully(dsl.doVerb(tUser, "examine", "getheaderexplain", new RestMudHttpRequestDetails("get")));

        int score = thisUser.getScore();

        failTo(dsl.doVerb(tUser, "examine", "getheader", new RestMudHttpRequestDetails("get")));

        Assert.assertEquals(score, thisUser.getScore());

        HashSet<String> headers = new HashSet<String>();
        RestMudHttpRequestDetails specialRequest = new RestMudHttpRequestDetails(headers, "get");

        failTo(dsl.doVerb(tUser, "examine", "getheader", specialRequest));
        Assert.assertEquals(score, thisUser.getScore());


        headers = new HashSet<String>();
        headers.add("X-REST-MUD-GET-EXAMINE:yes");
        specialRequest = new RestMudHttpRequestDetails(headers, "get");

        successfully(dsl.doVerb(tUser, "examine", "getheader", specialRequest));

        Assert.assertTrue(thisUser.getScore() > score);

        // but I can only do that once

        score = thisUser.getScore();
        failTo(dsl.doVerb(tUser, "examine", "getheader", specialRequest));

        Assert.assertTrue(thisUser.getScore() == score);
    }

    @Test
    public void canScorePointsInRestRoomByDELETEandFightSmallTroll(){

        String tUser = "trollfighter";

        MudUser thisUser = game.getUserManager().add(new MudUser(tUser, tUser, "password"));

        game.teleportUserTo(tUser, "39");

        successfully(dsl.doVerb(tUser, "examine", "fightsmalltrollsign", new RestMudHttpRequestDetails("get")));

        int score = thisUser.getScore();

        successfully(dsl.doVerb(tUser, "look", "", new RestMudHttpRequestDetails("get")));


        failTo(dsl.doVerb(tUser, "take", "smalltroll", new RestMudHttpRequestDetails("get")));
        Assert.assertEquals(score, thisUser.getScore());

        failTo(dsl.doVerb(tUser, "fight", "smalltroll", new RestMudHttpRequestDetails("get")));
        Assert.assertEquals(score, thisUser.getScore());


        HashSet<String> headers = new HashSet<String>();
        RestMudHttpRequestDetails specialRequest = new RestMudHttpRequestDetails(headers, "delete");

        successfully(dsl.doVerb(tUser, "fight", "smalltroll", specialRequest));
        Assert.assertTrue(thisUser.getScore()>score);
        score = thisUser.getScore();

/*
        headers = new HashSet<String>();
        headers.add("X-REST-MUD-GET-EXAMINE:yes");
        specialRequest = new RestMudHttpRequestDetails(headers, "get");

        successfully(dsl.doVerb(tUser, "examine", "getheader", specialRequest));

        Assert.assertTrue(thisUser.getScore() > score);
*/

        // but I can only do that once


        failTo(dsl.doVerb(tUser, "fight", "smalltroll", specialRequest));

        Assert.assertTrue(thisUser.getScore() == score);

        successfully(dsl.doVerb(tUser, "look", "", new RestMudHttpRequestDetails("get")));
        successfully(dsl.doVerb(tUser, "score", "", new RestMudHttpRequestDetails("get")));
    }

    @Test
    public void canScorePointsInRestRoomByDELETEandFightLargeTroll(){

        String tUser = "trollfightersequel";

        MudUser thisUser = game.getUserManager().add(new MudUser(tUser, tUser, "password"));

        game.teleportUserTo(tUser, "40");

        successfully(dsl.doVerb(tUser, "examine", "fightlargetrollsign", new RestMudHttpRequestDetails("get")));

        int score = thisUser.getScore();

        successfully(dsl.doVerb(tUser, "look", "", new RestMudHttpRequestDetails("get")));


        failTo(dsl.doVerb(tUser, "take", "largetroll", new RestMudHttpRequestDetails("get")));
        Assert.assertEquals(score, thisUser.getScore());

        failTo(dsl.doVerb(tUser, "fight", "largetroll", new RestMudHttpRequestDetails("get")));
        Assert.assertEquals(score, thisUser.getScore());


        HashSet<String> headers = new HashSet<String>();
        RestMudHttpRequestDetails specialRequest = new RestMudHttpRequestDetails(headers, "delete");

        failTo(dsl.doVerb(tUser, "fight", "largetroll", specialRequest));
        Assert.assertEquals(score, thisUser.getScore());



        headers = new HashSet<String>();
        headers.add("X-SUPER-FIGHTING-SKILLS-VERY-HARD:yes");
        specialRequest = new RestMudHttpRequestDetails(headers, "delete");

        successfully(dsl.doVerb(tUser, "fight", "largetroll", specialRequest));

        Assert.assertTrue(thisUser.getScore() > score);
        score = thisUser.getScore();

        // but I can only do that once


        failTo(dsl.doVerb(tUser, "fight", "largetroll", specialRequest));

        Assert.assertTrue(thisUser.getScore() == score);

        successfully(dsl.doVerb(tUser, "look", "", new RestMudHttpRequestDetails("get")));
        successfully(dsl.doVerb(tUser, "score", "", new RestMudHttpRequestDetails("get")));
    }

    // TODO: create a set of REST Rooms with 'puzzles' involving POST, GET, DELETE, PUT messages, could we do one with custom headers?
    // TODO: add a set of REST Rooms into the Zombie zork and release Zombie Zork as a download for Let's Test


        private ResultOutput successfullyNavigatedTo(String locationId, ResultOutput resultOutput) {
        Assert.assertEquals(locationId, resultOutput.look.location.locationId);
        return resultOutput;
    }


}
