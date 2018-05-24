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
public class MadHoarderStrategy implements RestMudBotStrategy {

    private RestMudAPI api;
    private RestMudBotWaitingStrategy waitingStrategy;
    private List neverHoard;

    @Override
    public void setApi(RestMudAPI api) {
        this.api = api;
        // remember things I cannot hoard
        this.neverHoard = new ArrayList<String>();
    }

    @Override
    public RestMudResponseProcessor execute() {
        System.out.println("Hoard all the things");

        RestMudResponseProcessor response = api.look();
        if(response.isSuccessful()) {
            if(response.result.look.treasureHoard!=null) {

                response = api.inventory();
                if(response.isSuccessful()) {
                    if(response.result.inventory!=null) {
                        for (IdDescriptionPair desc : response.result.inventory.contents) {
                            if(!neverHoard.contains(desc.id)) {
                                response = api.hoard(desc.id);
                                if (!response.isSuccessful()) {
                                    if (response.getData().contains("can not be hoarded")) {
                                        // add to never hoard again list
                                        // remember things I cannot hoard
                                        neverHoard.add(desc.id);
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
            }
        }

        return response;
    }

    public RestMudBotStrategy setWaitingStrategy(RestMudBotWaitingStrategy strategy){
        this.waitingStrategy = strategy;
        return this;
    }
}
