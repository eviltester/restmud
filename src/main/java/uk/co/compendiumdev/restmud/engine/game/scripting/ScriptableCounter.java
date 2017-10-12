package uk.co.compendiumdev.restmud.engine.game.scripting;

/**
 * Created by Alan on 07/06/2016.
 */
public class ScriptableCounter {
    public String name;
    private int value;

    public ScriptableCounter(String thensValue) {
        setFrom(thensValue);
    }

    public int getValue(){
        return value;
    }

    public static ScriptableCounter empty() {
        return new ScriptableCounter(" :");
    }

    public void setFrom(String thensValue) {
        String[] parts = thensValue.split(":");
        this.name = parts[0].toLowerCase();
        this.value = 0;

        try{
            if(parts.length>1){
                this.value = Integer.parseInt(parts[1]);
            }
        }catch(NumberFormatException e){
            // ignore, the default is 0 anyway
        }
    }
}
