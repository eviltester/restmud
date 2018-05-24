package uk.co.compendiumdev.restmud.bottest;


import java.util.Random;

public class Directions {

    public static final String[] directions = {"e","w","s","n"};


    public static String getRandomDirection() {
        int chosenDirection = new Random().nextInt(directions.length);
        return directions[chosenDirection];
    }
}
