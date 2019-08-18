package uk.co.compendiumdev.restmud.bottest.botstrategies.waiting;

import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotWaitingStrategy;

/**
 * Created by Alan on 25/01/2017.
 */
public class FixedWaitStrategy implements RestMudBotWaitingStrategy {
    private int timeInMillis;

    public RestMudBotWaitingStrategy waitTime(int timeInMillis) {
        this.timeInMillis = timeInMillis;
        return this;
    }


    @Override
    public void execute() throws InterruptedException {
            System.out.println("Waiting " + timeInMillis);
            Thread.sleep(timeInMillis);
    }
}
