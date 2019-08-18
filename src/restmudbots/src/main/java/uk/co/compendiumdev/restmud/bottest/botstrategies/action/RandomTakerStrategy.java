package uk.co.compendiumdev.restmud.bottest.botstrategies.action;

import uk.co.compendiumdev.restmud.api.RestMudAPI;
import uk.co.compendiumdev.restmud.api.RestMudResponseProcessor;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotStrategy;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotWaitingStrategy;
import uk.co.compendiumdev.restmud.output.json.IdDescriptionPair;

import java.util.Random;

public class RandomTakerStrategy implements RestMudBotStrategy {
    private RestMudAPI api;

    @Override
    public void setApi(RestMudAPI api) {
        this.api = api;
    }

    @Override
    public RestMudResponseProcessor execute() {

        System.out.println("TAKE RANDOM THING");

        // look then take what I see
        RestMudResponseProcessor response = api.look();
        if(response.isSuccessful()) {

            // choose a random thing to take
            IdDescriptionPair[] collectables = response.result.look.collectables;
            if (collectables != null) {
                if (collectables.length > 0) {
                    int chosenCollectable = new Random().nextInt(collectables.length);
                    response = api.take(collectables[chosenCollectable].id);
                }
            }
        }

        return response;
    }

    public RandomTakerStrategy setWaitingStrategy(RestMudBotWaitingStrategy waitingStrategy) {
        return this;
    }
}
