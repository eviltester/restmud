package uk.co.compendiumdev.restmud.engine.game.parser;


import org.junit.Assert;
import org.junit.Test;

public class VerbListTest {

    @Test
    public void canMaintainAListOfVerbs(){

        VerbList vl = new VerbList(null);

        Assert.assertEquals(0, vl.numberOfVerbs());

        vl.registerVerb(Verb.DARKEN.getName());

        Assert.assertEquals(1, vl.numberOfVerbs());
    }


    @Test
    public void canRegisterABuiltInVerb(){

        VerbList vl = new VerbList(null);
        Assert.assertEquals(0, vl.numberOfVerbs());
        vl.registerVerb(Verb.DARKEN);
        Assert.assertEquals(1, vl.numberOfVerbs());
    }
}
