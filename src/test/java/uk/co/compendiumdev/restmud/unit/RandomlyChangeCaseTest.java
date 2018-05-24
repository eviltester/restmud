package uk.co.compendiumdev.restmud.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restmud.api.ChangeCaseifier;

/**
 * Created by Alan on 08/02/2017.
 */
public class RandomlyChangeCaseTest {

    @Test
    public void canChangeCaseOfALetterToUppercase(){

        ChangeCaseifier changeCase = new ChangeCaseifier();

        String testString = "bob";

        Assert.assertEquals("Bob", changeCase.upperCaseOf(testString,0));
        Assert.assertEquals("bOb", changeCase.upperCaseOf(testString,1));
        Assert.assertEquals("boB", changeCase.upperCaseOf(testString,2));
    }

    @Test
    public void canChangeCaseOfALetterToLowercase(){

        ChangeCaseifier changeCase = new ChangeCaseifier();

        String testString = "BOB";

        Assert.assertEquals("bOB", changeCase.lowerCaseOf(testString,0));
        Assert.assertEquals("BoB", changeCase.lowerCaseOf(testString,1));
        Assert.assertEquals("BOb", changeCase.lowerCaseOf(testString,2));
    }

    @Test
    public void canReverseCaseOfALetter(){

        ChangeCaseifier changeCase = new ChangeCaseifier();

        String testString = "BoB";

        Assert.assertEquals("boB", changeCase.reverseCaseOf(testString,0));
        Assert.assertEquals("BOB", changeCase.reverseCaseOf(testString,1));
        Assert.assertEquals("Bob", changeCase.reverseCaseOf(testString,2));
    }

    @Test
    public void canSelectARandomLetterPosition(){

        ChangeCaseifier changeCase = new ChangeCaseifier();

        String testString = "AbC";

        for(int gen=0; gen<1000; gen++) {
            int position = changeCase.selectRandomLetterPosition(testString);
            System.out.println(position);
            Assert.assertTrue(String.format("Expected 0 <= [%d] <= 2", position), position >= 0 && position <= 2);
        }
    }

    @Test
    public void randomlyMutateCaseOfWord(){

        ChangeCaseifier changeCase = new ChangeCaseifier();

        String testString = "AbCdEfGhIjKlMnOpQrStUvWxYz";

        for(int gen=0; gen<1000; gen++) {
            System.out.println(changeCase.randomlyChangeCaseOf(testString));
        }
    }



}
