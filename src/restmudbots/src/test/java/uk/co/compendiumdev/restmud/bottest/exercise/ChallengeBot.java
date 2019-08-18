package uk.co.compendiumdev.restmud.bottest.exercise;

import uk.co.compendiumdev.restmud.bottest.RestMudTestBot;

public class ChallengeBot {
    public static RestMudTestBot create(final String rest_mud_url, final String your_desired_user_name, final String your_desired_password, final String registration_secret) {
        RestMudTestBot myFirstBot = new RestMudTestBot(rest_mud_url).setUserName(your_desired_user_name);

        myFirstBot.needsToRegisterOnSystem(true);
        myFirstBot.setRegistrationSecretCode(registration_secret);
        myFirstBot.setPassword(your_desired_password);

        myFirstBot.register();
        myFirstBot.login();

        myFirstBot.api().addRateLimitingTo(500);

        return myFirstBot;

    }
}
