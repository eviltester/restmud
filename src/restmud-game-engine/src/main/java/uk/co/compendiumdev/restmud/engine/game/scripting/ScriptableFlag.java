package uk.co.compendiumdev.restmud.engine.game.scripting;

/**
 * Created by Alan on 06/06/2016.
 */
public class ScriptableFlag {
    public String name;
    private boolean value;

    public ScriptableFlag(String thensValue) {
        setFrom(thensValue);
    }

    public boolean getValue(){
        return value;
    }

    public static ScriptableFlag empty(){
        return new ScriptableFlag(" :");
    }

    public void setFrom(String thensValue) {
        String[] parts = thensValue.split(":");
        this.name = parts[0].toLowerCase();
        if(parts.length>1){
            this.value = Boolean.parseBoolean(parts[1]);
        }else{
            this.value = true;
        }
    }
}
