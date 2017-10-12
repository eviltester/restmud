package uk.co.compendiumdev.restmud.engine.game.things;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alan on 29/03/2016.
 */
public class IdGenerator {

    Map<String, Integer> theIds = new HashMap<>();

    public String generateId(String thingName) {

        if(!theIds.containsKey(thingName)){
            theIds.put(thingName,0);
        }


        Integer count = theIds.get(thingName);
        int id = ++count;
        theIds.put(thingName,id);

        return String.format("%s_%d",thingName,id);
    }

    public void setFrom(IdGenerator from) {
        theIds.putAll(from.theIds);
    }
}
