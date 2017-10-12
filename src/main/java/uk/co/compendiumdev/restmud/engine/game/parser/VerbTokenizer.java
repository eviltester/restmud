package uk.co.compendiumdev.restmud.engine.game.parser;


import java.util.HashMap;
import java.util.Map;

public class VerbTokenizer {


    private Map<String, Integer> tokenNameIndex;
    private Map<String, VerbToken> verbTokenNameIndex;

    public VerbTokenizer() {
        tokenNameIndex = new HashMap<>();
        verbTokenNameIndex = new HashMap<>();
    }

    // return the integer associated with the verb
    public Integer getTokenValue(String verbName) {
        return tokenNameIndex.get(verbName);
    }

    public int getTokenCount() {
        return tokenNameIndex.size();
    }

    public void addVerb(int token, String verbName) {
        this.tokenNameIndex.put(verbName, token);
        VerbToken vt = new VerbToken(token, verbName);
        this.verbTokenNameIndex.put(verbName, vt);
    }

    public VerbToken getToken(String verbName) {
        return verbTokenNameIndex.get(verbName);
    }
}
