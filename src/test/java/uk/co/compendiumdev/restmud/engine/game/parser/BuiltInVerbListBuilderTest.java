package uk.co.compendiumdev.restmud.engine.game.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Alan on 09/08/2016.
 */
public class BuiltInVerbListBuilderTest {

    @Test
    public void canCreateAListOfTokenizedBuiltInVerbs(){

        BuiltInVerbListBuilder bl = new BuiltInVerbListBuilder(null);

        Assert.assertEquals(0, bl.numberOfVerbs());

        bl.addBuiltInVerbs();

        Assert.assertEquals(17, bl.numberOfVerbs());

        VerbList vl = bl.getVerbList();

        Assert.assertEquals(17, vl.numberOfVerbs());

        VerbTokenizer tokenizer = vl.getTokenizer();

        VerbToken score = tokenizer.getToken("score");

        Assert.assertEquals("score", score.getName());


        VerbToken take = tokenizer.getToken("take");

        Assert.assertTrue(score.getValue()!=0);
        Assert.assertTrue(take.getValue()!=0);
        Assert.assertTrue(score.getValue()!=take.getValue());
    }

    @Test
    public void canAddNewVerbsAfterBuiltInVerbs(){

        BuiltInVerbListBuilder bl = new BuiltInVerbListBuilder(null);
        bl.addBuiltInVerbs();
        VerbList vl = bl.getVerbList();

        int expectedTokenVal = vl.getNextTokenId();
        vl.registerVerb("eviserate");

        VerbToken t = vl.getTokenizer().getToken("eviserate");
        Assert.assertTrue(t.getValue()==expectedTokenVal);
        Assert.assertTrue(t.getName().contentEquals("eviserate"));
        Assert.assertEquals(expectedTokenVal+1,vl.getNextTokenId());

    }

}
