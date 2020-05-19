package uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions;

import uk.co.compendiumdev.restmud.engine.game.gamedefinition.GameDefinitionPopulator;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.locations.GateDirection;
import uk.co.compendiumdev.restmud.engine.game.locations.GateStatus;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.scripting.PriorityTurnCondition;
import uk.co.compendiumdev.restmud.engine.game.scripting.VerbCondition;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectableTemplate;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;
import uk.co.compendiumdev.restmud.engine.game.verbs.VerbGameAbilities;

/**
 * Created by Alan on 17/09/2017.
 */
public class TreasureHuntBasicGameDefinitionGenerator implements GameDefinitionPopulator {
    public void define(MudGameDefinition defn) {

        MudGameEntityCreator create = defn.creator();

        int total_score = 0;

        defn.setGameName("Treasure Hunt Game");
        defn.setWelcomeMessage("Welcome to the Treasure Hunt Adventure Game");

        MudLocationObject locationObj;

        MudLocation startLocation = new MudLocation("1","The Central Room", "This is the room in the center", "N:2,E:3,S:4,W:5");
        defn.setStartLocationId("1");


        // There is a valuable dispenser of jewels in room 22
        // there is a mega cloth dispenser in room 9
        // there is always a hoard in room 1
        // there are basic jewel and cloth dispensers in rooms 23 and 12

        // by default the system junk room is at "0"
        defn.gameLocations().addLocation(new MudLocation("0","The Junk Room", "This is the room of Junk", "N:10"));
        startLocation.setCanHoardTreasureHere(true);
        defn.gameLocations().addLocation(startLocation);
        defn.gameLocations().addLocation(new MudLocation("2","The North Room", "This is the room in the north", "S:1,N:12,E:36"));
        defn.gameLocations().addLocation(new MudLocation("3","The East Room", "This is the room in the east", "W:1,E:11:eastgate,N:15:secret,S:local"));
        // used as a simple door between 3 East and 11 West
        defn.addGate("eastgate", GateStatus.CLOSED);

        defn.gameLocations().addLocation(new MudLocation("4","The South Room", "This is the room in the South, there is a broken teleporter to the south", "N:1,S:local"));
        defn.gameLocations().addLocation(new MudLocation("5","The West Room", "This is the room in the West", "E:1,W:6"));
        defn.gameLocations().addLocation(new MudLocation("6","The West Corridor", "This is the eastern end of the western corridor in the West that continues West and East", "E:5,W:7,S:22"));
        defn.gameLocations().addLocation(new MudLocation("7","The Further West Corridor", "This is the middle of the western corridor in the West", "E:6,W:8").makeDark());
        defn.gameLocations().addLocation(new MudLocation("8","The Furtherest West Corridor", "This is the western end of the western corridor in the West, it continues E, and a teleporter is to the West", "E:7,W:local"));
        defn.gameLocations().addLocation(new MudLocation("9","The Furtherest East Corridor", "This is the eastern end of the eastern corridor in the East", "W:10"));
        defn.gameLocations().addLocation(new MudLocation("10","The Further East Corridor", "This is the middle of the eastern corridor in the East", "E:9,W:11,S:0:secret"));
        defn.gameLocations().addLocation(new MudLocation("11","The East Corridor", "This is the western end of the eastern corridor in the East", "E:10,W:3:eastgate"));
        defn.gameLocations().addLocation(new MudLocation("12","The Northern Room", "This is the middle of the room in the north of the North room", "S:2,W:13,E:14"));
        defn.gameLocations().addLocation(new MudLocation("13","The Northern Room (West)", "This is the west of the middle of the room in the north", "S:2,E:12"));

        // TODO: I don't think I created any tests for the secret clock panel
        defn.gameLocations().addLocation(new MudLocation("14","The Northern Room (east)",
                "This is the east of the middle of the room in the north", "S:2,W:12,N:23,E:16:secret:secretClockPanel"));


        defn.gameLocations().addLocation(new MudLocation("15","A Secret Room", "This is a secret room, ssh, don't tell anyone", "S:3"));
        defn.gameLocations().addLocation(new MudLocation("16","The Secret Clock Room",
                "This is the secret clock room. There are no clocks here.", "W:14"));
        defn.gameLocations().addLocation(new MudLocation("17","The Secret Room North of 12", "This is the secret room north of 12 that everyone forgets about.", "S:12"));
        defn.gameLocations().get("17").canHoardTreasureHere();

        defn.gameLocations().addLocation(new MudLocation("22","The Jewellery room", "This is the room of jewels.", "N:6"));
        defn.gameLocations().addLocation(new MudLocation("23","A Jewellery room", "This is a room of jewels.", "S:14"));
        create.location("24", "The Start of The Zombie Graveyard", "You are at the start of the zombie graveyard you can see a gate to the North that is rusted shut and will not open, ever. The graveyard is misty but you can see a path heading South", "S:27");
        create.location("25", "A Drummers Tomb", "You are in a tomb beside a drum", "N:28,S:30,W:30,E:28");
        create.location("26", "A Tomb", "You are waist deep in mist in a Zombie Graveyard and can make out a tomb", "N:26,S:29,W:29,E:29");
        create.location("27", "A Set of Gravestones", "You are knee deep in mist in a Zombie Graveyard and can make out a set of gravestones", "N:28,S:29,W:26,E:29");
        create.location("28", "Tombstones in the mist", "You are neck deep in mist in a Zombie Graveyard and can make out the top of some tombstones", "N:27,S:30,W:27,E:30");
        create.location("29", "A Pile of Bones", "The mist is over your head and you can just see your feet, you are standing on a pile of bones", "N:26,S:30,W:27,E:27");
        create.location("30", "An Axe Incident", "The mist has cleared and you can see a shallow open grave with a dead body and an axe in its head", "N:28,S:24,W:29,E:28");
        create.location("31", "South Star", "The southern most room of star rooms", "N:32,S:7");
        create.location("32", "Mid Star", "The most central room of star rooms", "N:33,S:31,E:35,W:34").setCanHoardTreasureHere(true);
        create.location("33", "North Star", "The most northern room of star rooms", "S:32");
        create.location("34", "West Star", "The most western room of star rooms", "E:32");
        create.location("35", "East Star", "The most eastern room of star rooms", "W:32");
        create.location("36", "REST Room Entrance", "In each of the REST rooms will be signs telling you what api requests to perform.", "e:37,w:2");
        create.location("37", "REST POST Room", "The Rest Rooms POST Room", "w:36,e:38");
        create.location("38", "REST GET Room", "The Rest Rooms GET Room", "w:37,e:39");
        create.location("39", "REST DELETE Room (small)", "The Rest Rooms small DELETE Room", "w:38,N:40");
        create.location("40", "REST DELETE Room (large)", "The Rest Rooms large DELETE Room", "s:39");


        VerbCondition cond = null;
        PriorityTurnCondition turn = null;


        // getting to puzzleland is a puzzle
        locationObj = new MudLocationObject("puzzlelandsign","A sign on the wall shaped like a question mark", "To enter puzzle land you have to be able to say puzzle.");
        defn.addLocationObjectIn(locationObj, "3");

        defn.addVerb("say");

        cond = new VerbCondition("say");
        cond.when(When.NOUNPHRASE_EQUALS, "puzzle").
                andWhen(When.LOCATION_ID_IS,"3").
                andWhen(When.DOES_FLAG_EXIST, "userCanEnterPuzzleLand:false").
                then(Then.SET_USER_FLAG, "userCanEnterPuzzleLand:true").
                then(Then.INCREMENT_SCORE,"100").
                then(Then.LAST_ACTION_SUCCESS, "You say 'puzzle' and a shimmery door opens to the south allowing you entry to puzzle land.");
        defn.addCondition(cond);

        cond = new VerbCondition("say");
        cond.when(When.NOUNPHRASE_EQUALS, "puzzle").
                andWhen(When.LOCATION_ID_IS,"3").
                andWhen(When.DOES_FLAG_EXIST, "userCanEnterPuzzleLand:true").
                then(Then.LAST_ACTION_ERROR, "You try to say 'puzzle' but you can't say the phrase, it is almost as if you have said it before and are tired of repeating yourself.");
        defn.addCondition(cond);


        cond = new VerbCondition("go");
        cond.when(When.DIRECTION, "s").
                andWhen(When.LOCATION_ID_IS, "3").
                andWhen(When.DOES_FLAG_EXIST,"userCanEnterPuzzleLand:true").
                then(Then.TELEPORT_PLAYER_TO_LOCATION, "18").
                then(Then.LAST_ACTION_SUCCESS,"You enter puzzle land.");
        defn.addCondition(cond);

        /*
            Puzzle Corridor  has rooms off it with self contained puzzles for points
            Each Puzzle can only be completed once by each player

            vase puzzle - a monkey hand trap
               - sign in vase puzzle room "treasure in vase, one per player"
               - take treasure - but it is in the vase
               - examine vase - at the bottom of vase is a treasure, and you want it
               - take vase
               - take treasure - the opening of the vase is too small to get the treasure out
               - go n in 20 - to the hammer room
               - there is a hammer chained to the wall
               - use hammer - without having vase - use it to do what? it isn't hammering time unless there is something to hammer
               - use hammer - when having vase then it smashes and you get treasure

         */
        defn.gameLocations().addLocation(new MudLocation("18","Puzzle Land Entrance", "This is the Entrance to Puzzle Land.", "N:3,S:19"));
        defn.gameLocations().addLocation(new MudLocation("19","Puzzle Corridor", "The corridor of puzzles where each room offers new challenges, should you choose to chase a high score through your cleverness", "N:18,S:20"));
        defn.gameLocations().addLocation(new MudLocation("20","Hammer Room", "This is a room with a hammer.", "N:19,S:21"));
        defn.gameLocations().addLocation(new MudLocation("21","Vase Puzzle Room", "This is a room with whole bunch of vases (and treasure).", "N:20"));

        locationObj = new MudLocationObject("hammer","A hammer chained to the floor", "Someone has chained a heavy hammer to the floor, I could probably use that to smash something, if I had something to smash.");
        defn.addLocationObjectIn(locationObj, "20");



        // can go west from 8 and teleport to room 9 and score points
        cond = new VerbCondition("go");
        cond.when(When.DIRECTION, "w").
                andWhen(When.LOCATION_ID_IS, "8").
                then(Then.INCREMENT_SCORE,"10").
                then(Then.TELEPORT_PLAYER_TO_LOCATION, "9").
                then(Then.LAST_ACTION_SUCCESS,"You walk west and realise you just stepped into a transporter. <ZAP> <PFIZZ> <TWAZZAP>.");
        defn.addCondition(cond);


        // when not picked up a vase, say you can't
        cond = new VerbCondition("use");
        cond.when(When.NOUNPHRASE_EQUALS, "hammer").
                andWhen(When.LOCATION_ID_IS,"20").
                andWhen(When.DOES_FLAG_EXIST, "userHasPickedUpAVase:false").
                then(Then.LAST_ACTION_ERROR, "You could use the hammer, but you have nothing to smash. So you decide not to do that - why waste your energy?");
        defn.addCondition(cond);

        // with a vase and a hammer, smash the vase
        cond = new VerbCondition("use");
        cond.when(When.NOUNPHRASE_EQUALS, "hammer").
                andWhen(When.LOCATION_ID_IS,"20").
                andWhen(When.DOES_FLAG_EXIST, "userHasPickedUpAVase:true").
                andWhen(When.DOES_FLAG_EXIST, "userIsCarryingAAVase:true").
                then(Then.INCREMENT_SCORE,"100")
                .andThen(Then.DELETE_USER_FLAG,"userIsCarryingAAVase").
                then(Then.LAST_ACTION_SUCCESS, "You use the hammer to smash the vase and your greedy eyes spy the treasure. You watch the treasure roll off to the side of the room and down a hole. What? Such a disappointment. Then you hear laughter and a voice says 'well done adventurer, you have scored some points'.");
        defn.addCondition(cond);

        // if not carrying a vase then - cannot use hammer twice
        cond = new VerbCondition("use");
        cond.when(When.NOUNPHRASE_EQUALS, "hammer").
                andWhen(When.LOCATION_ID_IS,"20").
                andWhen(When.DOES_FLAG_EXIST, "userHasPickedUpAVase:true").
                andWhen(When.DOES_FLAG_EXIST, "userIsCarryingAAVase:false").
                then(Then.INCREMENT_SCORE,"-10").
                then(Then.LAST_ACTION_ERROR, "You are not carrying a vase, were you trying to score points again? Oh Oh. Points removed for trying to cheat. What are you? A Tester?");
        defn.addCondition(cond);

        // can examine the sign
        locationObj = new MudLocationObject("vasePuzzleSign","A Sign On The Wall Of the Vase Puzzle Room", "Every <span collectableid='prizevase'>vase</span> has a prize. But only one per player.");
        defn.addLocationObjectIn(locationObj, "21");

        cond = new VerbCondition("take");
        cond.when("nounphrase", "prizevase").
                andWhen(When.LOCATION_ID_IS,"21").
                andWhen(When.DOES_FLAG_EXIST, "userHasPickedUpAVase:false").
                then(Then.SET_USER_FLAG, "userHasPickedUpAVase:true").
                then(Then.SET_USER_FLAG, "userIsCarryingAAVase:true").
                then(Then.LAST_ACTION_SUCCESS, "You look around and try to pick the best vase, but they all look the same, so you pick the first one that you find.");
        defn.addCondition(cond);

        // examine vase when not carrying one
        cond = new VerbCondition("examine");
        cond.when("nounphrase", "prizevase").
                andWhen(When.LOCATION_ID_IS,"21").
                andWhen(When.DOES_FLAG_EXIST, "userIsCarryingAAVase:false").
                then(Then.LAST_ACTION_SUCCESS, "All the vases look the same, and glinting at the bottom of each is some sort of <span collectableid='prizevasetreasure'>treasure</span>.");
        defn.addCondition(cond);

        // examine vase when taking it
        cond = new VerbCondition("examine");
        cond.when("nounphrase", "prizevase").
                andWhen(When.LOCATION_ID_IS,"21").
                andWhen(When.DOES_FLAG_EXIST, "userIsCarryingAAVase:true").
                then(Then.LAST_ACTION_SUCCESS, "You are sure that glinting at the bottom of your vase is the most valuable <span collectableid='prizevasetreasure'>treasure</span>.");
        defn.addCondition(cond);

        // fail to take a vase twice
        cond = new VerbCondition("take");
        cond.when("nounphrase", "prizevase").
                andWhen(When.LOCATION_ID_IS,"21").
                andWhen(When.DOES_FLAG_EXIST, "userHasPickedUpAVase:true").
                then(Then.INCREMENT_SCORE,"-5").
                then(Then.LAST_ACTION_ERROR, "You already picked one up, the sign said only one per player - boo to you for trying to cheat. You lost some points you cheater.");
        defn.addCondition(cond);

        // fail to take treasure without having already taken a vase
        cond = new VerbCondition("take");
        cond.when("nounphrase", "prizevasetreasure").
                andWhen(When.LOCATION_ID_IS,"21").
                andWhen(When.DOES_FLAG_EXIST, "userHasPickedUpAVase:false").
                then(Then.LAST_ACTION_ERROR, "The treasures are in the vases, you can't just take a treasure, you really need to take a vase first.");
        defn.addCondition(cond);

        // fail to take treasure after having taken a vase
        cond = new VerbCondition("take");
        cond.when("nounphrase", "prizevasetreasure").
                andWhen(When.LOCATION_ID_IS,"21").
                andWhen(When.DOES_FLAG_EXIST, "userHasPickedUpAVase:true").
                andWhen(When.DOES_FLAG_EXIST, "userIsCarryingAAVase:true").
                then(Then.LAST_ACTION_ERROR, "You grab the treasure at the bottom of the vase, but you can't get your hand out of the vase while holding the treasure. You can take your hand out if you leave the treasure behind, but nah, that's not going to happen.");
        defn.addCondition(cond);

        // can drop a vase if carrying it
        cond = new VerbCondition("drop");
        cond.when("nounphrase", "prizevase").
                andWhen(When.DOES_FLAG_EXIST, "userHasPickedUpAVase:true").
                andWhen(When.DOES_FLAG_EXIST, "userIsCarryingAAVase:true").
                then(Then.DELETE_USER_FLAG,"userIsCarryingAAVase").
                andThen(Then.DELETE_USER_FLAG,"userHasPickedUpAVase").
                then(Then.LAST_ACTION_SUCCESS, "You drop the vase (I assume you are trying to smash it to get the treasure). But the vase vanishes before it hits the floor. So now you have no vase, no smashed vase, and no treasure from the smashed vase. There must be another way to smash the vase and get the treasure.");
        defn.addCondition(cond);


        // can not drop a vase if not carrying it
        cond = new VerbCondition("drop");
        cond.when("nounphrase", "prizevase").
                andWhen(When.DOES_FLAG_EXIST, "userIsCarryingAAVase:false").
                then(Then.LAST_ACTION_ERROR, "You are not carrying a vase to drop.");
        defn.addCondition(cond);

        // fail to take treasure after having already taken a vase, but no longer carrying one
        cond = new VerbCondition("take");
        cond.when("nounphrase", "prizevasetreasure").
                andWhen(When.LOCATION_ID_IS,"21").
                andWhen(When.DOES_FLAG_EXIST, "userHasPickedUpAVase:true").
                andWhen(When.DOES_FLAG_EXIST, "userIsCarryingAAVase:false").
                then(Then.LAST_ACTION_ERROR, "The treasures are in the vases, you can't just take a treasure, you should already know that you need to take a vase first. Haven't you been taking notes?");
        defn.addCondition(cond);

        /*
           Bomb Puzzle
           - take smallroundthing
           - explodes after time
           - defuse smallroundthing - for points before explosion
         */
        turn = new PriorityTurnCondition();
        turn.when("locationId","2").
                andWhen("doesFlagExist", "userHasPickedUpABomb:false").
                then("message", "You can see <span id='smallroundthing' class='collectable'>a small round thing</span> on the floor");
        defn.addCondition(turn);

        turn = new PriorityTurnCondition();
        turn.when("isFlagSet", "userHasPickedUpABomb:true").
                andWhen("isCounterValue", "userPickedUpBombCountDown:>:0").
                then("message", "The small round thing with the words 'tick' and 'bomb' is ticking loudly").
                andThen("incrementCounter", "userPickedUpBombCountDown:-1");
        defn.addCondition(turn);

        // when counter == zero have the bomb vanish in a puff of smoke -
        // lucky you didn't panic and try and defuse the bomb you might have got yourself killed, have 3 points
        turn = new PriorityTurnCondition();
        turn.when("isFlagSet", "userHasPickedUpABomb:true").
                andWhen("isCounterValue","userPickedUpBombCountDown:<=:0").
                then("setUserFlag", "userHasPickedUpABomb:false").
                then("deleteUserCounter", "userPickedUpBombCountDown").
                andThen("message", "The small round thing with the words 'tick' and 'bomb' vanishes in a puff of smoke - lucky you didn't panic and try and defuse the bomb, or you might have blown yourself up").
                andThen("incrementScore", "5").
                andThen("message", "You feel as through your score has increased as a result.");
        defn.addCondition(turn);

        cond = new VerbCondition("examine");
        cond.when("nounphrase", "smallroundthing").
                andWhen("locationId","2").
                andWhen("doesFlagExist", "userHasPickedUpABomb:false").
                then("lastAction.success", "You look at the strange small round thing. It appears to have 'tick' written on it.");
        defn.addCondition(cond);

        cond = new VerbCondition("take");
        cond.when("nounphrase", "smallroundthing").
                andWhen("locationId","2").
                andWhen("doesFlagExist", "userHasPickedUpABomb:false").
                then("setUserFlag","userHasPickedUpABomb:true").
                then("setUserCounter","userPickedUpBombCountDown:10").
                andThen("lastAction.success", "You bend over and pick up the Small Round Thing, you hear it go tock, and then tick, and then tick. And it keeps going 'tick' as though it is counting down to something. I wonder what it is. Oh, by the way. The word 'bomb' is written on the other side.");
        defn.addCondition(cond);

        cond = new VerbCondition("examine");
        cond.when("nounphrase", "smallroundthing").
                andWhen("isflagset", "userHasPickedUpABomb:true").
                then("lastAction.success", "You look at the strange small round thing. It appears to have 'tick' written on one side of it, and 'bomb' written on the other. It is making very loud 'tick' and 'tock' noises. Do you think it is an alarm clock? I wonder if it will go off soon?");
        defn.addCondition(cond);

        defn.addVerb("defuse");

        cond = new VerbCondition("defuse");
        cond.when("nounphrase", "smallroundthing").
                andWhen("isflagset", "userHasPickedUpABomb:true").
                then("setUserFlag","userHasPickedUpABomb:false").
                andThen("incrementScore","100").
                andThen("message", "You feel as though your score has increased.").
                andThen("lastAction.success", "You look at the strange small round thing, realise it is a bomb. And drawing on your years of experience with defusing bombs you throw it away in the hope that you will never see it again. You can't see it now, and you will probably never see it again.");
        defn.addCondition(cond);

        total_score += 10;

        /*
           Broken Transporter Puzzle is not a Puzzle
        */
        //VerbCondition:go; when: locationId=4, nounphrase=s; then: lastAction.success:"You step into...";
        cond = new VerbCondition("go");
        cond.when("direction", "s").
                andWhen("locationId", "4").
                then("lastAction.success","You step into the broken transporter. <ZAP> <PFIZZ> 'Ouch', that hurt you, and for no benefit. You are in the same room. The transporter is broken. I think there is another transporter around here somewhere");

        defn.addCondition(cond);







        /*
            The Loose Stone Puzzle

         */
        locationObj = new MudLocationObject("loosestone","A loose stone on the floor", "Someone has carved something on the stone, it looks like it says 'lickforten'");
        defn.addLocationObjectIn(locationObj, "6");

        defn.addVerb("lick");

        cond = new VerbCondition("lick");
        cond.when("nounphrase", "loosestone").
                andWhen("locationId", "6").
                andWhen("doesFlagExist", "userLickedStone:false").
                //andWhen("isFlagSet", "userLickedStone:false").
                        then("setUserFlag","userLickedStone:true").
                then("incrementScore","10").
                andThen("lastAction.success","You lick the stone - bleurgh, that tasted funny, I wonder if everyone licks it. Yuck. Cooties. But you did gain 10 points on your score.");
        defn.addCondition(cond);

        total_score += 10;

        // we will only ever lick it once
        cond = new VerbCondition("lick");
        cond.when("nounphrase", "loosestone").
                andWhen("locationId", "6").
                andWhen("isFlagSet", "userLickedStone:true").
                then("incrementScore","-5").
                andThen("lastAction.error","Nope, I licked that once, I'm never doing it again. You must have forgotten how bad it tasted. Yucky. And you lost 5 points from your score for trying to cheat the system.");
        defn.addCondition(cond);



        /*
            Hint Signs
         */
        locationObj = new MudLocationObject(defn.idGenerator().generateId("sign"),"A sign on the wall", "Shh, GO Now, I think I see a secret way in 'The East Room'. Shhh.");
        defn.addLocationObjectIn(locationObj, "2");

        locationObj = new MudLocationObject(defn.idGenerator().generateId("sign"),"A direction sign on the wall", "You can go East as far as you can see, even if you cannot see. Do you see?");
        defn.addLocationObjectIn(locationObj, "8");

        locationObj = new MudLocationObject(defn.idGenerator().generateId("sign"),"A helpful sign on the wall", "West might be best. Even if it gets dark and you want to rest, it might be a test, so go east, or west.");
        defn.addLocationObjectIn(locationObj, "6");


        /*
            The Forgettable Sign Puzzle
                - examine forgettablesign
                - then for 6 moves you can go N from room 12, but only for 6 moves
         */

        locationObj = new MudLocationObject("forgettablesign","A grotty forgettable sign on the wall", "I think there is a room north of 12, but you will forget this information in 6");
        defn.addLocationObjectIn(locationObj, "6");

        // implement information that we forget for the secret room north of 12, only accessible after examining the forgettablesign
        cond = new VerbCondition("examine");
        cond.when("nounphrase", "forgettablesign").
                andWhen("locationId", "6").
                then("setUserCounter","rememberTheForgettableSign:6").
                andThen("message","You remember something about a room north of 12.");
        defn.addCondition(cond);

        turn = new PriorityTurnCondition();
        turn.when("doesCounterExist", "rememberTheForgettableSign").
                andWhen("isCounterValue", "rememberTheForgettableSign:>:0").
                then("message", "You remember something about a secret room north of 12. And you think you might forget soon.").
                andThen("incrementCounter", "rememberTheForgettableSign:-1");
        defn.addCondition(turn);

        turn = new PriorityTurnCondition();
        turn.when("doesCounterExist", "rememberTheForgettableSign").
                andWhen("isCounterValue", "rememberTheForgettableSign:==:0").
                then("message", "You forgot to remember something about a secret room north of 12.").
                andThen("deleteUserCounter", "rememberTheForgettableSign");
        defn.addCondition(turn);

        cond = new VerbCondition("go");
        cond.when("direction", "n").
                andWhen("locationId", "12").
                andWhen("doesflagexist","seenSecretRoomNorthOf12:false").
                andWhen("doesCounterExist","rememberTheForgettableSign").
                andWhen("isCounterValue", "rememberTheForgettableSign:>:0").
                then("setUserFlag","seenSecretRoomNorthOf12:true").
                andThen("message","You scored some points.").
                andThen("incrementScore","20").
                andThen("teleportUserToLocation","17").
                andThen("lastAction.success","You found the secret room that everyone forgets about")
        ;
        defn.addCondition(cond);

        total_score += 20;

        // another north when we have seen it and no points available
        cond = new VerbCondition("go");
        cond.when("direction", "n").
                andWhen("locationId", "12").
                andWhen("doesflagexist","seenSecretRoomNorthOf12:true").
                andWhen("doesCounterExist","rememberTheForgettableSign").
                andWhen("isCounterValue", "rememberTheForgettableSign:>:0").
                then("setUserFlag","seenSecretRoomNorthOf12:true").
                andThen("message","You found the secret room again, but you can only score the points once.").
                andThen("teleportUserToLocation","17").
                andThen("lastAction.success","You found the secret room that everyone forgets about")
        ;
        defn.addCondition(cond);


        /*
            The Secret Grandfather Clock puzzle
         */

        // create a one way secret panel which has no corresponding exits

        defn.addGate("secretClockPanel",  GateStatus.CLOSED).
                gateIsHidden(true).    // this gate should be secret true
                playerCanOpen(false).  // player can not open it
                playerCanClose(true).  // but they can close it
                setShortDescription("Secret Panel").
                gateAutoCloses(true).  // it will auto close when player goes through
                gateAutoHides(true); // gate will auto hide when closed

        locationObj = new MudLocationObject("tallgrandfatherclock","A Tall Grandfather Clock", "The clock <span id='clockhands' class='locationobject'>hands</span> have fingerprints on them.");
        defn.addLocationObjectIn(locationObj, "14");

        locationObj = new MudLocationObject("clockhands","Hands on the grandfather clock", "The hands look like they have been moved and well used.");
        locationObj.setAsSecret(true); // we can not see the hands in the description but can still use them
        defn.addLocationObjectIn(locationObj, "14");

        //VerbCondition:use; when: locationId=14, nounphrase=clockhands, locationObject.isHere("clockhands"); then: openGate("secretClockPanel"), showGate("secretClockPanel"); success:"You move the hands...";
        //VerbCondition:use; when: locationId=14, nounphrase=tallgrandfatherclock, locationObject.isHere("clockhands");  error:"I can't...";
        cond = new VerbCondition("use");
        cond.when("nounphrase", "clockhands").
                andWhen("locationId", "14").
                andWhen("locationObject.isHere", "clockhands").
                then("openGate","secretClockPanel").
                andThen("showGate", "secretClockPanel").
                andThen("lastAction.success","You move the clock hands. You hear a squeal of wood and metal as something opens.");

        defn.addCondition(cond);

        cond = new VerbCondition("use");
        cond.when("nounphrase", "tallgrandfatherclock").
                andWhen("locationId", "14").
                andWhen("locationObject.isHere", "tallgrandfatherclock").
                andThen("lastAction.success","I can't use the clock, have you examined it carefully?");

        defn.addCondition(cond);

        /*
            Some unique collectables in secret room
         */
        MudCollectable cloth = new MudCollectable(defn.idGenerator().generateId("cloth"), "A Cloth of Shining");
        cloth.addsAbility("polish",200);
        defn.addCollectable(cloth, defn.gameLocations().get("15"));


        cloth = new MudCollectable(defn.idGenerator().generateId("cloth"), "A Big Cloth of Shining");
        cloth.addsAbility("polish",400);
        defn.addCollectable(cloth, defn.gameLocations().get("15"));

        /*
            Some unique collectables in dark room 7
         */
        MudCollectable darkTreasure = new MudCollectable(defn.idGenerator().generateId("treasure"), "A Treasure in the dark").canHoard(true,200);
        defn.addCollectable(darkTreasure, defn.gameLocations().get("7"));


        cloth = new MudCollectable(defn.idGenerator().generateId("cloth"), "A Bigger Cloth of Shining");
        cloth.addsAbility("polish",600);
        defn.addCollectable(cloth, defn.gameLocations().get("7"));

        // create a torch for the person who solves the clock puzzle first
        MudCollectable torch = new MudCollectable(defn.idGenerator().generateId("torch"), "A Torch of Illumination");
        torch.addsToggleAbility(VerbGameAbilities.ILLUMINATE_DARKEN,10, false);
        defn.addCollectable(torch, defn.gameLocations().get("16"));


        /***********************
         *      Zombie Maze Puzzles
         *
         */

        // gate is visible in location 5 but not usable
        create.locationObject("rustedshutdoor", "A rusted shut iron door to the south", "A heavy Iron door that has not been opened in a long time. It is rusted shut. And heavily barred. There is also a padlock. And the root is covered in chains. A sign on the door says 'do not enter under any circumstances, the graveyard is dangerous, the treasures here are not worth it, unless you are a Voodoo Master, in which case the Money God will rain treasure down upon you'. Looking through the bars on the door you can see a corridor that has not been used in a long time and beyond that, what looks like a rusty gate leading to a graveyard. You faintly hear something say 'brains'.", "5");

        // say brains in location 11 to go to the maze
        create.locationObject("zombiemazesign", "A sign on the wall covered in blood", "Say brains to enter the Zombie Graveyard maze where much treasure lies", "11");

        // when say brains I am transported to the zombie maze
        create.verbCondition("say").
                when(When.NOUNPHRASE_EQUALS,"brains").
                andWhen(When.LOCATION_ID_IS,"11").
                then(Then.TELEPORT_PLAYER_TO_LOCATION,"24").
                then(Then.SET_USER_COUNTER, "zombieattack:0").
                andThen(Then.SET_USER_FLAG, "zombieattackhappening:true").
                andThen(Then.LAST_ACTION_SUCCESS, "You say 'brains' and you hear a booming laugh. Then you hear a loud drumming, everything goes dark and you are standing in a cold dark graveyard.");

        // there is a rusty gate beyond which I can see a corridor but I can't open the gate
        create.locationObject("rustygate", "An immovable old rusty gate", "The Gate will not move, and is heavily padlocked, through the gate you can see dusty, dark cobwebbed corridor which has not been walked through in years. At the end of that you can see a faint light and it looks like corridor of the dungeon you just left. You can't go through the gate.", "24");

        // there is a rock at the entrance and if I Examine it I can see that I can say "brains" backwards to leave "sniarb"
        create.locationObject("dirtyrock", "A dirty rock on the ground", "The rock is actually an old broken slab, on the reverse is some writing and it reads: You can leave the maze if you say brains backwards.", "24");

        create.verbCondition("say").
                when(When.NOUNPHRASE_EQUALS,"brainsbackwards").
                andWhen(When.LOCATION_ID_IS,"24").
                andThen(Then.LAST_ACTION_SUCCESS, "You say 'brains backwards' and you hear a booming laugh. Fool. Say 'brains' in reverse, not literally 'brains backwards'.");

        create.verbCondition("say").
                when(When.NOUNPHRASE_EQUALS,"sniarb").
                andWhen(When.LOCATION_ID_IS,"24").
                then(Then.TELEPORT_PLAYER_TO_LOCATION,"2").
                then(Then.DELETE_USER_FLAG, "zombieattackhappening").
                then(Then.DELETE_USER_COUNTER,"zombieattack").
                andThen(Then.LAST_ACTION_SUCCESS, "You say 'brains' backwards and you hear drums grow louder, the mist grows thicker and you see the hands of zombies reaching towards you, a zombie face snaps its jaws shut horribly close to your nose, darkness envelops you. And you are no longer in the graveyard. You check your nose. It is as noselike as it was before you entered the graveyard. But where are you?");

        // things in the zombie maze
        create.locationObject("gravestones", "A set of gravestones", "The gravestones are old, very old, ancient, written on them is a twisted language that you can't read. Which is lucky, because you would be scared if you understood what it said", "27");
        create.locationObject("tombstones", "Some Tombstones", "The tombstones are old, very old, ancient, written on them is a twisted language that you can't read. And spray painted over the top of that is writing that says 'You Say Voodoo, I say Drum'", "28");
        create.locationObject("tomb", "A Tomb", "The tomb of the Money God. A sign above the tomb proclaims 'say moneygod' and treasure will fall, say it too often and once will be all.", "26");
        create.locationObject("pileofbones", "A Pile Of Bones", "The bones have teeth marks all over them, they look like human teeth marks. Hey, I don't think this is a nice place.", "29");
        create.locationObject("deadbody", "A Dead Body", "The dead body is dessicated but has splendid teeth. I think the axe is there for a reason.", "30");
        create.locationObject("axeindeadbody", "An Axe In The Dead Body", "The axe has a wooden handle, and carved into the handle are the words 'Ash woz here' and say 'groovy'.", "30");


        // call the moneygod

        create.priorityTurnCondition()
                .when(When.IS_USER_FLAG_SET, "prayedtomoneygod:true").then("incrementcounter", "moneygod:1");

        create.priorityTurnCondition()
                .when(When.IS_USER_FLAG_SET, "prayedtomoneygod:true").
                andWhen(When.USER_COUNTER_VALUE_IS, "moneygod:>:15").
                then(Then.DISPLAY_MESSAGE, "The Mighty Money God will hear your prayers again.").
                andThen(Then.DELETE_USER_FLAG,"prayedtomoneygod").
                andThen(Then.DELETE_USER_COUNTER,"moneygod");

        create.verbCondition("say")
                .when("nounphrase", "moneygod").  // when we use the teleporterbutton
                andWhen("locationid", "26").
                andWhen(When.DOES_FLAG_EXIST,"prayedtomoneygod:true").
                andThen(Then.INCREMENT_SCORE,"-10").
                andThen(Then.LAST_ACTION_SUCCESS, "You call upon the Money God, and anger the Money God, for it is too soon to call upon the Money God. Wait");

        create.verbCondition("say")
                .when(When.NOUNPHRASE_EQUALS, "moneygod").  // when we use the teleporterbutton
                andWhen(When.LOCATION_ID_IS, "26").
                andWhen(When.DOES_FLAG_EXIST,"prayedtomoneygod:false").
                andThen(Then.SET_USER_COUNTER,"moneygod:0").
                andThen(Then.SET_USER_FLAG, "prayedtomoneygod:true").
                andThen(Then.DISPLAY_MESSAGE, "Wow, you have mighty Vodou powers. You call upon the money god and treasure falls in the graveyard").
                andThen(Then.RANDOMLY_GENERATE_HOARDABLE_TREASURE, "15:24:26:27:28:29:30").
                andThen(Then.LAST_ACTION_SUCCESS, "You called upon the Loa");

        // navigate to the drum room

        create.verbCondition("say").
                when(When.NOUNPHRASE_EQUALS,"voodoo").
                andWhen(When.LOCATION_ID_IS,"28").
                then(Then.TELEPORT_PLAYER_TO_LOCATION,"25").
                then(Then.FORCE_LOOK,"true").
                andThen(Then.LAST_ACTION_SUCCESS, "You say 'voodoo', and you hear drums drumming loudly as though drummers are playing loudly on drums. You look around and see you are in a tomb, with a drum, and no drummers");

        create.verbCondition("say").
                when(When.NOUNPHRASE_EQUALS,"groovy").
                andWhen(When.LOCATION_ID_IS,"30").
                then(Then.TELEPORT_PLAYER_TO_LOCATION,"25").
                andThen(Then.LAST_ACTION_SUCCESS, "You say 'groovy', and you hear someone say 'shut those drums up, they are giving me a headache. You look around and see you are in a tomb, with a drum, and no-one with a headache");


        create.locationObject("voodoodrum", "A Voodoo Drum", "This is a ceremonial voodoo drum used by expert voodoo practitioners to control zombies. It can only be used by Houngans or Mambos of great power. There is a sticker on the side that says 'use voodoodrum. scare zombies'.",
                "8");

        // use drum for voodoo purposes  and cancel the zombie attack - score points for that

        create.verbCondition("use")
                .when("nounphrase", "voodoodrum").  // when we use the teleporterbutton
                andWhen("locationid", "25").
                andWhen("doesflagexist","useddrum:false").
                andThen("setuserflag", "useddrum:true").
                andThen("incrementscore", "100").
                andThen("message", "Wow, you have mighty Vodou powers. You can see zombies shuffling off into the darkness saying 'see ya, back soon'").
                andThen("setusercounter", "zombieattack:0").
                andThen("lastAction.success", "You called upon the Loa");

        create.verbCondition("use")
                .when("nounphrase", "voodoodrum").  // when we use the teleporterbutton
                andWhen("locationid", "25").
                andWhen("isflagset","useddrum:true").
                andWhen(When.USER_COUNTER_VALUE_IS,"zombieattack:>:18").
                andThen("incrementscore", "500").
                andThen("message", "You wait until a zombie has its teeth around your throat before you use your Voudun powers. Zombies shuffle off sighing 'OK Dokey eat ya later'").
                andThen("setusercounter", "zombieattack:0").
                andThen("lastAction.success", "You called upon the Loa Once More, but you cut it too close that time, you almost gave me a heart attack");

        // create some 'achievements' for using the drum at different times
        create.verbCondition("use")
                .when("nounphrase", "voodoodrum").  // when we use the teleporterbutton
                andWhen("locationid", "25").
                andWhen("isflagset","useddrum:true").
                andWhen(When.USER_COUNTER_VALUE_IS,"zombieattack:>:16").
                andThen("incrementscore", "50").
                andThen("message", "You use your Voudun powers. Zombies shuffle off sighing 'OK Dokey eat ya later'").
                andThen("setusercounter", "zombieattack:0").
                andThen("lastAction.success", "You called upon the Loa Once More, but you cut it a but close that time");



        create.verbCondition("use")
                .when("nounphrase", "voodoodrum").  // when we use the teleporterbutton
                andWhen("locationid", "25").
                andWhen("isflagset","useddrum:true").
                andThen("message", "You use your Voudun powers. Zombies shuffle off sighing 'OK Dokey eat ya later'").
                andThen("setusercounter", "zombieattack:0").
                andThen("lastAction.success", "You called upon the Loa Once More");

        // increment the zombie attack counter if it exists
        create.priorityTurnCondition()
                .when("isflagset", "zombieattackhappening:true").then("incrementcounter", "zombieattack:1");

        create.priorityTurnCondition()
                .when("isflagset", "zombieattackhappening:true").
                andWhen("iscountervalue", "zombieattack:<:10").
                then("message", "You heard moaning from the graveyard.");


        create.priorityTurnCondition()
                .when("isflagset", "zombieattackhappening:true").
                andWhen("iscountervalue", "zombieattack:==:11").
                then("message", "You heard an ominous silence. The graveyard is still. You feel something become aware of your presence.");


        create.priorityTurnCondition()
                .when("isflagset", "zombieattackhappening:true").
                andWhen("iscountervalue", "zombieattack:==:12").
                then("message", "You heard something moan. It sounded like it said 'brains'.");

        create.priorityTurnCondition()
                .when("isflagset", "zombieattackhappening:true").
                andWhen("iscountervalue", "zombieattack:==:14").
                then("message", "You heard something groan. It sounded like it said 'mmm, can smell tasty brains'.");

        create.priorityTurnCondition()
                .when("isflagset", "zombieattackhappening:true").
                andWhen("iscountervalue", "zombieattack:==:16").
                then("message", "You heard something wheeze. It sounded like it said 'oh, there, me can see tasty brain box'.");

        create.priorityTurnCondition()
                .when("isflagset", "zombieattackhappening:true").
                andWhen("iscountervalue", "zombieattack:==:18").
                then("message", "You heard something burble. It sounded like it said 'oh, there, me can see tasty brain box'.");

        create.priorityTurnCondition()
                .when("isflagset", "zombieattackhappening:true").
                andWhen("iscountervalue", "zombieattack:==:19").
                then("message", "You heard something shout loudly. It sounded like it said 'me open brain box now'.");

        // you get killed by a zombie when counter reaches? 10
        // 10 is the absolute minimum if you just go straight up and find the key and bring it back and do nothing else
        create.priorityTurnCondition()
                .when(When.IS_USER_FLAG_SET, "zombieattackhappening:true").
                andWhen(When.USER_COUNTER_VALUE_IS, "zombieattack:==:20").
                then(Then.TELEPORT_PLAYER_TO_LOCATION, "2").
                andThen(Then.SET_USER_FLAG,"zombieattackhappening:false").
                andThen(Then.INCREMENT_SCORE,"-200").
                then(Then.DISPLAY_MESSAGE, "You were attacked by a zombie and now you are dead. Fooled you. Actually you were teleported to a new location and lost some score. But it was really horrible what the zombie did to you before we rebuilt you from the ground up. Why didn't you scare the zombies away?");


        /***
         *
         * The Star Rooms
         *
         *
         */

        // can enter star rooms from room 7 by pressing a button
        create.locationObject("starshapedbutton", "A red star shaped button", "Red buttons are usually dangerous, I would not use that if I were you", "7");

        create.verbCondition("use").
                when(When.NOUNPHRASE_EQUALS,"starshapedbutton").
                andWhen(When.LOCATION_ID_IS,"7").
                then(Then.FORCE_LOOK, "true").
                then(Then.TELEPORT_PLAYER_TO_LOCATION,"31").
                andThen(Then.LAST_ACTION_SUCCESS, "You press the button and a star shaped portal to the north appears, you walk through it because you can't help it.");

        create.locationObject("starshapedsign", "A red star shaped sign", "The Sign Says: Red star shaped buttons are brilliant, I always use them and so should you", "9");


        // something for the first player there to collect
        create.collectable("shinystar","A valuable shiny star", "33").canHoard(true,200);
        create.locationObject("starsign", "A sign on the wall", "The sign says to use the sign and you might get lucky", "33");

        // create a set of rooms east of the North room 2 with a treasure generator and a hoard
        // generate treasure by use starsign
        // if we have used sign enough then money
        create.verbCondition("use").
                when(When.NOUNPHRASE_EQUALS,"starsign").
                andWhen(When.LOCATION_ID_IS,"33").
                andWhen(When.DOES_FLAG_EXIST,"starsigncounterflag:true").
                andWhen(When.USER_COUNTER_VALUE_IS,"starsigncounter:==:3").
                then(Then.SET_USER_COUNTER,"starsigncounter:-3").
                then(Then.RANDOMLY_GENERATE_HOARDABLE_TREASURE, "3:33:34:35:31").
                then(Then.FORCE_LOOK, "true").
                andThen(Then.LAST_ACTION_SUCCESS, "You hear a kachink noise in the distance");

        // first time using it
        create.verbCondition("use").
                when(When.NOUNPHRASE_EQUALS,"starsign").
                andWhen(When.LOCATION_ID_IS,"33").
                andWhen(When.DOES_FLAG_EXIST,"starsigncounter:false").
                then(Then.SET_USER_FLAG,"starsigncounter:true").
                then(Then.SET_USER_FLAG,"starsigncounterflag:true").
                then(Then.SET_USER_COUNTER,"starsigncounter:0").
                then(Then.RANDOMLY_GENERATE_HOARDABLE_TREASURE, "5:33:34:35:31").
                then(Then.FORCE_LOOK, "true").
                andThen(Then.LAST_ACTION_SUCCESS, "You hear a loud kachink noise in the distance");

        create.verbCondition("use").
                when(When.NOUNPHRASE_EQUALS,"starsign").
                andWhen(When.LOCATION_ID_IS,"33").
                andWhen(When.DOES_FLAG_EXIST,"starsigncounterflag:true").
                andWhen(When.USER_COUNTER_VALUE_IS,"starsigncounter:<:3").
                then(Then.INCREMENT_USER_COUNTER,"starsigncounter:1").
                then(Then.FORCE_LOOK, "true").
                andThen(Then.LAST_ACTION_SUCCESS, "You hear a dull thud");


        /***
         *
         * REST Room Puzzles
         *
         */

        create.locationObject("restroomsexplained", "Sign That Explains What You Have To Do in the REST Rooms", "In the REST Rooms you will needto use the api/player/yourname. You can use the API at the same time as the GUI either with Basic Auth, session cookie, or the authentication header from /info or when you registered or logged via the API", "36");

        create.locationObject("postexplain", "Sign That Explains What You Have To Do", "POST to api/player/user {\"verb\" : \"examine\", \"nounphrase\" : \"postsign\"} for points", "37");
        create.locationObject("postheaderexplain", "Sign That Explains What You Have To Do For More Points", "POST to api/player/user \"verb\" : \"examine\", \"nounphrase\" : \"postheader\"} with custom header X-REST-MUD-POST-EXAMINE for points", "37");

        create.verbCondition("examine").
                andWhen(When.LOCATION_ID_IS,"37").
                when(When.NOUNPHRASE_EQUALS,"postsign").
                when(When.HTTP_VERB_IS, "post").
                andWhen(When.DOES_FLAG_EXIST,"postsignexamined:false").
                then(Then.INCREMENT_SCORE,"100").
                andThen(Then.SET_USER_FLAG,"postsignexamined:true").
                andThen(Then.LAST_ACTION_SUCCESS, "That was easy, your score increased");

        create.verbCondition("examine").
                andWhen(When.LOCATION_ID_IS,"37").
                when(When.NOUNPHRASE_EQUALS,"postheader").
                when(When.HTTP_VERB_IS, "post").
                when(When.HTTP_HEADER_EXISTS, "X-REST-MUD-POST-EXAMINE").
                andWhen(When.DOES_FLAG_EXIST,"postsignexaminedheader:false").
                then(Then.INCREMENT_SCORE,"200").
                andThen(Then.SET_USER_FLAG,"postsignexaminedheader:true").
                andThen(Then.LAST_ACTION_SUCCESS, "That was easy, your score increased");

        create.locationObject("getheaderexplain", "Sign That Explains What You Have To Do For Points", "GET api/player/user/examine/getheader with custom header X-REST-MUD-GET-EXAMINE for points", "38");

        create.verbCondition("examine").
                andWhen(When.LOCATION_ID_IS,"38").
                when(When.NOUNPHRASE_EQUALS,"getheader").
                when(When.HTTP_VERB_IS, "get").
                when(When.HTTP_HEADER_EXISTS, "X-REST-MUD-GET-EXAMINE").
                andWhen(When.DOES_FLAG_EXIST,"getsignexaminedheader:false").
                then(Then.INCREMENT_SCORE,"100").
                andThen(Then.SET_USER_FLAG,"getsignexaminedheader:true").
                andThen(Then.LAST_ACTION_SUCCESS, "That was easy, your score increased");

        create.locationObject("fightsmalltrollsign", "How to fight the small troll", "DELETE api/player/user/fight/smalltroll for points", "39");
        create.locationObject("fightlargetrollsign", "How to fight the large troll", "DELETE api/player/user/fight/largetroll with custom header X-SUPER-FIGHTING-SKILLS-VERY-HARD for points", "40");

        create.locationObject("smalltroll", "A Small Troll", "The Troll glares at you as though you have seen something you should not have.", "39").setAsSecret(true);
        create.locationObject("largetroll", "A Large Troll", "The Large Troll glares at you. You should not be looking at him.", "40").setAsSecret(true);


        // TODO: game should be able to process multiple conditions if they don't end in a LastAction
        // I have to put trolls in different rooms as a result
        create.verbCondition("look").
                andWhen(When.LOCATION_ID_IS,"39").
                andWhen(When.DOES_FLAG_EXIST,"smalltrolldefeated:false").
                andThen(Then.SHOW_LOCATION_OBJECT_IN_LOOK, "smalltroll:A Small Ugly Troll");

        create.verbCondition("look").
                andWhen(When.LOCATION_ID_IS,"40").
                andWhen(When.DOES_FLAG_EXIST,"largetrolldefeated:false").
                andThen(Then.SHOW_LOCATION_OBJECT_IN_LOOK, "largetroll:A Large Very Ugly Troll");


        defn.addVerb("fight");

        create.verbCondition("fight").
                andWhen(When.HTTP_VERB_IS,"delete").
                andWhen(When.LOCATION_ID_IS,"39").
                andWhen(When.NOUNPHRASE_EQUALS,"smalltroll").
                andWhen(When.DOES_FLAG_EXIST,"smalltrolldefeated:false").
                andThen(Then.SET_USER_FLAG, "smalltrolldefeated:true").
                andThen(Then.INCREMENT_SCORE,"100").
                andThen(Then.LAST_ACTION_SUCCESS,"You poke the small troll in the nose and it squeals and runs away. You win the fight.");

        create.verbCondition("fight").
                andWhen(When.HTTP_VERB_IS,"delete").
                andWhen(When.HTTP_HEADER_EXISTS,"X-SUPER-FIGHTING-SKILLS-VERY-HARD").
                andWhen(When.LOCATION_ID_IS,"40").
                andWhen(When.NOUNPHRASE_EQUALS,"largetroll").
                andWhen(When.DOES_FLAG_EXIST,"largetrolldefeated:false").
                andThen(Then.SET_USER_FLAG, "largetrolldefeated:true").
                andThen(Then.INCREMENT_SCORE,"200").
                andThen(Then.LAST_ACTION_SUCCESS,"You punch the large troll in the stomache and it looks at you, then slowly fades away. You win the fight. You Super Fighter All Win Fight");




        /*
            Some unique collectables in secret room 17
         */
        MudCollectable treasure = new MudCollectable(defn.idGenerator().generateId("treasure"), "A Treasure in the dark").canHoard(true, 200);
        defn.addCollectable(treasure, defn.gameLocations().get("17"));


        /*
            Some Dispensers
         */

        // create a torch dispenser
        locationObj = new MudLocationObject("torchdispenser","A Torch Dispenser", "Use the dispenser to get a torch.");
        locationObj.setIsADispenser(true);
        locationObj.setDispenserTemplate(MudCollectableTemplate.getDispenserTemplate("torch", "A Dispensed Torch", VerbGameAbilities.ILLUMINATE_DARKEN, 4, 20));
        defn.addLocationObjectIn(locationObj, "13");



        // create a cheap jewel dispenser in room 23
        locationObj = new MudLocationObject("ajeweldispenser","A Jewel Dispenser", "Use the jewel dispenser to get a jewel.");
        locationObj.setIsADispenser(true);
        locationObj.setDispenserTemplate(MudCollectableTemplate.getDispenserTemplate("jewel", "A Nice Jewel").setHoardableAttributes(true,1,15));
        defn.addLocationObjectIn(locationObj, "23");


        // create a valuable jewel dispenser in room 22
        locationObj = new MudLocationObject("jeweldispenser","A Jewel Dispenser", "Use the jewel dispenser to get a jewel.");
        locationObj.setIsADispenser(true);
        locationObj.setDispenserTemplate(MudCollectableTemplate.getDispenserTemplate("jewel", "A Groovy Jewel").setHoardableAttributes(true,100,150));
        defn.addLocationObjectIn(locationObj, "22");

        // Create a location obj which is a dispenser

        locationObj = new MudLocationObject("clothdispenser","A Cloth Dispenser", "Use the dispenser to get a cloth.");
        locationObj.setIsADispenser(true);
        locationObj.setDispenserTemplate(MudCollectableTemplate.getDispenserTemplate("cloth", "A Dispensed Cloth", "polish", 100, 200));
        defn.addLocationObjectIn(locationObj, "12");

        locationObj = new MudLocationObject("megaclothdispenser","A Mega Cloth Dispenser", "Use the dispenser to get a cloth.");
        locationObj.setIsADispenser(true);
        locationObj.setDispenserTemplate(MudCollectableTemplate.getDispenserTemplate("cloth", "A Dispensed Cloth", "polish", 300, 400));
        defn.addLocationObjectIn(locationObj, "9");

        /*
            Some Default treasures
         */

        // a default collectable
        MudCollectable aCollectable = new MudCollectable("shiny_gold_ring","A shiny gold ring");
        aCollectable.canHoard(true,200);
        defn.addCollectable(aCollectable, startLocation);
        total_score+=200;


        // need to have the following random treasure generation as scriptable
//        RandomTreasureGenerator moneyGod = new RandomTreasureGenerator(game);
//        moneyGod.createHoards(4);
//        // Money God create random stuff in locations
//        moneyGod.createHoardableTreasures(15);
//        moneyGod.createNonHoardableTreasures(5);
//        moneyGod.createHoardableJunk(15);
//        moneyGod.createNonHoardableJunk(5);

        defn.addStartupRule(Then.RANDOMLY_GENERATE_HOARDS, "4");
        defn.addStartupRule(Then.RANDOMLY_GENERATE_HOARDABLE_TREASURE, "15");
        defn.addStartupRule(Then.RANDOMLY_GENERATE_NONHOARDABLE_TREASURES, "5");
        defn.addStartupRule(Then.RANDOMLY_GENERATE_HOARDABLE_JUNK, "15");
        defn.addStartupRule(Then.RANDOMLY_GENERATE_NON_HOARDABLE_JUNK, "5");

        // TODO:
        // add the new Then Clauses to the parser
        // create a startup rule processor when the game starts to initialise random generation

        // this is the total possible score a user could achieve if they played the game on their own
        defn.setTotalScore(total_score);

    }
}
