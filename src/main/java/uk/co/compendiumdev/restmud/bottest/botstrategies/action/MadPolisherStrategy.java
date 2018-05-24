package uk.co.compendiumdev.restmud.bottest.botstrategies.action;

import uk.co.compendiumdev.restmud.api.RestMudAPI;
import uk.co.compendiumdev.restmud.api.RestMudResponseProcessor;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotStrategy;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotWaitingStrategy;
import uk.co.compendiumdev.restmud.output.json.IdDescriptionPair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 06/02/2017.
 */
public class MadPolisherStrategy implements RestMudBotStrategy {

    private RestMudAPI api;
    private RestMudBotWaitingStrategy waitingStrategy;
    private List<String> neverPolish;


    @Override
    public void setApi(RestMudAPI api) {
        this.api = api;
        // remember things it would not be worth polishing
        this.neverPolish = new ArrayList<String>();
    }

    @Override
    public RestMudResponseProcessor execute() {
        System.out.println("Polish all the things");

        RestMudResponseProcessor response = api.inventory();
        if(response.isSuccessful()) {
            if(response.result.inventory!=null) {
                for (IdDescriptionPair desc : response.result.inventory.contents) {
                    if(!neverPolish.contains(desc.id)) {
                        response = api.polish(desc.id);
                        if (!response.isSuccessful()) {
                            if (response.getData().contains("try to polish it with your hand")) {
                                break;
                            }
                        }
                        if (!response.isSuccessful()) {
                            if (response.getData().contains("not be worthwhile polishing that")) {
                                // remember things I should never waste time trying to polish
                                neverPolish.add(desc.id);
                            }
                        }
                        // then wait
                        if (this.waitingStrategy != null) {
                            try {
                                waitingStrategy.execute();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        return response;
    }

    public RestMudBotStrategy setWaitingStrategy(RestMudBotWaitingStrategy strategy){
        this.waitingStrategy = strategy;
        return this;
    }
}
