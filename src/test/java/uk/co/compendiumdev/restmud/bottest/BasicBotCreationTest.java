package uk.co.compendiumdev.restmud.bottest;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import uk.co.compendiumdev.restmud.api.RestMudAPI;
import uk.co.compendiumdev.restmud.api.RestMudResponseProcessor;
import uk.co.compendiumdev.restmud.bottest.botstrategies.action.*;
import uk.co.compendiumdev.restmud.bottest.botstrategies.waiting.RandomWaitStrategy;
import uk.co.compendiumdev.restmud.output.json.VisibleExit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Ignore("Integration test using heroku")
public class BasicBotCreationTest {

    private final String REST_MUD_URL = "http://restmud.herokuapp.com";
    private final String REGISTRATION_SECRET = "CHANGEME";

    // TODO: Bots that know specific game strategies e.g. push certain buttons in certain locations
    // TODO: Bots that choose stratgies based on conditions e.g. if in room with hoard then hoard stuff, not randomly choose to leave
    // TODO: hoarder - when it finds a hoard it tries to hoard everything
    // TODO: Inspect hoarder - inspects stuff before hoarding if they are worth anything
    // TODO: Random Dropper - randomly drops X number of things in inventory


    @Test
    public void createAMultiUserRestMudTestBot(){

        RestMudTestBot myFirstBot = new RestMudTestBot(REST_MUD_URL).setUserName("zzzamd64Windows811488121657822");

        // only need to register on multiplayer
        myFirstBot.needsToRegisterOnSystem(true);
        myFirstBot.setRegistrationSecretCode(REGISTRATION_SECRET);
        // only need to login if multiplayer
        myFirstBot.setPassword("password");
        myFirstBot.register();
        myFirstBot.login();


        myFirstBot.addActionStrategy(new WalkerStrategy().canOpenDoors(true)); // walks about and can open doors if needed
        //myFirstBot.addActionStrategy(new AllDoorCloserStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,2000)));
        //myFirstBot.addActionStrategy(new RandomDoorCloserStrategy()); // closes a random door
        //myFirstBot.addActionStrategy(new RandomDoorOpenerStrategy()); // opens a random door
        myFirstBot.addActionStrategy(new MadPolisherStrategy());
        myFirstBot.addActionStrategy(new MadHoarderStrategy());
        myFirstBot.addActionStrategy(new RandomTakerStrategy()); // takes a random collectable
        myFirstBot.addActionStrategy(new RandomExaminerStrategy()); // examines a random thing in the room
        myFirstBot.addActionStrategy(new RandomUseStrategy()); // uses a random thing in the room
        myFirstBot.addActionStrategy(new AllDoorOpenerStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,1000)));

        myFirstBot.addWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(0,100));

        for(int x=0;x<2000;x++) {
            myFirstBot.executeARandomActionStrategy();
            myFirstBot.waitAWhile();
        }
    }

    @Test
    public void createARestMudTestBot(){

        RestMudTestBot myFirstBot = new RestMudTestBot(REST_MUD_URL).setUserName("user");

        myFirstBot.setPassword("password");
        // only need to login if multiplayer
        //myFirstBot.login();


        myFirstBot.addActionStrategy(new WalkerStrategy().canOpenDoors(false)); // walks about and can open doors if needed
        myFirstBot.addActionStrategy(new AllDoorCloserStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,2000)));
        myFirstBot.addActionStrategy(new RandomDoorCloserStrategy()); // closes a random door
        myFirstBot.addActionStrategy(new RandomDoorOpenerStrategy()); // opens a random door
        myFirstBot.addActionStrategy(new RandomTakerStrategy()); // takes a random collectable
        myFirstBot.addActionStrategy(new RandomExaminerStrategy()); // examines a random thing in the room
        myFirstBot.addActionStrategy(new RandomUseStrategy()); // uses a random thing in the room
        myFirstBot.addActionStrategy(new AllDoorOpenerStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,1000)));

        myFirstBot.addWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(0,100));

        for(int x=0;x<2000;x++) {
            myFirstBot.executeARandomActionStrategy();
            myFirstBot.waitAWhile();
        }
    }

    @Test
    public void createThreadedRestMudTestBots(){

        // TODO: bots get stuck in room 22 on example_game - fix room 22 and walkthrough stuff

        // register users so we can create a lot of bots
        String secretRegistrationCode = REGISTRATION_SECRET;

        ThreadedRestMudTestBot myFirstThreadedBot = new ThreadedRestMudTestBot(REST_MUD_URL,"alan", "password",null);
        myFirstThreadedBot.getBot().addActionStrategy(new WalkerStrategy().canOpenDoors(false)); // walks about and can open doors if needed
        myFirstThreadedBot.getBot().addActionStrategy(new AllDoorCloserStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,2000)));
        myFirstThreadedBot.getBot().addActionStrategy(new RandomDoorCloserStrategy()); // closes a random door
        myFirstThreadedBot.getBot().addActionStrategy(new RandomDoorOpenerStrategy()); // opens a random door
        myFirstThreadedBot.getBot().addActionStrategy(new RandomTakerStrategy()); // takes a random collectable
        myFirstThreadedBot.getBot().addActionStrategy(new RandomExaminerStrategy()); // examines a random thing in the room
        myFirstThreadedBot.getBot().addActionStrategy(new RandomUseStrategy()); // uses a random thing in the room
        myFirstThreadedBot.getBot().addActionStrategy(new AllDoorOpenerStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,1000)));
        //myFirstThreadedBot.getBot().addWaitingStrategy(new FixedWaitStrategy().waitTime(500));
        myFirstThreadedBot.start();

        ThreadedRestMudTestBot mySecondThreadedBot = new ThreadedRestMudTestBot(REST_MUD_URL,"bob", "password", null);
        mySecondThreadedBot.getBot().addActionStrategy(new WalkerStrategy());
        mySecondThreadedBot.getBot().addActionStrategy(new WalkerStrategy().canOpenDoors(false)); // walks about and can open doors if needed
        mySecondThreadedBot.getBot().addActionStrategy(new AllDoorCloserStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,2000)));
        mySecondThreadedBot.getBot().addActionStrategy(new RandomDoorCloserStrategy()); // closes a random door
        mySecondThreadedBot.getBot().addActionStrategy(new RandomDoorOpenerStrategy()); // opens a random door
        mySecondThreadedBot.getBot().addActionStrategy(new RandomTakerStrategy()); // takes a random collectable
        mySecondThreadedBot.getBot().addActionStrategy(new RandomExaminerStrategy()); // examines a random thing in the room
        mySecondThreadedBot.getBot().addActionStrategy(new RandomUseStrategy()); // uses a random thing in the room
        mySecondThreadedBot.getBot().addActionStrategy(new AllDoorOpenerStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,1000)));
        //mySecondThreadedBot.getBot().addWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,2000));
        mySecondThreadedBot.start();

        // give the bots a chance to start
        justSleep(2000);

        int timeToRunInMillis = 20000;
        long startTime = System.currentTimeMillis();

        // while bots are running
        while(myFirstThreadedBot.running || mySecondThreadedBot.running){
            justSleep(500);
            if(System.currentTimeMillis()>(startTime+timeToRunInMillis)){
                myFirstThreadedBot.stop();
                mySecondThreadedBot.stop();
            }
        }

        mySecondThreadedBot.getBot().api.look();
        mySecondThreadedBot.getBot().api.score();
        mySecondThreadedBot.getBot().api.inventory();

        myFirstThreadedBot.getBot().api.look();
        myFirstThreadedBot.getBot().api.score();
        myFirstThreadedBot.getBot().api.inventory();

    }

    public void justSleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createRegisterAndUseThreadedRestMudTestBots(){

        // TODO: bots get stuck in room 22 on example_game - fix room 22 and walkthrough stuff

        // register users so we can create a lot of bots

        //https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html
        String secretRegistrationCode = REGISTRATION_SECRET;
        int numberOfBotsToUse=50;
        List<ThreadedRestMudTestBot>bots = new ArrayList<>();

        for(int thisUserId=0; thisUserId<numberOfBotsToUse; thisUserId++){
            String aUserName = "bot"+System.getProperty("os.arch") + System.getProperty("os.name") + System.currentTimeMillis();
            aUserName = aUserName.replaceAll(" ","");
            aUserName = aUserName.replaceAll("\\.","");

            ThreadedRestMudTestBot myThreadedBot = new ThreadedRestMudTestBot(REST_MUD_URL, aUserName, "password", secretRegistrationCode);
            myThreadedBot.getBot().addActionStrategy(new WalkerStrategy().canOpenDoors(true)); // walks about and can open doors if needed
            //myThreadedBot.getBot().addActionStrategy(new AllDoorCloserStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,2000)));
            //myThreadedBot.getBot().addActionStrategy(new RandomDoorCloserStrategy()); // closes a random door
            //myThreadedBot.getBot().addActionStrategy(new RandomDoorOpenerStrategy()); // opens a random door
            myThreadedBot.getBot().addActionStrategy(new RandomTakerStrategy()); // takes a random collectable
            myThreadedBot.getBot().addActionStrategy(new RandomExaminerStrategy()); // examines a random thing in the room
            myThreadedBot.getBot().addActionStrategy(new RandomUseStrategy()); // uses a random thing in the room
            myThreadedBot.getBot().addActionStrategy(new MadPolisherStrategy());   //
            myThreadedBot.getBot().addActionStrategy(new MadPolisherStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,2000)));
            myThreadedBot.getBot().addActionStrategy(new MadHoarderStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,2000)));
            myThreadedBot.getBot().addActionStrategy(new AllDoorOpenerStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,1000)));
            myThreadedBot.getBot().addWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(2000,4000));
            myThreadedBot.start();

            //justSleep(1000);

            bots.add(myThreadedBot);

        }


        // give the bots a chance to start
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        areBotsAlive(bots);

        int timeToRunInMillis = 1000000;
        long startTime = System.currentTimeMillis();

        // while bots are running
        while(botsAreRunning(bots) && areBotsAlive(bots)){

            justSleep(1000);

            if(System.currentTimeMillis()>(startTime+timeToRunInMillis)){
                stopBots(bots);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            areBotsAlive(bots);
        }

        // give the bots a chance to end
        justSleep(10000);

        reportBots(bots);


    }

    private boolean areBotsAlive(List<ThreadedRestMudTestBot> bots) {
        boolean allalive = false;
        for(ThreadedRestMudTestBot bot : bots){
            boolean alive = bot.alive();
            allalive = allalive | alive;
            System.out.println(bot.getBot().getUserName() + " alive status: " + alive);
        }
        return allalive;
    }

    private void reportBots(List<ThreadedRestMudTestBot> bots) {

        int []numberOfStates = {0,0,0,0,0};

        System.out.println("Reporting on bots");
        for(ThreadedRestMudTestBot bot : bots){
            bot.getBot().api.refreshConnection();   // I think connections time out before reporting so create new connection for reporting
            bot.getBot().api.look();
            bot.getBot().api.score();
            bot.getBot().api.inventory();
            int statesCount = bot.reportOnStates();
            int currentCount =  numberOfStates[statesCount];
            numberOfStates[statesCount] = currentCount+1;

            bot.getBot().api.reportStats();
        }

        System.out.println("State Count");
        for(int stateCount=0;stateCount<=4;stateCount++){
            System.out.println( stateCount + " = " + numberOfStates[stateCount] );
        }
    }

    private void stopBots(List<ThreadedRestMudTestBot> bots) {
        for(ThreadedRestMudTestBot bot : bots){
            if(bot.isRunning() && bot.alive()){
                System.out.println("Stopping bot " + bot.getBot().getUserName());
                bot.stop();
                justSleep(1000);
            }
        }
    }

    private boolean botsAreRunning(List<ThreadedRestMudTestBot> bots) {
        System.out.println("Checking if bots are running ");
        for(ThreadedRestMudTestBot bot : bots){
            if(bot.isRunning()){
                System.out.println(bot.getBot().getUserName() + " is still running");
                return true;
            }
        }
        return false;
    }


    @Test
    public void canUseTheAPIAsMultiUser(){
        RestMudAPI api = new RestMudAPI(REST_MUD_URL).setForUser("alan");

        api.login("password");

        // multi user - uses Basic Auth or an Auth Header - login sets up a basic auth header


        // WALK ABOUT STRATEGY - LOOK, then GO in a random direction
        RestMudResponseProcessor response = api.look();
        Assert.assertTrue(response.isSuccessful());

        // choose a random direction and go
        List<uk.co.compendiumdev.restmud.output.json.VisibleExit> exits = response.result.look.exits;
        if(exits!=null) {
            if(exits.size()>0){
                int chosenExit = new Random().nextInt(exits.size());
                // check for gates before going there i.e. if it is closed then try to open it first
                RestMudResponseProcessor goResponse = api.go(exits.get(chosenExit).direction);
            }
        }
    }

    @Test
    public void canUseTheAPIAsSingleUser(){
        RestMudAPI api = new RestMudAPI(REST_MUD_URL).setForUser("user");

        //api.login("password");

        // WALK ABOUT STRATEGY - LOOK, then GO in a random direction
        RestMudResponseProcessor response = api.look();
        Assert.assertTrue(response.isSuccessful());

        // choose a random direction and go
        List<VisibleExit> exits = response.result.look.exits;
        if(exits!=null) {
            if(exits.size()>0){
                int chosenExit = new Random().nextInt(exits.size());
                // check for gates before going there i.e. if it is closed then try to open it first
                RestMudResponseProcessor goResponse = api.go(exits.get(chosenExit).direction);
            }
        }
    }



}
