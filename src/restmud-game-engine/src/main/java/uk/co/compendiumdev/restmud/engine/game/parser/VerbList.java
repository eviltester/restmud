package uk.co.compendiumdev.restmud.engine.game.parser;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.DefaultVerbHandler;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.VerbHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerbList {

    private final MudGame game;
    private int nextToken = 0;

    private final Map<Integer, Verb > verbsByToken;
    private final Map<String, Verb > verbsByName;

    public VerbList(MudGame game){
        this.game = game;

        verbsByToken = new HashMap<>();
        verbsByName = new HashMap<>();
    }

    public int numberOfVerbs() {
        return verbsByToken.keySet().size();
    }

    public void registerVerb(DefaultVerb verb) {

        try {
                VerbHandler handler = (VerbHandler) verb.getHandlerClass().getDeclaredConstructor().newInstance();
                handler.setGame(game);

                Verb newVerb = new Verb(verb.getTokenValue(), sanitisedVerbName(verb.getName()), handler);
                verbsByName.put(newVerb.getName(), newVerb);
                verbsByToken.put(newVerb.getTokenValue(), newVerb);

                if(newVerb.getTokenValue()>nextToken){
                    nextToken = newVerb.getTokenValue()+1;
                }

        } catch (InstantiationException e) {
            throw new RuntimeException(String.format("Could not instantiate handler for %s", verb.getName()),e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Could not access class to create handler for %s", verb.getName()),e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Could not create class to create handler for %s", verb.getName()),e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void registerVerb(String verbName) {

        String actualVerbName = sanitisedVerbName(verbName);

        // only add once
        if(verbsByName.keySet().contains(actualVerbName)){
            return;
        }

        // custom verbs use a default verb handler that does nothing
        VerbHandler vh = new DefaultVerbHandler();
        vh.setGame(game);

        Verb newVerb = new Verb(nextToken, actualVerbName, vh);
        verbsByName.put(newVerb.getName(), newVerb);
        verbsByToken.put(newVerb.getTokenValue(), newVerb);

        nextToken = nextToken+1;
    }

    public Verb getVerb(final String verbName) {
        Verb theVerb = verbsByName.get(sanitisedVerbName(verbName));
        return theVerb;
    }

    public VerbToken getToken(String verbName){
        Verb theVerb = getVerb(verbName);
        if(theVerb==null){
            return null;
        }

        return theVerb.getToken();
    }


    public int getNextTokenId() {
        return nextToken;
    }

    private String sanitisedVerbName(String verbName){
        if(verbName==null){
            return "";
        }

        return verbName.trim().toLowerCase();
    }

    public VerbHandler getHandler(String verbToHandle) {
        String actualVerbName = sanitisedVerbName(verbToHandle);

        if(verbsByName.keySet().contains(actualVerbName)){
            return verbsByName.get(actualVerbName).getHandler().usingCurrentVerb(actualVerbName);
        }

        return null;  // perhaps this should be the default handler?
    }


}
