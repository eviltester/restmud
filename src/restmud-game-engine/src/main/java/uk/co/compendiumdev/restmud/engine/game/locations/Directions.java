package uk.co.compendiumdev.restmud.engine.game.locations;

public class Directions {

    private static final String southSynonyms = "|s|south|";
    private static final String northSynonyms = "|n|north|";
    private static final String westSynonyms = "|w|west|";
    private static final String eastSynonyms = "|e|east|";


    public static String findBaseDirection(String lcDirection) {

        String dir = lcDirection.toLowerCase();

        if (southSynonyms.contains(String.format("|%s|", dir))) {
            return "s";
        }
        if (northSynonyms.contains(String.format("|%s|", dir))) {
            return "n";
        }
        if (westSynonyms.contains(String.format("|%s|", dir))) {
            return "w";
        }
        if (eastSynonyms.contains(String.format("|%s|", dir))) {
            return "e";
        }

        return "";
    }

    public static String findOppositeDirection(String lcDirection) {

        String dir = findBaseDirection(lcDirection.toLowerCase());

        switch(dir){
            case "s":
                return "n";
            case "e":
                return "w";
            case "n":
                return "s";
            case "w":
                return "e";
        }

        return "";
    }

}
