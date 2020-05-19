package uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions;


import uk.co.compendiumdev.restmud.engine.game.gamedefinition.GameDefinitionPopulator;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
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

public class ExampleDocumentedGameDefinitionGenerator implements GameDefinitionPopulator {
    public void define(MudGameDefinition defn) {

        /**
         * A VerbCondition is something that can trigger a lastAction.success or lastAction.Error
         * and is triggered when someone enters a verb/noun phrase
         * they are used to override or create new verb actions
         */
        VerbCondition cond;

        /**
         * A PriorityTurnCondition is something that can run every turn based on a condition
         * any embedded lastAction.success or lastAction.Error is ignored
         */
        PriorityTurnCondition turn;

        /**
         * A MudLocation is a 'room' or location in a game
         */
        MudLocation objectLocation;

        /**
         * Collectables can be taken and hoarded
         */
        MudCollectable collectable;


        /**
         * A MudLocation is a 'room' or location in a game
         */
        MudLocation location;

        /**
         * Track the total score for a game and assign it manually with game.setTotalScore(total_score);
         */
        int total_score = 0;
        defn.setTotalScore(total_score);



        defn.setGameName("Example Documented Test Game");
        defn.setWelcomeMessage("Welcome to the Example Test Game");


        MudGameEntityCreator create = defn.creator();

        // by default the system junk room is at "0"
        // the junk room does not need any exits out,
        // but I added one just in case a user ends up here
        create.location("0","The Junk Room", "This is the room of Junk", "S:1");

        // a game needs a start location where a user will start
        create.location("1","The Start Test Room", "This is the room where a user starts",
                // locations are comma separated list of attributes
                // attributes are the direction:whereto:optionalflag
                "N:2,E:3,S:4:northgate,W:5");

        // note the northgate reference on the southexit, this says the exit is controlled by the northgate (which is defined later)


        defn.setStartLocationId("1");

        /**
         * location flags are local, secret
         */

        /**
         * secret exits are not shown in the list of visible exits
         * so this room will not show any exits in the visible exit list,
         * but the user can go/s and get to room 1
         */
        create.location("2","The North Room", "This is the room north of the start room", "S:1:secret");

        /**
         * local exits
         * The local for W:1:local makes no difference, really it is just documentation
         * The local for S:local means that we need to create a VerbCondition to handle the movement but S will always be listed as an exit
         * and if no verb condition matches then the game will trigger lastAction.Error "I can't go that way at the moment"
         */
        create.location("3","The East Room", "This is the room east of the start room", "W:1:local, S:local");


        /**
         * GATES
         *
         * Gates control if a exit can be blocked or not
         * Gates can be created with minimal information e.g. name and open or close status*
         * Gates can have extra properties as to auto closing etc.
         * Gates are assigned to exits on locations
         * Gates have to be added to both exits for a two way gate e.g. in location 4 add a gate on an exit, and in location 6 add the gate to an exit
         *
         */
        /**
         * gated exits
         * The local for W:1:local makes no difference, really it is just documentation
         * The local for S:local means that we need to create a VerbCondition to handle the movement but S will always be listed as an exit
         * and if no verb condition matches then the game will trigger lastAction.Error "I can't go that way at the moment"
         */
        create.location("4","The South Room", "This is the room south of the start room", "N:1:northgate,E:6:onewayeast");

        // a simple gate exit that says there is a gate from north to south, which starts as closed and is accessible
        // from both locations 4 and 1, it will be shown in both locations
        // because it has been added to exits on both locations
        defn.addGate("northgate", GateStatus.CLOSED);


        create.location("6","The Room East of the South Room", "This is a room south of the start room.", "W:4");
        // gates do not need to exist before being used in an exit definition
        // a one way gate is only shown in the from location i.e. 4 and only impacts the from location
        // in location 6 we will not even know that a gate existed
        // a one way gate is achieved by only adding the gate to a single exit
        // e.g. see the location definition for location 4 and 6
        // only location 4 mentions the gate
        defn.addGate("onewayeast", GateStatus.CLOSED);



        /* even though S:1 is always listed, it is handled by a gate */
        /* priority is local handling with verb condition, gate handling, location handling */
        create.location("5","The West Room", "This is the room West of the start room", "E:1,S:1:theTeleporter");

        /**
         * We can name gates so that they are accessible from scripting
         * This gate is hidden so that it doesn't show in the gates description, the way south is controlled by a gate so needs a destination e.g. S:1
         */
        defn.addGate("theTeleporter", GateStatus.CLOSED).
                gateIsHidden(true).     // so it never appears in the list of gates displayed
                gateAutoCloses(true).   // so you have to press the button to activate it each time you go through
                setShortDescription("the teleporter"). // by default a gate is called door, so this makes it 'the teleporter'
                setClosedDescription("not active").  // instead of saying "closed" by default say "not active"
                playerCanOpen(false). // by default a gate can be opened by a player e.g. open s (to open the gate to the south)
                playerCanClose(false). // we don't want the player to be able to open or close this gate - leaving this out would create a bug in the game
                // by setting a through description we don't have to add custom rules for messages when we go through e.g. The Gate slams shut behind you as you go through
                setThroughDescription(". The teleporter makes an awful screeching noise and you feel as if you are being torn limb from limb. Luckily you arrive safely and clamber out");

        // add a location object to location "5" which we will use to control the gate
        create.locationObject("teleporterbutton", "A Teleporter Button", "Use the button to switch on the teleporter","5");

        // create a verb condition to switch on the teleporter (make the gate open) when the player uses the button
        create.verbCondition("use")
                .when("nounphrase", "teleporterbutton").  // when we use the teleporterbutton
                andWhen(When.LOCATION_OBJECT_IS_HERE, "teleporterbutton").
                then(Then.OPEN_GATE, "theTeleporter").
                andThen("message", "You push the button and the teleporter flickers to life scarily. Are you sure you want to go through it?").
                andThen("lastAction.success", "The teleporter is active now");     // then open the lever6gate


        /**
         * We can use verb conditions to change a user's location and not just use gates
         */

        create.locationObject("corridorbutton", "A Teleporter Button To go to the corridor of test rooms", "Use the button to go to the corridor of test rooms","1");

        // locationIds do not need to be numbers
        create.location("corridor","The North End of the Corridor of Test Rooms", "This is the North end of the corridor, a test room is not off to the east because location 7 does not exist", "E:7,S:8");

        create.verbCondition("use")
                .when("nounphrase", "corridorbutton").  // when we use the button
                andWhen(When.LOCATION_OBJECT_IS_HERE, "corridorbutton").
                then("teleportUserToLocation", "corridor").
                andThen("message", "You push the button and are transported to the corridor of test rooms.").
                andThen("forceLook","").
                andThen("lastAction.success", "You are now in the corridor of test rooms.");

        create.locationObject("startroombutton", "A Teleporter Button To go to the start room", "Use the button to go to the start room","corridor");

        create.verbCondition("use")
                .when("nounphrase", "startroombutton").  // when we use the button
                andWhen(When.LOCATION_OBJECT_IS_HERE, "startroombutton").
                then("teleportUserToLocation", "1").
                andThen("message", "You push the button and are transported to the start room.").
                andThen("lastAction.success", "You are now in the start room.");

        // Room 7 does not exist, that is the test


        create.location("8","South of The Northern most End of the Corridor of Test Rooms", "This is still at the the North end of the corridor, but not the most northern end, a test room is off to the east where we can hoard things", "N:corridor,E:9,S:10,W:20");
        create.collectable("cursed2","A shiny cursed thing that reduces score","8").canHoard(true, 50);


        create.location("9","The room of hoarding", "This is a test room of hoarding", "W:8").setCanHoardTreasureHere(true);

        create.collectable("treasure1","A shiny treasure", "9").canHoard(true, 100);


        create.collectable("cursed1","A shiny cursed thing that reduces score","9").canHoard(true, 50);


        create.location("secretStash","Secret Storage Room", "An unfindable Secret Storage Room", "");

        create.collectable("junk1","A shiny thing that can not be hoarded", "9");


        create.collectable("treasure2","A valuable treasure", "secretStash").canHoard(true, 100);


        create.location("10","A corridor", "The corridor of collectable zapping", "N:8,E:11,S:12, W:21");
        create.locationObject("zapherebutton", "A Button", "Use the button to zap the thing","10");

        create.location("11","A room", "The room of collectable zapping", "W:10").setCanHoardTreasureHere(true);

        create.verbCondition("use")
                .when(When.NOUNPHRASE_EQUALS, "zapherebutton").  // when we use the button
                andWhen(When.LOCATION_OBJECT_IS_HERE, "zapherebutton").
                then(Then.TELEPORT_COLLECTABLE_TO_LOCATION, "treasure2:10").
                andThen(Then.FORCE_LOOK,"").
                andThen(Then.DISPLAY_MESSAGE, "You push the button and The zappable valuable treasure appears.").
                andThen(Then.LAST_ACTION_SUCCESS, "You instantiated greed.");



        // Stuff to inspect

        create.location("12","A inspect corridor", "The corridor of inspection", "N:10,E:13, S:14, W:22");
        create.location("13","A room", "The room of inspection", "W:12");

        create.collectable("inspectHoardable1","A valuable treasure", "13").canHoard(true, 97);

        create.collectable("inspectNotHoardable1","A very valuable treasure", "13").canHoard(false, 10000);
        create.collectable("inspectAbility1","A very special valuable treasure", "13").canHoard(true, 500).addsAbility("stink", 20);
        create.locationObject("notinspect", "A Thing I cannot inspect", "I can't inspect this","13");

        // Stuff to Examine
        create.location("14","An examine corridor", "The corridor of examining", "N:12,E:15,S:16,W:23");
        create.location("15","A room of examining things", "The room of examining", "W:14");

        create.collectable("examineThing2","A thing to take and then examine","14");

        create.locationObject("examineme", "A thing to examine", "OK, you examined it alright","15");

        create.collectable("examineThing1","A very valuable treasure but not much to examine","15");


        // TODO create a verb rule to allow me to examine a collectable
        // TODO create a verb rule that overrides a default location object description


        // darken illuminate
        create.location("16","An corridor of light and dark", "Leads to a room of darkness", "N:14,E:17,S:18");
        create.location("17","A room of darkness", "A Dark Room", "W:16").makeDark();

        create.collectable("aThingInTheDark","A thing on the floor","17").canHoard(true,10);

        create.collectable("aTorchOnTheFloor","A torch on the floor","16").addsToggleAbility(VerbGameAbilities.ILLUMINATE_DARKEN, 10, false);


        create.collectable("aBurntOutTorch","A used torch","16").addsToggleAbility(VerbGameAbilities.ILLUMINATE_DARKEN, 0, false);

        create.collectable("aLitTorchOnTheFloor","A lit torch on the floor","16")
                .addsToggleAbility(VerbGameAbilities.ILLUMINATE_DARKEN, 7, true);


        create.verbCondition("darken")
                .when(When.NOUNPHRASE_EQUALS, "aLitTorchOnTheFloor").  // when we use the button
                andWhen("user.iscarrying", "aLitTorchOnTheFloor").
                // TODO: allow showing messages when there is an error, at the moment have to put everything in the error report
                        andThen("lastAction.error", "There does not appear to be an off switch. It was lit when I picked it up. I think I'll leave it on, just in case it explodes in my face. You leave the lit torch alone.");

        // TODO: add the capability to not toggle an ability e.g. can't extinguish a lit torch

        // polish
        create.location("18","A corridor of polishing", "Leads to a room of polishing", "N:16,E:19");
        create.location("19","A room of polishing", "A Dark Room", "W:18");

        create.collectable("aThingToPolish","A thing to polish", "18").canHoard(true,150);
        create.collectable("aThingToNotPolish","A thing not worth polishing","18");
        create.collectable("aThingToPolishWith","A cloth of mighty polishing", "19").addsAbility("polish", 100);



        // counter
        create.location("20","A counter room", "The room with a counter", "E:8");

        create.locationObject("acounter", "A counter in the middle of the floor", "On the counter are some buttons that control counters","20");
        create.locationObject("redbutton", "A red push button on the counter", "The red button has an up arrow on it","20").setAsSecret(true);
        create.locationObject("bluebutton", "A blue push button on the counter", "The blue button has a down arrow on it","20").setAsSecret(true);

        // WARNING - secret location objects can still be 'use'd so I could 'use' the redbutton and it would say that nothing happened.
        // todo should I be able to examine counter to see the count

        // buttons only appear when "examine acounter"
        create.verbCondition("examine").
                when("nounphrase","acounter").
                andWhen(When.DOES_FLAG_EXIST, "examinedcounter:false").
                andThen(Then.SET_USER_FLAG, "examinedcounter:true").
                andThen(Then.OBJECT_SET_SECRET, "redbutton:false").
                andThen(Then.OBJECT_SET_SECRET, "bluebutton:false").
                andThen(Then.SET_USER_COUNTER,"counterbuttoncounter:0").
                andThen(Then.LAST_ACTION_SUCCESS,"You examine the counter and see some buttons on it");

        create.verbCondition("examine").
                when("nounphrase","acounter").
                andWhen(When.DOES_FLAG_EXIST, "examinedcounter:true").
                andWhen(When.DOES_USER_COUNTER_EXIST,"counterbuttoncounter:true").
                andThen(Then.LAST_ACTION_SUCCESS,"You examined the counter already, you saw some buttons. Remember?");

        create.verbCondition("examine").
                when("nounphrase","acounter").
                andWhen(When.DOES_FLAG_EXIST, "examinedcounterbuttonsgone:true").
                andWhen(When.IS_USER_FLAG_SET,"examinedcounterbuttonsgone:true").
                andThen(Then.LAST_ACTION_SUCCESS,"You examine the counter and the buttons have vanished. No more pushing buttons here for you.");

        defn.addVerb("push");

        create.priorityTurnCondition().
                when(When.USER_COUNTER_VALUE_IS, "counterbuttoncounter:==:3").
                andThen(Then.INCREMENT_USER_COUNTER, "counterbuttoncounter:4").     // this stops the next blue button continuing to score points immediately
                andThen(Then.DISPLAY_MESSAGE,"You heard a ding noise").
                andThen(Then.INCREMENT_SCORE,"20");

        create.priorityTurnCondition().
                when(When.USER_COUNTER_VALUE_IS, "counterbuttoncounter:==:10").
                andThen(Then.DELETE_USER_COUNTER, "counterbuttoncounter").
                andThen(Then.DISPLAY_MESSAGE,"You heard a dong noise").
                andThen(Then.OBJECT_SET_SECRET, "redbutton:true").
                andThen(Then.OBJECT_SET_SECRET, "bluebutton:true").
                andThen(Then.SET_USER_FLAG, "examinedcounterbuttonsgone:true").
                andThen(Then.INCREMENT_SCORE,"20");

        create.verbCondition("push").
                when("nounphrase","redbutton").
                andWhen(When.DOES_FLAG_EXIST, "examinedcounter:true").
                andWhen(When.DOES_FLAG_EXIST, "examinedcounterbuttonsgone:false").
                andWhen(When.LOCATION_ID_IS,"20").
                andWhen(When.IS_USER_FLAG_SET, "examinedcounter:true").
                andThen(Then.INCREMENT_USER_COUNTER, "counterbuttoncounter:1").
                andThen(Then.LAST_ACTION_SUCCESS, "You push the red button");

        create.verbCondition("push").
                when("nounphrase","bluebutton").
                andWhen(When.DOES_FLAG_EXIST, "examinedcounter:true").
                andWhen(When.DOES_FLAG_EXIST, "examinedcounterbuttonsgone:false").
                andWhen(When.LOCATION_ID_IS,"20").
                andWhen(When.IS_USER_FLAG_SET, "examinedcounter:true").
                andThen(Then.INCREMENT_USER_COUNTER, "counterbuttoncounter:-1").
                andThen(Then.LAST_ACTION_SUCCESS, "You push the blue button");


        // button on floor
        // push button on floor, button on wall
        // push buttonon wall, button on wall disappears, buttonon floor appears

        create.locationObject("greenbuttonfloor", "A green button on the floor", "You could push it, I don't know what it does","20").setAsSecret(true);
        create.locationObject("blackbuttonwall", "A black button on the wall", "You could push it, I don't know what it does","20");

        create.verbCondition("push").
                when(When.NOUNPHRASE_EQUALS,"blackbuttonwall").
                andWhen(When.LOCATION_ID_IS,"20").
                andWhen(When.DOES_FLAG_EXIST,"blackbuttonpress:false").
                andThen(Then.SET_USER_FLAG, "blackbuttonpress:true").
                andThen(Then.OBJECT_SET_SECRET, "greenbuttonfloor:false").
                andThen(Then.LAST_ACTION_SUCCESS, "A green button appears").
                andThen(Then.FORCE_LOOK,"");

        create.verbCondition("push").
                when(When.NOUNPHRASE_EQUALS,"blackbuttonwall").
                andWhen(When.LOCATION_ID_IS,"20").
                andWhen(When.DOES_FLAG_EXIST,"blackbuttonpress:true").
                andThen(Then.LAST_ACTION_ERROR, "You already pushed it, now it is flush with the wall and won't move").
                andThen(Then.FORCE_LOOK,"");

        create.verbCondition("push").
                when(When.NOUNPHRASE_EQUALS,"greenbuttonfloor").
                andWhen(When.LOCATION_ID_IS,"20").
                andWhen(When.DOES_FLAG_EXIST,"blackbuttonpress:true").
                andThen(Then.OBJECT_SET_SECRET, "greenbuttonfloor:true").
                andThen(Then.LAST_ACTION_SUCCESS, "The green button disappears, the black button slides out.").
                andThen(Then.DELETE_USER_FLAG, "blackbuttonpress").
                andThen(Then.FORCE_LOOK,"");



        // room 21 has a secret exit which is implemented by a verbCondition
        create.location("21","A room with a southward view", "The room with southernly direction", "E:10");
        create.verbCondition("go").
                when(When.LOCATION_ID_IS,"21").
                andWhen(When.DIRECTION,"s").
                then(Then.TELEPORT_PLAYER_TO_LOCATION,"10").
                andThen(Then.LAST_ACTION_SUCCESS, "You walk south where there is no exit and end up in a different room.").
                andThen(Then.FORCE_LOOK,"");

        // Note: this could be used to implement windows and alcoves e.g. look south




        create.location("22","A room with shown things", "The room with things that are shown", "E:12:secretGate22");
        create.locationObject("asignonwall", "A Sign on the Wall", "the sign says say boo","22");
        create.locationObject("hiddensign", "A Hidden Sign", "The sign says 'hidden in plain sight'", "22").setAsSecret(true);

        defn.addGate("secretGate22", GateStatus.CLOSED).gateIsHidden(true);

        defn.addVerb("say");

        create.verbCondition("say").
                when(When.LOCATION_ID_IS,"22").
                andWhen(When.NOUNPHRASE_EQUALS,"boo").
                then(Then.SHOW_LOCATION_OBJECT_IN_LOOK,"hiddensign:A sign that was previously hidden").
                then(Then.SHOW_COLLECTABLE_IN_LOOK, "anothing:A thing that does not exist").
                then(Then.SHOW_GATE,"secretGate22"). // can only show named gates
                then(Then.SET_USER_FLAG, "havesaidboo:true").
                then(Then.FORCE_LOOK, "").
                then(Then.DISPLAY_MESSAGE,"Stuff Happened").         // display message to prevent default "Nothing happened" message from template shown
                then(Then.LAST_ACTION_SUCCESS,"You say boo and are aware of more stuff.");

        create.location("23","The dispensery", "The room with a dispenser", "E:14");

        //Create a dispenser of torches
        MudLocationObject locationObj = create.dispenser("23","torchdispenser","A Torch Dispenser", "Use the dispenser to get a torch.");
        locationObj.setDispenserTemplate(MudCollectableTemplate.getDispenserTemplate("torch", "A Dispensed Torch", VerbGameAbilities.ILLUMINATE_DARKEN, 4, 20));

        //Create a dispenser of hoardable gold
        locationObj = create.dispenser("23","golddispenser","A Gold Dispenser", "Use the dispenser to get gold.");
        locationObj.setDispenserTemplate(MudCollectableTemplate.getDispenserTemplate("gold", "A Gold Nugget").
                setHoardableAttributes(true, 10, 100));


        // Use Code coverage from running ExampleDocumentedGameGenerator to identify
        // what other functionality to add to this game
        // examine
        // all scripting commands
        // etc.

    }
}
