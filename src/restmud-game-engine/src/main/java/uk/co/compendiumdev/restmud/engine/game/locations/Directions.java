package uk.co.compendiumdev.restmud.engine.game.locations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Directions {

    List<String> directions;
    Map<String, List<String>> synonyms;  //"s" south, southward, southwards
    Map<String, String> opposites; // base direction opposites

    // by default have N,S,E,W and if we need: up, down, in, out, se, sw, ne, nw - can do this in game definition

    public Directions(){
        directions = new ArrayList<>();
        synonyms = new HashMap<>();
        opposites = new HashMap<>();

        addDirection("s").
                synonymFor("s", "south").
                synonymFor("s", "southward").
                synonymFor("s", "southernly").
                synonymFor("s", "southwards");

        addDirection("n").
                synonymFor("n", "north").
                synonymFor("n", "northward").
                synonymFor("n", "northernly").
                synonymFor("n", "northwards");

        addDirection("e").
                synonymFor("e", "east").
                synonymFor("e", "eastward").
                synonymFor("e", "easternly").
                synonymFor("e", "eastwards");

        addDirection("w").
                synonymFor("w", "west").
                synonymFor("w", "westward").
                synonymFor("w", "westernly").
                synonymFor("w", "westwards");

        oppositeFor("s","n");
        oppositeFor("e","w");
    }

    public Directions(final ArrayList<String> baseDirs, final Map<String, List<String>> synonymDirs, final Map<String, String> oppositeDirs) {
        directions = baseDirs;
        synonyms = synonymDirs;
        opposites = oppositeDirs;
    }

    private Directions oppositeFor(final String dir1, final String dir2) {
        String dira = formatDirectionText(dir1);
        String dirb = formatDirectionText(dir2);

        if(!opposites.containsKey(dira)){
            System.out.println(String.format("Could not find base direction %s", dira));
            return this;
        }

        if(!opposites.containsKey(dirb)){
            System.out.println(String.format("Could not find base direction %s", dirb));
            return this;
        }

        opposites.put(dira, dirb);
        opposites.put(dirb, dira);

        return this;
    }

    private Directions synonymFor(final String baseDirection, final String synonym) {

        String key = formatDirectionText(baseDirection);
        String value = formatDirectionText(synonym);

        if(!synonyms.containsKey(key)){
            System.out.println(String.format("base direction %s does not exist",key));
            return this;
        }

        if(value.length()==0){
            System.out.println(String.format("empty value for synonym of %s", key));
            return this;
        }

        if(synonyms.get(key).contains(value)){
            System.out.println(String.format("ignore duplicate synonym %s of %s", value, key));
            return this;
        }

        synonyms.get(key).add(value);

        return this;
    }

    private Directions addDirection(final String aDirection) {

        String theDirection = formatDirectionText(aDirection);

        if(theDirection.length()==0){
            System.out.println(String.format("could not add direction %s",aDirection));
            return this;
        }

        if(directions.contains(theDirection)){
            System.out.println(String.format("Ignored duplicate addition of direction %s",theDirection));
            return this;
        }

        directions.add(theDirection);
        synonyms.put(theDirection, new ArrayList<String>());
        opposites.put(theDirection,"");

        return this;
    }

    private String formatDirectionText(final String aDirection) {
        if(aDirection==null){
            return "";
        }

        return aDirection.trim().toLowerCase().replace(" ","");
    }

    public String findBaseDirection(String lcDirection) {

        String dir = formatDirectionText(lcDirection);

        if(dir.length()==0){
            return "";
        }

        if(directions.contains(dir)){
            return dir;
        }

        for(String baseDirection : synonyms.keySet()){
            final List<String> values = synonyms.get(baseDirection);
            for(String synonym : values){
                if(synonym.equals(dir)){
                    return baseDirection;
                }
            }
        }

        return "";

    }

    public String findOppositeDirection(String lcDirection) {

        String dir = findBaseDirection(lcDirection);

        if(opposites.keySet().contains(dir)){
            return opposites.get(dir);
        }

        return "";
    }

    public Directions createClonedCopy() {

        Map<String, List<String>> newSynonyms = new HashMap<>();
        Map<String, String> newOpposites = new HashMap<>();

        for(String key : synonyms.keySet()){
            newSynonyms.put(key, new ArrayList<>(synonyms.get(key)));
        }

        newOpposites.putAll(opposites);

        for(String key : synonyms.keySet()){
            newSynonyms.put(key, new ArrayList<>(synonyms.get(key)));
        }

        return new Directions(new ArrayList<String>(directions), newSynonyms, newOpposites);
    }
}
