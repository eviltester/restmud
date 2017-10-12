package uk.co.compendiumdev.restmud.engine.game.verbmodes;

import uk.co.compendiumdev.restmud.engine.game.VerbRegexToVerbMatch;

import java.util.ArrayList;
import java.util.List;

public class VerbMode {

    private final String name;
    private List<VerbRegexToVerbMatch> guiMatchers;
    private List<VerbRegexToVerbMatch> apiGetMatchers;
    private List<VerbRegexToVerbMatch> apiPostMatchers;

    public VerbMode(String aName) {
        this.name = aName;
        guiMatchers = new ArrayList<>();
        apiGetMatchers = new ArrayList<>();
        apiPostMatchers = new ArrayList<>();
    }

    public void addGUIVerbMapping(String pathRegEx, String verbName) {
        guiMatchers.add(new VerbRegexToVerbMatch(pathRegEx, verbName));
    }

    public void addApiGetVerbMapping(String pathRegEx, String verbName) {
        apiGetMatchers.add(new VerbRegexToVerbMatch(pathRegEx, verbName));
    }

    public void addApiPostVerbMapping(String verb, String verbName) {
        apiPostMatchers.add(new VerbRegexToVerbMatch(verb, verbName));
    }

    public String getName() {
        return name;
    }

    public List<VerbRegexToVerbMatch> getGuiMatchers() {
        return guiMatchers;
    }

    public List<VerbRegexToVerbMatch> getApiGetVerbMatchers() {
        return apiGetMatchers;
    }

    public List<VerbRegexToVerbMatch> getApiPostVerbMatchers() {
        return apiPostMatchers;
    }
}
