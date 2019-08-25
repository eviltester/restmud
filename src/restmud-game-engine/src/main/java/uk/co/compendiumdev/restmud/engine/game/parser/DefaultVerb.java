package uk.co.compendiumdev.restmud.engine.game.parser;

import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.*;

public enum DefaultVerb {





    DARKEN("darken",1, VerbDarkenHandler.class),
    DROP("drop",2, VerbDropHandler.class),
    EXAMINE("examine",3, VerbExamineHandler.class),
    HOARD("hoard",4, VerbHoardHandler.class),
    ILLUMINATE("illuminate",5, VerbIlluminateHandler.class),
    INSPECT("inspect",6, VerbInspectHandler.class),
    OPEN("open",7, VerbOpenHandler.class),
    CLOSE("close",8, VerbCloseHandler.class),
    POLISH("polish",9, VerbPolishHandler.class),
    TAKE("take",10, VerbTakeHandler.class),
    USE("use",11, VerbUseHandler.class),
    LOOK("look",12, VerbLookHandler.class),
    GO("go",13, VerbGoHandler.class),
    INVENTORY("inventory",14, VerbInventoryHandler.class),
    MESSAGES("messages",15, VerbMessagesHandler.class),
    SCORE("score",16, VerbScoreHandler.class),
    SCORES("scores",17, VerbScoresHandler.class)
    ;



    private final int tokenValue;
    private final String name;
    private final Class handlerClass;

    DefaultVerb(String name, int tokenValue, Class handlerClass) {
        this.name = name;
        this.tokenValue = tokenValue;
        this.handlerClass = handlerClass;
    }

    public String getName() {
        return this.name;
    }
    public int getTokenValue() {
        return this.tokenValue;
    }
    public Class getHandlerClass() {
        return this.handlerClass;
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
