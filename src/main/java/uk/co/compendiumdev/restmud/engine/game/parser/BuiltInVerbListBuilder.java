package uk.co.compendiumdev.restmud.engine.game.parser;

import uk.co.compendiumdev.restmud.engine.game.MudGame;

/**
 * Created by Alan on 09/08/2016.
 */
public class BuiltInVerbListBuilder {

    private final VerbList verbList;

    public BuiltInVerbListBuilder(MudGame game){
        verbList = new VerbList(game);
    }

    public int numberOfVerbs() {
        return verbList.numberOfVerbs();
    }

    public BuiltInVerbListBuilder addBuiltInVerbs() {

        verbList.registerVerb(Verb.DARKEN);
        verbList.registerVerb(Verb.DROP);
        verbList.registerVerb(Verb.EXAMINE);
        verbList.registerVerb(Verb.HOARD);
        verbList.registerVerb(Verb.ILLUMINATE);
        verbList.registerVerb(Verb.INSPECT);
        verbList.registerVerb(Verb.OPEN);
        verbList.registerVerb(Verb.CLOSE);
        verbList.registerVerb(Verb.POLISH);
        verbList.registerVerb(Verb.TAKE);
        verbList.registerVerb(Verb.USE);
        verbList.registerVerb(Verb.LOOK);
        verbList.registerVerb(Verb.GO);
        verbList.registerVerb(Verb.INVENTORY);
        verbList.registerVerb(Verb.MESSAGES);
        verbList.registerVerb(Verb.SCORE);
        verbList.registerVerb(Verb.SCORES);

        return this;

    }

    public VerbList getVerbList() {
        return verbList;
    }
}
