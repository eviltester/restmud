package uk.co.compendiumdev.restmud.engine.game;


import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;

import java.util.HashMap;
import java.util.Map;

public class Collectables {

    private Map<String, MudCollectable> collectables;

    public Collectables(){
        collectables = new HashMap<>();
    }

    public Map<String, MudCollectable> getCollectables() {
        Map<String, MudCollectable> copyMap = new HashMap<String, MudCollectable>();
        copyMap.putAll(this.collectables);
        return copyMap;
    }

    public Map<String, MudCollectable> getClonedCopiedCollectables() {
        Map<String, MudCollectable> copyMap = new HashMap<String, MudCollectable>();
        for(MudCollectable collectable : collectables.values()){
            copyMap.put(collectable.getCollectableId().toLowerCase(), collectable.createClonedCopy());
        }
        return copyMap;
    }


    public void add(MudCollectable collectable) {
        collectables.put(collectable.getCollectableId().toLowerCase(), collectable);
    }

    public MudCollectable get(String nounPhrase) {
        return collectables.get(nounPhrase);
    }

    public int count() {
        return collectables.size();
    }

    public void setFrom(Map<String,MudCollectable> from) {
        this.collectables.putAll(from);
    }
}
