package uk.co.compendiumdev.restmud.engine.game.scripting;

public class AttributeParser {

    private final String [] parts;


    public AttributeParser(String thensValue) {

        parts = thensValue.split(":");
        int id = 0;
        for(String part : parts){
            parts[id] = part.trim();
            id++;
        }
    }

    public String getPart(int id){
        return parts[id];
    }
}
