package uk.co.compendiumdev.restmud.bottest.botstrategies.action;

import uk.co.compendiumdev.restmud.api.RestMudAPI;
import uk.co.compendiumdev.restmud.api.RestMudResponseProcessor;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotStrategy;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotWaitingStrategy;
import uk.co.compendiumdev.restmud.output.json.VisibleGate;

import java.util.Random;

/**
 * Created by Alan on 25/01/2017.
 */
public class RandomDoorOpenerStrategy implements RestMudBotStrategy {
    private RestMudAPI api;

    @Override
    public void setApi(RestMudAPI api) {
        this.api = api;
    }

    @Override
    public RestMudResponseProcessor execute() {

        System.out.println("OPEN RANDOM DOOR");

        RestMudResponseProcessor response = api.look();
        if(response.isSuccessful()) {
            if(response.result.look.visibleGates!=null) {
                if(response.result.look.visibleGates.size()>0){
                    int chosenGate = new Random().nextInt(response.result.look.visibleGates.size());
                    VisibleGate gate = response.result.look.visibleGates.get(chosenGate);
                    if (!gate.open) {
                        // open it
                        response = api.open(gate.direction);
                    }
                }
            }
        }

        return response;
    }

    public RestMudBotStrategy setWaitingStrategy(RestMudBotWaitingStrategy strategy){
        return this;
    }
}
