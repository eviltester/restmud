package uk.co.compendiumdev.restmud.engine.game;

public class VerbRegexToVerbMatch {
    public final String verbRegex;
    public final String matchingVerb;

    public VerbRegexToVerbMatch(String regex, String verb){
        this.verbRegex = regex;
        this.matchingVerb = verb;
    }


    public VerbRegexToVerbMatch getClonedCopy() {
        return new VerbRegexToVerbMatch(this.verbRegex, this.matchingVerb);
    }
}
