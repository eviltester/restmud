package uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions;

import uk.co.compendiumdev.restmud.engine.game.gamedefinition.GameDefinitionPopulator;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;


public class TechWebTest101GameDefinitionGenerator implements GameDefinitionPopulator {
    public void define(MudGameDefinition defn) {



        defn.setGameName("Tech Web Test 101 Test Game");
        defn.setWelcomeMessage("Welcome to the Technical Web Testing Game");

        /**
         * Add custom verbs that the game needs
         */
        defn.addVerb("lift");


        // TODO: make it easier to create the game via code
        // would like objects for teh different When and Then commands to aid code completion instead of having to remember the strings

        // TODO: for single player have a 'dead' and 'restart game' set of options

        /**
         * Track the total score for a game and assign it manually with game.setTotalScore(total_score);
         */
        int total_score = 0;
        defn.setTotalScore(total_score);

        MudGameEntityCreator create = defn.creator();

        // by default the system junk room is at "0"
        // the junk room does not need any exits out,
        // but I added one just in case a user ends up here
        create.location("0","The Junk Room", "This is the room of Junk. How did you get here?", "S:1");

        // a game needs a start location where a user will start
        create.location("1","At the House Front Door", "You are in front of a house. It is a small house. A scary house. The door to the house (in the north) is locked with a <span location_object_id='very_strong_padlock'>very strong padlock</span>.",
                // locations are comma separated list of attributes
                // attributes are the direction:whereto:optionalflag
                "N:local,E:3,S:2,W:5");

        defn.setStartLocationId("1");


        create.locationObject("very_strong_padlock", "A Very Strong Lock Indeed", "The lock is very strong. Much stronger than all the practice lock picks that I bought from the hardware store. Those locks I could pick in a few minutes. This one. I doubt it can be picked at all.",
                "1").setAsSecret(true).
                because("examine padlock - but padlock is 'hidden' so you have to inspect element to find its id " +
                        "creating a 'real' but hidden object means I don't have to create any rules for examining");


        create.verbCondition("go")
                .when("direction", "n").  // when we go north
                andWhen("locationid", "1").
                andThen("lastAction.error", "Did you notice that the door was locked with a strong padlock? That means you can't do that way").
                because("North, can't go in, it is padlocked, we need to use the key");

        create.verbCondition("use")
                .when("nounphrase", "frontdoorkey").  // when we go north
                andWhen("locationid", "1").
                andWhen("user.iscarrying","frontdoorkey").
                andThen("message", "You hear zombies getting closer. One runs towards you. You fumble with the key.<br/> You drop the key.<br/>You hear a loud crescendo of music from somewhere, it is very dramatic.<br/>The zombie falls over, giving you time to pick up the key and calmly unlock the door.<br/>You walk inside and lock the door behind you. You accidentally drop the key in the house, where it promptly falls down a crack in the floor never to be seen again. Ever.<br/>I think that if Matt ever comes back, then you just killed him. He won't be able to get in the house.<br/> Oh dear, never mind, on with the adventure.").
                andThen("teleportusertolocation","inhouse").
                andThen("teleportcollectabletolocation","frontdoorkey:1"). // allow other people in the game to find the key by moving it to the front of the house
                andThen("setuserflag","zombieattackhappening:false").
                andThen("incrementscore", "20").
                andThen("lastAction.success", "You open the front door").
                because("Use key to 'zap' into the house and lock the door behind you");

        total_score += 20;


        create.locationObject("welcomemat", "A Welcome Mat", "The welcome mat has the words 'Welcome Matt' on it. Presumeably this heavenly hovel from hell was once owned by a man named Matt. Sometimes people put spare keys under the mats. Do you think you could lift the mat and see?"
                ,"1").
                because("location object a 'welcome mat' " +
                        "examine the matt and you find a wire - I wonder if you can use wire to pick the lock? - no you can't");

        create.collectable("lockpickwire", "A thin wire that lock pickers can USE to pick locks.","0").
                because("initially the lock pick isn't here, it is in the junk room");


        create.verbCondition("lift")
                .when(When.NOUNPHRASE_EQUALS, "welcomemat").  // when we use the teleporterbutton
                andWhen(When.LOCATION_OBJECT_IS_HERE, "welcomemat").
                andWhen(When.DOES_FLAG_EXIST,"liftedmat:false").
                then(Then.TELEPORT_COLLECTABLE_TO_LOCATION, "lockpickwire:1").
                andThen(Then.SET_USER_FLAG, "liftedmat:true").
                andThen(Then.INCREMENT_SCORE, "10").
                andThen(Then.DISPLAY_MESSAGE, "It occurs to you to lift the mat and look under it, you see a small wire. I wonder if you can use wire to pick the lock?").
                andThen(Then.LAST_ACTION_SUCCESS, "You lifted Matt's mat");

        total_score += 10;

        create.verbCondition("lift")
                .when("nounphrase", "welcomemat").  // when we use the teleporterbutton
                andWhen(When.LOCATION_OBJECT_IS_HERE, "welcomemat").
                andWhen("doesflagexist","liftedmat:true").
                andThen("incrementscore", "-5").
                andThen("message", "We already did that. OK. You lift the mat, wave it arround but nothing falls out. You roll it into a tube and peer through it, all you can see is a dark graveyard surrounding you and that reminds you that there are scary things there that might get you. You put the mat tube to your ear and that let's you hear even more loudly the THINGS THAT ARE OUT THERE.").
                andThen("lastAction.success", "You lifted Matt's mat AGAIN");

        create.verbCondition("use")
                .when("nounphrase", "lockpickwire").  // when we use the teleporterbutton
                andWhen("locationid", "1").
                andWhen("user.iscarrying", "lockpickwire").
                andThen("incrementscore", "10").
                andThen("message", "Believing that since you are in an adventure game you have all your normal real world skills. And since in the real world you are an expert lock picker using wires that you found on the ground under old bits of cloth with words written on them. You stick the wire in the lock expecting it to open. But it doesn't. Because you aren't in the real world now. You are in Adventure Game Land. And there are zombies. And you can't. So the lock doesn't open. But you know now that you can't pick locks, so that's something.").
                andThen("lastAction.success", "You try to use the wire to pick the lock");

        total_score += 10;

        create.location("2","At the House Front Garden", "You are in the front garden of a house. It is a small house. A scary house. The garden is small. The garden is scary. For some reason the house has been built inside a Graveyard. A small graveyard. A very scary graveyard. You can hear things moving in the graveyard. Big things. Scary things.","N:1,S:6,E:6,W:6");

        // mailbox and examine letter
        create.locationObject("mailbox", "Matt's Mailbox", "A wooden mailbox, although who would try to deliver mail here?","2");

        create.locationObject("letter", "Matt's Letter", "Dear Adventurer<br/>I accidentally build my house in a graveyard and zombies try to eat me. Thanks for visiting. I hope you have a nice time here. I've locked the door so you'll probably want to find a key to try and get in.<br/>Don't take too long, otherwise the zombies might eat you. Remember to inspect things and make sure you observe the writing in the game as there are clues there.<br/>All the best,<br/>Your friend Matt (even though we have never met).<br/><br/>PS. The graveyard is Easily Wandered through but you could get lost, So Seriously, Simply make a map",
                "2").setAsSecret(true);



        create.verbCondition("open")
                .when("nounphrase", "mailbox").
                andWhen("locationid", "2").
                andWhen("isflagset","openedmailbox:true").
                andThen("lastAction.error", "You already opened the mailbox. I'm not doing that again.");

        create.verbCondition("open")
                .when("nounphrase", "mailbox").
                andWhen("locationid", "2").
                andWhen("doesflagexist","openedmailbox:false").
                andThen("incrementscore", "10").
                andThen("object.setSecret", "letter:false").
                andThen("setuserflag","openedmailbox:true").
                andThen("message", "You open the mailbox and a letter falls to the ground.").
                andThen("lastAction.success", "You open the mailbox").
                because("bug? 10:45 I think we can probably read the letter without opening the mailbox - leave bug in game do not fix");

        create.verbCondition("use").
                when("nounphrase", "mailbox").
                andWhen("locationid", "2").
                andWhen("doesflagexist","openedmailbox:false").
                andThen("incrementscore", "5").
                andThen("object.setSecret", "letter:false").
                andThen("setuserflag","openedmailbox:true").
                andThen("message", "You bash the mailbox like a primitive monkey and it opens and a letter falls to the ground. You could have probably opened it and received more points. You don't always have to force things.").
                andThen("lastAction.success", "You open the mailbox").
                because("we can use the mailbox for less points, but if we open it then that is better");

        total_score += 10;



        create.location("3","At the East side of the House", "You are on the East side of the house. It is a small house. A scary house. etc. There is a window to the West but it is barred shut, there is no way that you or anyone, or any thing (say a Big Scary Thing that makes noise in the graveyard) could break through the barred window. You'd be safe in the house. You are not safe out here.","N:4,S:2,E:6,W:local");

        create.verbCondition("go")
                .when("direction", "w").
                andWhen("locationid", "3").
                andThen("lastAction.error", "Yeah, I'm not sure you are reading the location descriptions. The clue was 'there is no way that you or anyone... could break through the barred window. You failed. You could not get through the barred window to the west.");



        create.location("4","At the North side of the House", "You are at back of the house. It is a small house. etc. graveyard etc. scary things moving etc. The back door is blocked. It looks as though someone bricked up the door, then covered it in wooden planks and then stuck bars over them. There is no way that you are going to go South into the house through that door. Actually it isn't really a door anymore. To call it a door is to call a brick wall a door, and that would be so wildly inaccurate that I wouldn't do that. Someone even built <span location_object_id='a_small_wall'>a small wall</span> at the bottom of the wall, well actually it is more like a pile of stones but I call it a small wall, what would you call it?","N:6,S:local,E:3,W:5");

        create.verbCondition("go")
                .when("direction", "s").
                andWhen("locationid", "4").
                andThen("lastAction.error", "Not you. Not a zombie. Not a tank. Not even a large green superhero who says 'smash' could go that way.");

        create.collectable("frontdoorkey", "A large heavy key perfect for unlocking large heavy locks.","0").
                because("initially the key isn't here, it is in the junk room");


        create.verbCondition("examine")
                .when("nounphrase", "a_small_wall").
                andWhen("locationid", "4").
                andWhen("doesflagexist","examinedsmallwall:true").
                andThen("incrementscore", "-5").
                andThen("message", "What fun. Since we all know that there are zombies walking about that want to eat your brains. Let's play with stones instead of finding a way into this fortress called 'the house'.").
                andThen("lastAction.success", "You play with some stones");

        create.verbCondition("examine")
                .when("nounphrase", "a_small_wall").
                andWhen("locationid", "4").
                andWhen("doesflagexist","examinedsmallwall:false").
                then("teleportcollectabletolocation", "frontdoorkey:4").
                andThen("setuserflag", "examinedsmallwall:true").
                andThen("incrementscore", "10").
                andThen("message", "Pretty smart. You examined the small pile of stones that constitute a small wall. And it looks like you found a key.").
                andThen("lastAction.success", "You examine the small pile of stones");

        total_score += 10;



        create.location("5","At the West side of the House", "You are at the other side of the house. Unless of course this is the first side you have seen, in which case you are at the side of the house that is new to you. It is a small house in a graveyard with scary things which are scary. Are you scared? You should be. Someone has scratched something into <span location_object_id='scratchedwood'>the wood</span> of the house, under the wood it looks like there is reinforced metal that once belonged on a tank.","N:4,S:1,E:local,W:6");

        create.verbCondition("go")
                .when("direction", "e").
                andWhen("locationid", "5").
                andThen("lastAction.error", "Unless you are a very big tank. And I mean super big. Like as big as a 'MSC Oscar'. You can't go that way").
                because("we can't walk through the wall");


        create.locationObject("scratchedwood", "A Message Scratched into the wood", "Someone scratched a message into the wood, it says. 'Zombies love brains and hate drums, with love from Matt'. I guess the someone was Matt.",
                "5").setAsSecret(true);

        create.location("6","In the graveyard", "You are in the graveyard. A small graveyard. A very scary graveyard. You can hear things moving around in the graveyard. Big things. Scary things. The graveyard is very dark and everywhere looks the same. I'm concerned that if you don't get out of here and into the safety of the house soon you will very likely be eaten by one of the big scary things that you can hear in the graveyard.","N:6,E:7,W:6,S:4");

        create.location("7","Somewhere else In the graveyard", "You are in the graveyard. A small graveyard. With twisty routes between the graves.  You can hear things moving around in the graveyard. Are you lost?","N:7,E:7,W:8,S:6");

        create.location("8","Somewhere special In the graveyard", "You are in the graveyard. A small graveyard. This is the place of Voodoo where zombies are summoned and dispelled.","N:8,E:8,W:8,S:7");

        // location object a voodoo drum
        create.locationObject("voodoodrum", "A Voodoo Drum", "This is a ceremonial voodoo drum used by expert voodoo practitioners to control zombies. It can only be used by Houngans or Mambos of great power. There is a sticker on the side that says 'use voodoodrum. scare zombies'.",
                "8");


        create.verbCondition("use")
                .when("nounphrase", "voodoodrum").
                andWhen("locationid", "8").
                andWhen("doesflagexist","useddrum:false").
                andThen("setuserflag", "useddrum:true").
                andThen("incrementscore", "10").
                andThen("message", "Wow, you have mighty Vodou powers. You can see zombies shuffling off into the darkness saying 'see ya, back soon'").
                andThen("setusercounter", "zombieattack:0").
                andThen("lastAction.success", "You call upon the Loa").
                because("use drum for voodoo purposes  and cancel the zombie attack - score points for that");

        total_score += 10;

        create.verbCondition("use")
                .when("nounphrase", "voodoodrum").
                andWhen("locationid", "8").
                andWhen("isflagset","useddrum:true").
                andThen("message", "You use your Voudun powers. Zombies shuffle off sighing 'OK Dokey eat ya later'").
                andThen("setusercounter", "zombieattack:0").
                andThen("lastAction.success", "You call upon the Loa Once More");



        create.priorityTurnCondition()
                .when("doesflagexist", "zombieattackhappening:false").
                then("setusercounter", "zombieattack:0").
                andThen("setuserflag", "zombieattackhappening:true").
                because("Zombie counter and conditions");


        create.priorityTurnCondition()
                .when("isflagset", "zombieattackhappening:true").then("incrementcounter", "zombieattack:1").
                because("increment the zombie attack counter if it exists");

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

        create.priorityTurnCondition()
                .when("isflagset", "zombieattackhappening:true").
                andWhen("iscountervalue", "zombieattack:==:20").
                then("message", "You were attacked by a zombie and now you are dead.").
                andThen("teleportusertolocation", "living_death")
                .andThen("setuserflag","zombieattackhappening:false").
                because("you get killed by a zombie when counter reaches? 10 " +
                        "10 is the absolute minimum if you just go straight up and find the key and bring it back and do nothing else");



        create.location("living_death","You are dead", "You are dead. Living dead. The game doesn't end. But there is nothing you can do. You should probably just start the game again.","").
                because("We don't have death, so simulate it with a living death room with no exits");



        // TODO: The rest of the adventure in the house
        create.location("inhouse","You are in the house", "You are safely inside the house. It is quiet in here. And safe. <br/> Actually, if you made it here, that's the end of the game. Well done.","");

        defn.setTotalScore(total_score);
    }
}
