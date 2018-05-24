package uk.co.compendiumdev.restmud.bottest.botstrategies.action;

import uk.co.compendiumdev.restmud.api.RestMudAPI;
import uk.co.compendiumdev.restmud.api.RestMudResponseProcessor;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotStrategy;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotWaitingStrategy;
import uk.co.compendiumdev.restmud.output.json.IdDescriptionPair;

import java.util.Random;

public class RandomExaminerStrategy implements RestMudBotStrategy {
    private RestMudAPI api;

    @Override
    public void setApi(RestMudAPI api) {
        this.api = api;
    }

    @Override
    public RestMudResponseProcessor execute() {

        System.out.println("EXAMINE RANDOM THING");

        // look then take what I see
        RestMudResponseProcessor response = api.look();
        if(response.isSuccessful()) {

            // choose a random thing to take
            IdDescriptionPair[] things = response.result.look.visibleObjects;
            if (things != null) {
                if (things.length > 0) {
                    int chosenThing = new Random().nextInt(things.length);
                    response = api.examine(things[chosenThing].id);
                }
            }
        }

        return response;
    }

    public RandomExaminerStrategy setWaitingStrategy(RestMudBotWaitingStrategy waitingStrategy) {
        return this;
    }
}
