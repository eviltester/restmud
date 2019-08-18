package uk.co.compendiumdev.restmud.engine.game.parser;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbToken {
    private final int token;
    private final String verbName;

    public VerbToken(int token, String verbName) {
        this.token = token;
        this.verbName = verbName;
    }

    public String getName() {
        return this.verbName;
    }

    public int getValue() {
        return token;
    }
}

