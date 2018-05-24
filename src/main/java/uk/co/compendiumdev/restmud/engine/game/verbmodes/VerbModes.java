package uk.co.compendiumdev.restmud.engine.game.verbmodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerbModes {

    private final Map<String, VerbMode> verbModes = new HashMap<>();

    public VerbMode get(String verbModeName) {
        return verbModes.get(verbModeName);
    }

    public void add(VerbMode verbMode) {
        verbModes.put(verbMode.getName(), verbMode );
    }

    public List<String> getNames(){
        List<String> names = new ArrayList<>();
        names.addAll(verbModes.keySet());
        return names;
    }

    public List<VerbMode> getVerbModes(){
        List<VerbMode> modes = new ArrayList<>();

        modes.addAll(verbModes.values());
        return modes;
    }
}
