package uk.co.compendiumdev.restmud.engine.game.scripting;

public class AttributePair {

    public final String name;
    public final String value;

    
    public AttributePair(String thensValue) {

        String[] parts = thensValue.split(":");
        this.name = parts[0];
        if(parts.length>1){
            this.value =parts[1];
        }else{
            this.value = "";
        }
    }
}
