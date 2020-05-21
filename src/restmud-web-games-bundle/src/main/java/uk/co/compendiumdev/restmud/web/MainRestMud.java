package uk.co.compendiumdev.restmud.web;

import spark.Spark;

public class MainRestMud {
    public static void main(String[] args){
        new RestMudApp().start(args);
        Spark.awaitStop();
    }
}
