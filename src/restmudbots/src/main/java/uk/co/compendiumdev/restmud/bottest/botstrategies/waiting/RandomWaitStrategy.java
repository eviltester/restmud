package uk.co.compendiumdev.restmud.bottest.botstrategies.waiting;

import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotWaitingStrategy;

import java.util.Random;

public class RandomWaitStrategy implements RestMudBotWaitingStrategy {
    private int minWait;
    private int maxWait;

    public RestMudBotWaitingStrategy waitTimeBetween(int minWait, int maxWait) {
        this.minWait = minWait;
        this.maxWait = maxWait;
        return this;
    }


    @Override
    public void execute() throws InterruptedException {
        int waitTime = (new Random().nextInt(maxWait - minWait) + 1) + minWait;
        System.out.println("Waiting " + waitTime);
        Thread.sleep(waitTime);
    }
}
