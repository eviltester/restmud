package uk.co.compendiumdev.restmud.bottest.botstrategies.action;

import uk.co.compendiumdev.restmud.api.RestMudAPI;
import uk.co.compendiumdev.restmud.api.RestMudResponseProcessor;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotStrategy;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotWaitingStrategy;
import uk.co.compendiumdev.restmud.output.json.VisibleGate;

public class AllDoorOpenerStrategy implements RestMudBotStrategy {
    private RestMudAPI api;
    private RestMudBotWaitingStrategy waitingStrategy;

    @Override
    public void setApi(RestMudAPI api) {
        this.api = api;
    }

    @Override
    public RestMudResponseProcessor execute() {

        System.out.println("OPEN ALL DOORS");

        RestMudResponseProcessor response = api.look();
        if(response.isSuccessful()) {
            if(response.result.look.visibleGates!=null) {
                for (VisibleGate gate : response.result.look.visibleGates) {
                    if (!gate.open) {
                        // open it
                        api.open(gate.direction);

                        // then wait
                        if(this.waitingStrategy != null){
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
