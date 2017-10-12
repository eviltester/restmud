package uk.co.compendiumdev.restmud.engine.game.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Alan on 09/08/2016.
 */
public class UserInputParserTest {

    @Test
    public void canTokeniseABuildInVerb(){

        VerbList vl = new VerbList(null);

        vl.registerVerb("take");

        VerbTokenizer vt = vl.getTokenizer();

        vt.addVerb(1, "take");

        UserInputParser parser = new UserInputParser(vl);

        VerbToken t = parser.getVerbToken("take");

        Assert.assertTrue(t.getName().contentEquals("take"));
    }

}
