package uk.co.compendiumdev.restmud.engine.game;

public class VerbRegexToVerbMatch {
    private final String verbRegex;
    private final String matchingVerb;

    public VerbRegexToVerbMatch(String regex, String verb){
        this.verbRegex = regex;
        this.matchingVerb = verb;
    }

    public String getVerbRegex(){
        return verbRegex;
    }

    public String getMatchingVerb(){
        return matchingVerb;
    }

    public VerbRegexToVerbMatch getClonedCopy() {
        return new VerbRegexToVerbMatch(this.verbRegex, this.matchingVerb);
    }
}
