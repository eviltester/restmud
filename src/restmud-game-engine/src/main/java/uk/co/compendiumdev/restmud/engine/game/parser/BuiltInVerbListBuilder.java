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

        verbList.registerVerb(DefaultVerb.DARKEN);
        verbList.registerVerb(DefaultVerb.DROP);
        verbList.registerVerb(DefaultVerb.EXAMINE);
        verbList.registerVerb(DefaultVerb.HOARD);
        verbList.registerVerb(DefaultVerb.ILLUMINATE);
        verbList.registerVerb(DefaultVerb.INSPECT);
        verbList.registerVerb(DefaultVerb.OPEN);
        verbList.registerVerb(DefaultVerb.CLOSE);
        verbList.registerVerb(DefaultVerb.POLISH);
        verbList.registerVerb(DefaultVerb.TAKE);
        verbList.registerVerb(DefaultVerb.USE);
        verbList.registerVerb(DefaultVerb.LOOK);
        verbList.registerVerb(DefaultVerb.GO);
        verbList.registerVerb(DefaultVerb.INVENTORY);
        verbList.registerVerb(DefaultVerb.MESSAGES);
        verbList.registerVerb(DefaultVerb.SCORE);
        verbList.registerVerb(DefaultVerb.SCORES);

        return this;

    }

    public VerbList getVerbList() {
        return verbList;
    }
}
