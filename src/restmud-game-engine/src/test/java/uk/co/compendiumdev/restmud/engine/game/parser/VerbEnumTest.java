package uk.co.compendiumdev.restmud.engine.game.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbEnumTest {

    @Test
    public void checkBuiltInVerbEnumsExist(){

        Assert.assertTrue(DefaultVerb.DARKEN.toString().contentEquals("darken"));
        Assert.assertEquals(1, DefaultVerb.DARKEN.getTokenValue());
        Assert.assertTrue(DefaultVerb.DROP.toString().contentEquals("drop"));
        Assert.assertTrue(DefaultVerb.EXAMINE.toString().contentEquals("examine"));
        Assert.assertTrue(DefaultVerb.HOARD.toString().contentEquals("hoard"));
        Assert.assertTrue(DefaultVerb.ILLUMINATE.toString().contentEquals("illuminate"));
        Assert.assertTrue(DefaultVerb.INSPECT.toString().contentEquals("inspect"));
        Assert.assertTrue(DefaultVerb.OPEN.toString().contentEquals("open"));
        Assert.assertTrue(DefaultVerb.CLOSE.toString().contentEquals("close"));
        Assert.assertTrue(DefaultVerb.POLISH.toString().contentEquals("polish"));
        Assert.assertTrue(DefaultVerb.TAKE.toString().contentEquals("take"));
        Assert.assertTrue(DefaultVerb.USE.toString().contentEquals("use"));
        Assert.assertTrue(DefaultVerb.LOOK.toString().contentEquals("look"));
        Assert.assertTrue(DefaultVerb.GO.toString().contentEquals("go"));
        Assert.assertTrue(DefaultVerb.INVENTORY.toString().contentEquals("inventory"));
        Assert.assertTrue(DefaultVerb.MESSAGES.toString().contentEquals("messages"));
        Assert.assertTrue(DefaultVerb.SCORE.toString().contentEquals("score"));
        Assert.assertTrue(DefaultVerb.SCORES.toString().contentEquals("scores"));

        // catch any missing verbs
        Assert.assertEquals(17, DefaultVerb.SCORES.getTokenValue());

    }
}
