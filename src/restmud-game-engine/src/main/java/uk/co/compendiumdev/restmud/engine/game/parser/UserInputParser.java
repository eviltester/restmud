package uk.co.compendiumdev.restmud.engine.game.parser;

import uk.co.compendiumdev.restmud.engine.game.VerbRegexToVerbMatch;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.VerbHandler;

import java.util.List;

public class UserInputParser {

    private VerbList verbList;

    public UserInputParser(VerbList verbList){
        this.verbList = verbList;
    }

    public VerbToken getVerbToken(String verbToHandle) {
        return verbList.getToken(verbToHandle);
    }

    public VerbHandler getVerbHandler(String verbToHandle) {
        return verbList.getHandler(verbToHandle);
    }

    public void addVerb(String verbName) {
        verbList.registerVerb(verbName);
    }

    public void addVerbs(List<VerbRegexToVerbMatch> localVerbs) {
        for(VerbRegexToVerbMatch localVerb : localVerbs){
            verbList.registerVerb(localVerb.getMatchingVerb());
        }
    }

    public Verb getVerb(final String verbName) {
        return verbList.getVerb(verbName);
    }
}
