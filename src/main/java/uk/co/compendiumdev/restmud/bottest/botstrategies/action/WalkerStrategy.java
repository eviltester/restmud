package uk.co.compendiumdev.restmud.bottest.botstrategies.action;

import uk.co.compendiumdev.restmud.api.RestMudAPI;
import uk.co.compendiumdev.restmud.api.RestMudResponseProcessor;
import uk.co.compendiumdev.restmud.bottest.Directions;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotStrategy;
import uk.co.compendiumdev.restmud.output.json.VisibleExit;
import uk.co.compendiumdev.restmud.output.json.VisibleGate;

import java.util.List;
import java.util.Random;

public class WalkerStrategy implements RestMudBotStrategy {
    private RestMudAPI api;
    private boolean canOpenDoors;

    public void setApi(RestMudAPI api) {
        this.api = api;
        canOpenDoors=true; // by default I canopen a door if it is closed
    }

    public RestMudResponseProcessor execute() {

        System.out.println("WALK ABOUT");

        // WALK ABOUT STRATEGY - LOOK, then GO in a random direction
        RestMudResponseProcessor response = api.look();
        if(response.isSuccessful()) {

            // choose a random direction and go
            List<VisibleExit> exits = response.result.look.exits;
            if (exits != null) {
                if (exits.size() > 0) {
                    int chosenExit = new Random().nextInt(exits.size());
                    String chosenDirection = exits.get(chosenExit).direction;

                    if(canOpenDoors) {
                        // check for gates before going there i.e. if it is closed then try to open it first
                        VisibleGate gate = response.getGateGoing(chosenDirection);
                        if (gate != null) {
                            if (!gate.open) {
                                api.open(chosenDirection);
                            }
                        }
                    }

                    response = api.go(chosenDirection);
                }else{
                    // there are no exits pick a random exit just in case there is a secret exit
                    response = api.go(Directions.getRandomDirection());
                }
            }else{
                // there are no exits
                // TODO: if it is dark and I can illuminate then switch the lights on
                // if it is in darkness then randomly pick a direction
                if(response.result.look.location.isInDarkness){
                    response = api.go(Directions.getRandomDirection());
                }
            }
        }

        return response;
    }

    public RestMudBotStrategy canOpenDoors(boolean canOpenDoors) {
        this.canOpenDoors = canOpenDoors;
        return this;
    }
}
