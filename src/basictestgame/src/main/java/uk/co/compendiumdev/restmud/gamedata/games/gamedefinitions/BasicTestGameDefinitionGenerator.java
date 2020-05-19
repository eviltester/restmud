package uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions;

import uk.co.compendiumdev.restmud.engine.game.gamedefinition.GameDefinitionPopulator;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
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
public class BasicTestGameDefinitionGenerator implements GameDefinitionPopulator {
    public void define(MudGameDefinition defn) {
        VerbCondition cond = null;
        PriorityTurnCondition turn = null;
        MudLocationObject locationObj = null;
        MudLocation objectLocation = null;

        int total_score = 0;




        defn.setGameName("Basic Test Game");
        defn.setWelcomeMessage("Welcome to the Basic Test Game");

        defn.gameLocations().addLocation(new MudLocation("0","The Junk Room", "This is the room of Junk", "S:1"));


        cond = new VerbCondition("go");
        cond.when("direction", "s").
                andWhen("locationId","0").
                then("message", "You are glad to get out of there, that room stunk.").
                because("can create conditions for normal exits");

        defn.addCondition(cond);



        MudLocation startLocation = new MudLocation("1","The Central Test Room",
                "This is the room in the center of the test game", "N:2,E:3,S:4,W:5");

        defn.gameLocations().addLocation(startLocation);
        defn.setStartLocationId("1");



        locationObj = new MudLocationObject("ahint","A sign on the wall", "I think you can go south near the strong room.");

        defn.addLocationObjectIn(locationObj, "1");




        defn.gameLocations().addLocation(new MudLocation("2","The Dark room in the north", "This room is daaaaark and spooky", "S:1").
                            makeDark().
                            because("7 is permanently dark, hints in 4"));

        defn.gameLocations().addLocation(new MudLocation("3","The East Room", "This is the room in the east", "W:1,E:7:secret").
                            because("secret exits are not shown in the location description"));



        cond = new VerbCondition("go");
        cond.when("direction", "e").
                andWhen("locationId","3").
                andWhen("doesflagexist","knowAboutSecretRoomIn3:false").
                then("lastAction.error","I can't go that way. What makes you think I can go that way!");
        defn.addCondition(cond);



        cond = new VerbCondition("go");
        cond.when("direction", "e").
                andWhen("locationId","3").
                andWhen("doesflagexist","foundSecretRoomIn3:false").
                then("message", "Hey, that was strange, I didn't see the exit, it must have been an optical illusion or something.")
                .andThen("incrementScore","10").
                andThen("setUserFlag","foundSecretRoomIn3:true").
                andThen("message", "Your exploration earned you some points on your score");
        defn.addCondition(cond);




        total_score += 10;

        locationObj = new MudLocationObject("mazesign","A Sign", "This way to the maze. Are you ready to map your way out of danger? Use the lever if you dare");
        defn.addLocationObjectIn(locationObj, "3");


        locationObj = new MudLocationObject("mazelever","A lever on the wall",
                "The lever looks unused, shiny, almost as if the maze has not been used, or tested. I'm not sure I'd use the mazelever. But I'm not as brave as you.");
        defn.addLocationObjectIn(locationObj, "3");




        cond = new VerbCondition("use");
        cond.when("nounphrase", "mazelever").
                andWhen("locationId", "3").
                andWhen("locationObject.isHere", "mazelever").
                then("teleportUserToLocation","14").
                andThen("lastAction.success","You use the lever and zap yourself into a maze off the beaten path. Oops. I hope you know how to get out of here.");
        defn.addCondition(cond);



        defn.gameLocations().addLocation(new MudLocation("4","The South Room", "This is the room in the South", "N:1,E:6:westerngate"));



        locationObj = new MudLocationObject("adirsign","A sign on the wall", "Since this is room four, I'll tell you some more, you can go east when you think you can't go east no more, and then you can read this sign some more.");
        defn.addLocationObjectIn(locationObj, "4");


        defn.gameLocations().addLocation(new MudLocation("5","The West Hoard Room", "This is the room in the West where everyone stores all their stuff, there is a strong room door to the west", "E:1, W:local, S:12:secret")
                .setCanHoardTreasureHere(true).
                because("a 'local' exit means it shows in the list of exits but the commands are handled by scripts"));




        locationObj = new MudLocationObject("hintsigne", "A sign on the wall", "Shh, GO Easily, I think I see a secret way in Room '3'. Shhh.")
                                .because("examinable object in room 5 gives a hint about the secret room exit in 3");
        defn.addLocationObjectIn(locationObj, "5");



        cond = new VerbCondition("examine");
        cond.when("nounphrase", "hintsigne").
                andWhen("locationId","5").
                andWhen("doesflagexist","knowAboutSecretRoomIn3:false").
                then("setUserFlag", "knowAboutSecretRoomIn3:true");
        defn.addCondition(cond);



        cond = new VerbCondition("go");
        cond.when("direction", "w").
                andWhen("locationId","5").
                then("message", "Nope. No-one can enter the strong room, you might steal stuff. But I like your initiative.");
        defn.addCondition(cond);



        defn.gameLocations().addLocation(new MudLocation("6","The West Corridor", "This is the western corridor",
                            "W:4:westerngate,E:8:secret:secretLeverPanel"));

        defn.addGate("westerngate", GateStatus.CLOSED).because("create a simple door between 4 and 6");

        defn.addGate("secretLeverPanel", GateStatus.CLOSED).
                gateIsHidden(true).    // this gate should be secret true
                playerCanOpen(false).  // player can not open it
                playerCanClose(true).  // but they can close it
                setShortDescription("Secret Panel").
                gateAutoCloses(true).  // it will auto close when player goes through
                gateAutoHides(true).   // gate will auto hide when closed
                because("create a one way secret panel which has no corresponding exits to room 8, see 7 for how to open");

        defn.gameLocations().addLocation(new MudLocation("7","The Secret East Room", "This is the secret room in the east, there is a lever on the wall", "W:3"));



        locationObj = new MudLocationObject("alever","A Lever on the wall", "The lever has a <span id='leverbutton' class='locationobject'>button</span> with a fingerprint worn on it from much use.");
        defn.addLocationObjectIn(locationObj,"7");



        locationObj = new MudLocationObject("leverbutton","A button on a lever", "The button looks like it has been worn down over centuries of use. Or perhaps it was just a really cheap button.");
        locationObj.setAsSecret(true); // we can not see the button in the description but can still use them
        defn.addLocationObjectIn(locationObj,"7");



        cond = new VerbCondition("use");
        cond.when("nounphrase", "leverbutton").
                andWhen("locationId", "7").
                andWhen("locationObject.isHere", "leverbutton").
                then("openGate","secretLeverPanel").
                andThen("showGate", "secretLeverPanel").
                andThen("lastAction.success","You press the button on the lever. You hear a squeal of wood and metal as something opens somewhere.");
        defn.addCondition(cond);



        cond = new VerbCondition("use");
        cond.when("nounphrase", "alever").
                andWhen("locationId", "7").
                andWhen("locationObject.isHere", "alever").
                andThen("lastAction.success","I can't use the lever, it seems to be rusted and unmoveable, have you examined it carefully?");
        defn.addCondition(cond);




        cond = new VerbCondition("examine");
        cond.when("nounphrase", "alever").
                when("locationId", "7").
                andWhen("doesFlagExist","userExaminedTheGateLever:false").
                then("setUserFlag", "userExaminedTheGateLever:true");
        defn.addCondition(cond);



        turn = new PriorityTurnCondition();
        turn.when("locationId", "7").
                when("doesFlagExist","userExaminedTheGateLever:true").
                then("look.showlocationobject","leverbutton:A much used lever button");
        defn.addCondition(turn);



        defn.gameLocations().addLocation(new MudLocation("8","The Room behind the gate in the East", "This is the room behind the secret gate, you see no reason for the fuss", "W:6,E:9"));






        locationObj = new MudLocationObject("wobblyswitch","A wobbly switch on the wall", "Someone has written something beside the switch, it looks like it says 'wobble me'");
        defn.addLocationObjectIn(locationObj, "8");



        // custom verb for the wobbly switch
        defn.addVerb("wobble");




        cond = new VerbCondition("wobble");
        cond.when("nounphrase", "wobblyswitch").
                andWhen("locationId", "8").
                andWhen("doesFlagExist", "userHasWobbledSwitch:false").
                //andWhen("isFlagSet", "userLickedStone:false").
                        then("setUserFlag","userHasWobbledSwitch:true").
                then("incrementScore","20").
                andThen("lastAction.success","You wobble the switch - and your score increased. But I don't think you should try that again, the wiz might not like that.");
        defn.addCondition(cond);



        total_score+=20;

        cond = new VerbCondition("wobble");
        cond.when("nounphrase", "wobblyswitch").
                andWhen("locationId", "8").
                andWhen("isFlagSet", "userHasWobbledSwitch:true").
                then("incrementScore","-5").
                andThen("lastAction.error","You wobble the switch - and you felt your score change. But I think it dropped. The wiz didn't like that. I did warn you.").
                because("we will only ever lick it once");
        defn.addCondition(cond);

        defn.gameLocations().addLocation(new MudLocation("9","An Eastern Corridor", "This corridor goes east", "W:8,E:10"));


        turn = new PriorityTurnCondition();
        turn.when("locationId","9").
                andWhen(When.DOES_FLAG_EXIST, "userHasPickedUpAnElvenPipe:false").
                then(Then.DISPLAY_MESSAGE, "You can see <span id='atinypipe' class='collectable'>a tiny pipe on the floor</span> on the floor").
                because("create a timed condition in room 9")
        ;
        defn.addCondition(turn);



        turn = new PriorityTurnCondition();
        turn.when("locationId","9").
                andWhen(When.DOES_FLAG_EXIST, "userHasPickedUpAnElvenPipe:false").
                andWhen(When.IS_USER_FLAG_SET, "userHasExaminedAnElvenPipe:true").
                then(Then.SHOW_COLLECTABLE_IN_LOOK, "atinypipe:A Tiny Pipe").
                because("if we examined it then we can tempt the user to pick it up")
        ;
        defn.addCondition(turn);



        turn = new PriorityTurnCondition();
        turn.when("isFlagSet", "userHasPickedUpAnElvenPipe:true").
                andWhen("isCounterValue", "userHasPickedUpAnElvenPipeAndElfHasNoticed:>:0").
                then("message", "You feel a bit guilty about taking the Elf's pipe, but since it disappeared you feel less worried. But still a bit worried.").
                andThen("incrementCounter", "userHasPickedUpAnElvenPipeAndElfHasNoticed:-1");
        defn.addCondition(turn);



        turn = new PriorityTurnCondition();
        turn.when("isFlagSet", "userHasPickedUpAnElvenPipe:true").
                andWhen("isCounterValue","userHasPickedUpAnElvenPipeAndElfHasNoticed:<=:0").
                then("setUserFlag", "userHasPickedUpAnElvenPipe:false").
                then("deleteUserCounter", "userHasPickedUpAnElvenPipeAndElfHasNoticed").
                andThen("message", "An elf appears and bashes you on the head with a tiny hammer. Ouch. That tiny hammer really hurts.").
                andThen("incrementScore", "-5").
                andThen("message", "You feel as through your score has decreased as a result.").
                because("run out of time");
        defn.addCondition(turn);




        cond = new VerbCondition("examine");
        cond.when("nounphrase", "atinypipe").
                andWhen("locationId","9").
                andWhen("doesFlagExist", "userHasPickedUpAnElvenPipe:false").
                then(Then.SET_USER_FLAG, "userHasExaminedAnElvenPipe:true").
                then("lastAction.success", "You look at the tiny pipe. It appears to have 'This is mine. Do not touch. Signed Tiny Elf' written on it in tiny writing, to be honest, it is so tiny that you could almost claim to have not seen the writing.");
        defn.addCondition(cond);



        cond = new VerbCondition("take");
        cond.when("nounphrase", "atinypipe").
                andWhen("locationId","9").
                andWhen("doesFlagExist", "userHasPickedUpAnElvenPipe:false").
                then("setUserFlag","userHasPickedUpAnElvenPipe:true").
                then("setUserCounter","userHasPickedUpAnElvenPipeAndElfHasNoticed:10").
                andThen("lastAction.success", "You bend over and pick up the Tiny pipe, it vanishes before you can put it in your pocket. Oh well, easy come, easy go.");
        defn.addCondition(cond);



        cond = new VerbCondition("examine");
        cond.when("nounphrase", "atinypipe").
                andWhen("locationId","9").
                andWhen("isflagset", "userHasPickedUpAnElvenPipe:true").
                then("lastAction.error", "You can't examine the tiny pipe, it disappeared remember.");
        defn.addCondition(cond);




        defn.gameLocations().addLocation(new MudLocation("10","Another Eastern Corridor", "This corridor goes east and west", "W:9,E:11"));


        locationObj = new MudLocationObject("asinsign","A sign on the wall", "confess <insert your sinning item here> and all will be forgiven. Or don't and it won't. Up to you. You smell like poo!").
                because("the hint that you need to confess/atinypipe");
        defn.addLocationObjectIn(locationObj, "10");

        defn.addVerb("confess");



        cond = new VerbCondition("confess");
        cond.when("nounphrase", "atinypipe").
                andWhen("locationId", "10").
                andWhen("isflagset", "userHasPickedUpAnElvenPipe:true").
                then("setUserFlag","userHasPickedUpAnElvenPipe:false").
                andThen("incrementScore","10").
                andThen("message", "You feel as though your score has increased.").
                andThen("lastAction.success", "You confess your sin of taking the tiny pipe to the sign with an angry face. A tiny elf appears and says 'I forgive you, but do not do it again' and the Elf hits you on the nose with a tiny feather. Ow, that really hurt. What was the feather made out of? Steel? The Elf says 'Ha, do it again and I hit you with something other than my magic lead feather of much pain'. The Elf disappears, leaving you and your pain to wallow in your guilt. Happy Days.");
        defn.addCondition(cond);



        total_score+=10;

        cond = new VerbCondition("confess");
        cond.andWhen("locationId", "10").
                andWhen("isflagset", "userHasPickedUpAnElvenPipe:true").
                andThen("lastAction.error", "Confess WHAT? you 'orrible little adventurer. You should feel guilty. But that is not good enough. WHAT?").
                because("generic confess handler after having picked up the pipe");
        defn.addCondition(cond);



        cond = new VerbCondition("confess");
        cond.andWhen("locationId", "10").
                andThen("lastAction.error", "Confess WHAT? Are you really guilty of anything?").
                because("generic confess handler");
        defn.addCondition(cond);



        defn.gameLocations().addLocation(new MudLocation("11","Another Eastern Corridor but this one is really dark", "This corridor goes east best but west by default", "W:10,E:4:secret").makeDark());



        cond = new VerbCondition("go");
        cond.when("direction","e").
                andWhen("locationId", "11").
                then("message", "Ooer, that felt funny").
                andThen("incrementScore","10");
        defn.addCondition(cond);

        total_score+=10;






        // add some collectibles
        MudCollectable cloth = new MudCollectable(defn.idGenerator().generateId("cloth"), "A Cloth of Shining");
        cloth.addsAbility("polish",200);

        MudLocation alocation = defn.gameLocations().get("2");
        defn.addCollectable(cloth, alocation);




        // create a torch
        MudCollectable torch = new MudCollectable(defn.idGenerator().generateId("torch"), "A Torch of Illumination");
        torch.addsToggleAbility(VerbGameAbilities.ILLUMINATE_DARKEN,10, false);

        alocation = defn.gameLocations().get("7");

        defn.addCollectable(torch, alocation);




        defn.gameLocations().addLocation(new MudLocation("12","A secret area", "A secret area must be around here somewhere. Oh wait a minute, this is it.", "N:5,E:13:secret"));




        // create a torch Dispenser

        locationObj = new MudLocationObject("torchdispenser","A Torch Dispenser", "Use the dispenser to get a torch.");
        locationObj.setIsADispenser(true);
        locationObj.setDispenserTemplate(MudCollectableTemplate.getDispenserTemplate("torch", "A Dispensed Torch", VerbGameAbilities.ILLUMINATE_DARKEN, 4, 20));
        defn.addLocationObjectIn(locationObj, "12");


        defn.gameLocations().addLocation(new MudLocation("13","A secret treasure stash", "Oh, this is the secret treasure stash.", "W:12"));



        // a default collectable
        MudCollectable aCollectable;
        MudLocation location;

        aCollectable = new MudCollectable("shiny_gold_ring","A shiny gold ring");
        aCollectable.canHoard(true,200);
        location = defn.gameLocations().get("13");
        defn.addCollectable(aCollectable, location);



        total_score+=200;
        total_score+=200; // if we polish it

        aCollectable = new MudCollectable("cursed_gold_ring","A very shiny gold ring");
        aCollectable.canHoard(true,-200);

        location = defn.gameLocations().get("13");

        defn.addCollectable(aCollectable, location);



        aCollectable = new MudCollectable("heavy_ring","A very heavy gold looking ring");
        aCollectable.canHoard(false,0);

        location = defn.gameLocations().get("13");

        defn.addCollectable(aCollectable, location);



        defn.gameLocations().addLocation(new MudLocation("14","The Start of the Maze", "You are in a maze of twisty tiny passages constructed by a loony who is experimenting with his game engine, this can't be good.", "N:15,E:3,S:16,W:17"));
        locationObj = new MudLocationObject("startmazesign","A helpful sign", "Even you can find an Easy Egress if you Go the Easy Exit, Especially Early in the maze Environment");
        defn.addLocationObjectIn(locationObj, "14");



        cond = new VerbCondition("go");
        cond.when("direction","e").
                andWhen("locationId", "14").
                then("message", "Ah, you took the easy way out. splendid. Experience the pain of losing points you LOSER... Loser..... looooser... looooooooooser. You might think that was my voice fading into the distance, but it wasn't LOOOOOOSER.").
                andThen("incrementScore","-10");
        defn.addCondition(cond);

        defn.gameLocations().addLocation(new MudLocation("15","In the maze", "You are in a maze of twisty tiny maze passages all alike. I wonder why?", "N:15,E:17,S:15,W:14"));
        defn.gameLocations().addLocation(new MudLocation("16","In the large cavernous maze passage", "You are in a maze of twisty tiny maze passages where some are very large, but all are alike.", "S:17,E:16,N:16,W:15"));
        defn.gameLocations().addLocation(new MudLocation("17","In the dark part of the maze", "You are in a maze of dark twisty tiny maze passages, but all are alike.", "S:17,E:17,N:17,W:15").makeDark());



        locationObj = new MudLocationObject("amazelever17","A dark lever", "On the white wall in front of you is a lever painted black to blend into the wall if the light was not on");
        defn.addLocationObjectIn(locationObj, "17");

        cond = new VerbCondition("use");
        cond.when("nounphrase","amazelever17").
                andWhen("locationId", "17").        // TODO: only if location is lit and I can see
                then("message", "Ah, this looks more promising.").
                then("teleportUserToLocation","18").
                andThen("incrementScore","10").andThen("lastAction.success","You use the lever and find yourself in new set of twisty tiny passages");
        defn.addCondition(cond);



        defn.gameLocations().addLocation(new MudLocation("18","Near the end of the maze", "You are near the end of the maze.", "S:17,E:20:secret,N:19:secret,W:19:secret"));
        locationObj = new MudLocationObject("endmazebutton","A button", "The shiny red button in front of you screams 'do not push me'. Literally. It was quite loud.");
        defn.addLocationObjectIn(locationObj, "18");

        cond = new VerbCondition("go");
        cond.when("direction","e").
                andWhen("locationId", "18").
                andWhen("doesflagexist", "userFoundTheMazeSecret:false").
                then("message", "You found the secret. If you are first to find the secret then there is a prize. If not, then just think of all the work you did to increase your score by 300 points. Yes 300!!!! points. How much is the prize worth you wonder?").
                andThen("incrementScore","300").
                andThen("setUserFlag","userFoundTheMazeSecret:true");
        defn.addCondition(cond);
        total_score+=300;



        locationObj = new MudLocationObject("endmazebuttonsign","A sign", "The end maze button does what you think it would do if you use it. Bit of a let down that. But not if you found the secret of the maze, though. You did find it RIGHT?");
        defn.addLocationObjectIn(locationObj, "18");

        cond = new VerbCondition("use");
        cond.when("nounphrase","endmazebutton").
                andWhen("locationId", "18").
                then("message", "And you finish the maze.").
                then("teleportUserToLocation","3").
                andThen("incrementScore","20").andThen("lastAction.success","You escaped from the maze like a... ehm... an... umm... maze escaper person");
        defn.addCondition(cond);
        total_score+=20;



        defn.gameLocations().addLocation(new MudLocation("19","A secret of the maze!", "You are in an empty room.", "S:18,E:18,N:18,W:18:secret"));
        defn.gameLocations().addLocation(new MudLocation("20","The secret of the maze!", "You are in an empty room.", "S:18:secret,E:18:secret,N:18:Secret,W:18:secret"));

        aCollectable = new MudCollectable("the_secret_of_the_maze_prize","A very prize like prize");
        aCollectable.canHoard(true,400);
        defn.addCollectable(aCollectable, defn.gameLocations().get("20"));

        total_score+=400;




        defn.setTotalScore(total_score);
    }
}
