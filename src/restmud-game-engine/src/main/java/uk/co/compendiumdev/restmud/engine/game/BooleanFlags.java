package uk.co.compendiumdev.restmud.engine.game;

import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptableFlag;

import java.util.HashMap;
import java.util.Map;

public class BooleanFlags {

    private Map<String, Boolean> flags;

    public BooleanFlags(){
        this.flags = new HashMap<String, Boolean>();
    }

    public boolean flagExists(String matchThis) {
        return flags.containsKey(matchThis);
    }

    public boolean getFlagValue(String matchThis) {
        Boolean flagValue = flags.get(matchThis);
        if(flagValue==null){
            return false;
        }

        return flagValue.booleanValue();
    }

    public void setFlag(ScriptableFlag userFlag) {
        flags.put(userFlag.name, userFlag.getValue());
    }

    public void deleteFlag(String name) {
        flags.remove(name);
    }
}
