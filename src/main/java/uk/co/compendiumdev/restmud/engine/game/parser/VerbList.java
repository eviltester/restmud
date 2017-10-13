package uk.co.compendiumdev.restmud.engine.game.parser;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.DefaultVerbHandler;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.VerbHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerbList {


    private final ArrayList<String> verbNameList;
    private final Map<Integer, VerbHandler > handlerList;
    private final VerbTokenizer tokenizer;
    private final MudGame game;
    private int nextToken = 0;

    public VerbList(MudGame game){
        verbNameList = new ArrayList<String>();
        tokenizer = new VerbTokenizer();
        handlerList = new HashMap<>();
        this.game = game;
    }

    public int numberOfVerbs() {
        return verbNameList.size();
    }

    public void registerVerb(Verb verb) {

        verbNameList.add(verb.getName());
        tokenizer.addVerb(verb.getTokenValue(), verb.getName().toLowerCase());
        if(verb.getTokenValue()>nextToken){
            nextToken = verb.getTokenValue()+1;
        }
        try {
                VerbHandler handler = (VerbHandler) verb.getHandlerClass().newInstance();
                handler.setGame(game);
                handlerList.put(verb.getTokenValue(), handler);

        } catch (InstantiationException e) {
            throw new RuntimeException(String.format("Could not instantiate handler for %s", verb.getName()),e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Could not access class to create handler for %s", verb.getName()),e);
        }
    }

    public VerbTokenizer getTokenizer() {
        return tokenizer;
    }


    public int getNextTokenId() {
        return nextToken;
    }

    public void registerVerb(String verbName) {

        // only add once
        if(verbNameList.contains(verbName)){
            return;
        }

        verbNameList.add(verbName);
        int token = nextToken;
        tokenizer.addVerb(nextToken++, verbName);

        // custom verbs use a default verb handler that does nothing
        VerbHandler vh = new DefaultVerbHandler();
        vh.setGame(game);
        handlerList.put(token, vh);
    }

    public VerbHandler getHandler(String verbToHandle) {
        return handlerList.get(tokenizer.getTokenValue(verbToHandle));
    }
}
