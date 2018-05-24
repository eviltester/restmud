package uk.co.compendiumdev.restmud.engine.game.parser;

import org.junit.Assert;
import org.junit.Test;


public class VerbTokenizerTest {


    @Test
    public void canAddVerbToTokenizer(){

        VerbTokenizer tokenizer = new VerbTokenizer();
        Assert.assertEquals(0,tokenizer.getTokenCount());

        tokenizer.addVerb(1, Verb.DARKEN.getName());
        Assert.assertEquals(1,tokenizer.getTokenCount());

        Integer t = tokenizer.getTokenValue("darken");
        Assert.assertEquals(1,t.intValue());

        VerbToken vt = tokenizer.getToken("darken");
        Assert.assertEquals(1,vt.getValue());
        Assert.assertEquals("darken", vt.getName());



    }

    @Test
    public void nullIsReturnedWhenVerbDoesNotExist(){
        VerbTokenizer tokenizer = new VerbTokenizer();

        Assert.assertEquals(null,tokenizer.getTokenValue("noverbwiththisname"));

        Assert.assertEquals(null,tokenizer.getToken("noverbwiththisname"));
    }
}
