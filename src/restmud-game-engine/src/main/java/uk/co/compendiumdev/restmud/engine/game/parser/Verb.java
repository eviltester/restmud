package uk.co.compendiumdev.restmud.engine.game.parser;

import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.VerbHandler;

public class Verb {

    private int tokenValue;
    private String name;
    private VerbHandler handler;
    private Verb synonymOf;


    public Verb(final int tokenValue, final String verbName, final VerbHandler handler) {
        this.tokenValue = tokenValue;
        this.name = verbName;
        this.handler = handler;
        synonymOf = null;
    }

    public boolean isSynonym(){
        return synonymOf!=null;
    }

}
