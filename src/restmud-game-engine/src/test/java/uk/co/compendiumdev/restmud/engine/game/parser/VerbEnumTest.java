package uk.co.compendiumdev.restmud.engine.game.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbEnumTest {

    @Test
    public void checkBuiltInVerbEnumsExist(){

        Assert.assertTrue(Verb.DARKEN.toString().contentEquals("darken"));
        Assert.assertEquals(1,Verb.DARKEN.getTokenValue());
        Assert.assertTrue(Verb.DROP.toString().contentEquals("drop"));
        Assert.assertTrue(Verb.EXAMINE.toString().contentEquals("examine"));
        Assert.assertTrue(Verb.HOARD.toString().contentEquals("hoard"));
        Assert.assertTrue(Verb.ILLUMINATE.toString().contentEquals("illuminate"));
        Assert.assertTrue(Verb.INSPECT.toString().contentEquals("inspect"));
        Assert.assertTrue(Verb.OPEN.toString().contentEquals("open"));
        Assert.assertTrue(Verb.CLOSE.toString().contentEquals("close"));
        Assert.assertTrue(Verb.POLISH.toString().contentEquals("polish"));
        Assert.assertTrue(Verb.TAKE.toString().contentEquals("take"));
        Assert.assertTrue(Verb.USE.toString().contentEquals("use"));
        Assert.assertTrue(Verb.LOOK.toString().contentEquals("look"));
        Assert.assertTrue(Verb.GO.toString().contentEquals("go"));
        Assert.assertTrue(Verb.INVENTORY.toString().contentEquals("inventory"));
        Assert.assertTrue(Verb.MESSAGES.toString().contentEquals("messages"));
        Assert.assertTrue(Verb.SCORE.toString().contentEquals("score"));
        Assert.assertTrue(Verb.SCORES.toString().contentEquals("scores"));
        Assert.assertEquals(17,Verb.SCORES.getTokenValue());

    }
}
