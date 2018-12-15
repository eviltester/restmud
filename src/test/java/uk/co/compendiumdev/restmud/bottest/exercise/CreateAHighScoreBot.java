package uk.co.compendiumdev.restmud.bottest.exercise;

import org.junit.Test;
import uk.co.compendiumdev.restmud.bottest.RestMudTestBot;
import uk.co.compendiumdev.restmud.bottest.botstrategies.action.*;
import uk.co.compendiumdev.restmud.bottest.botstrategies.waiting.RandomWaitStrategy;

import java.util.UUID;

public class CreateAHighScoreBot {

    /*
        Exercise: Change the following to match the environment and challenge
        Add your username and password for the game
        if you are using an already registered user then remove the `myFirstBot.register();` line below
     */
    private final String REST_MUD_URL = "http://restmud.herokuapp.com";
    private final String REGISTRATION_SECRET = "handsontech"; // "CHANGEME";
    private String YOUR_DESIRED_USER_NAME = UUID.randomUUID().toString();
    private String YOUR_DESIRED_PASSWORD = UUID.randomUUID().toString();
    private int CHALLENGE_TURNS_AVAILABLE = 100; // max about 1.5 minutes to run

    //


    @Test
    public void createAHighScoringMultiUserRestMudTestBot(){

        RestMudTestBot myFirstBot = new RestMudTestBot(REST_MUD_URL).setUserName(YOUR_DESIRED_USER_NAME);

        myFirstBot.needsToRegisterOnSystem(true);
        myFirstBot.setRegistrationSecretCode(REGISTRATION_SECRET);
        myFirstBot.setPassword(YOUR_DESIRED_PASSWORD);
        myFirstBot.register();
        myFirstBot.login();

        // Add any strategies you need here
        // Amend any of the strategies if you want
        // create any new strategies that you want
        myFirstBot.addActionStrategy(new WalkerStrategy().canOpenDoors(true)); // walks about and can open doors if needed
        myFirstBot.addActionStrategy(new MadHoarderStrategy());
        myFirstBot.addActionStrategy(new MadHoarderStrategy());
        myFirstBot.addActionStrategy(new MadHoarderStrategy());
        myFirstBot.addActionStrategy(new MadHoarderStrategy());
        myFirstBot.addActionStrategy(new RandomTakerStrategy()); // takes a random collectable
        myFirstBot.addActionStrategy(new RandomTakerStrategy()); // takes a random collectable
        myFirstBot.addActionStrategy(new RandomTakerStrategy()); // takes a random collectable
        myFirstBot.addActionStrategy(new RandomTakerStrategy()); // takes a random collectable
        //myFirstBot.addActionStrategy(new RandomExaminerStrategy()); // examines a random thing in the room
        //myFirstBot.addActionStrategy(new RandomUseStrategy()); // uses a random thing in the room
        //myFirstBot.addActionStrategy(new AllDoorOpenerStrategy().setWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(500,1000)));

        // Do not change the waiting strategy
        myFirstBot.addWaitingStrategy(new RandomWaitStrategy().waitTimeBetween(100,500));

        for(int x=0;x<CHALLENGE_TURNS_AVAILABLE;x++) {
            myFirstBot.executeARandomActionStrategy();
            myFirstBot.waitAWhile();
        }

        // Final Reports
        myFirstBot.api().look();
        myFirstBot.api().inventory();
        myFirstBot.api().score();

    }
}
