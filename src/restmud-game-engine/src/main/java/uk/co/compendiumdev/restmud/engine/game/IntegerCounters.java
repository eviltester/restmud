package uk.co.compendiumdev.restmud.engine.game;

import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptableCounter;

import java.util.HashMap;
import java.util.Map;

public class IntegerCounters {

    private Map<String, Integer> counters;

    public IntegerCounters(){
        this.counters = new HashMap<String, Integer>();
    }

    public void setCounter(ScriptableCounter counter) {
        counters.put(counter.name, counter.getValue());
    }

    public void incrementCounter(String name, int value) {
        if(counterExists(name)){
            Integer count = counters.get(name);
            count = count.intValue() + value;
            counters.put(name, count);
        }
    }

    public boolean counterExists(String matchThis) {
        return counters.containsKey(matchThis);
    }

    public int getCounter(String name) {
        if(counterExists(name)) {
            return counters.get(name);
        }

        return 0; // by default
    }

    public void deleteCounter(String name) {
        counters.remove(name);
    }
}
