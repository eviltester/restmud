package uk.co.compendiumdev.restmud.gamedata;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Alan on 14/03/2016.
 */
public class RandomStringGeneratorTest {

    @Test
    public void canGenerateARandomString(){

        RandomStringGenerator rndString = new RandomStringGenerator();

        String generated = rndString.generateAlpha(6);
        System.out.println(generated);
        Assert.assertEquals(6, generated.length());
    }
}
