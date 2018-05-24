package uk.co.compendiumdev.restmud.engine.game.scripting;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Alan on 01/09/2016.
 */
public class ScriptableCounterConditionTest {


    @Test
    public void byDefaultAnEmptyConditionIsEqualZero(){

        int counter=0;

        ScriptableCounterCondition condition = ScriptableCounterCondition.empty();

        Assert.assertTrue(condition.comparedTo(counter));
    }

    @Test
    public void counterConditionEqualAssertions(){

        int counter=0;

        // just a name: == 0
        ScriptableCounterCondition condition = new ScriptableCounterCondition("counter");
        Assert.assertTrue(condition.comparedTo(counter));

        condition = new ScriptableCounterCondition(" counter ");
        Assert.assertTrue(condition.comparedTo(counter));

        condition = new ScriptableCounterCondition(" counter : == ");
        Assert.assertTrue(condition.comparedTo(counter));

        condition = new ScriptableCounterCondition(" counter : == : 0");
        Assert.assertTrue(condition.comparedTo(counter));
        Assert.assertEquals(0, condition.getValue());
        Assert.assertEquals("==", condition.getCondition());

        condition = new ScriptableCounterCondition(" counter : == : 1");
        Assert.assertFalse(condition.comparedTo(counter));
        Assert.assertEquals(1, condition.getValue());
        Assert.assertEquals("==", condition.getCondition());

        condition = new ScriptableCounterCondition(" counter : = : 1");
        Assert.assertFalse(condition.comparedTo(counter));
        Assert.assertEquals(1, condition.getValue());
        Assert.assertEquals("=", condition.getCondition());

    }

    @Test
    public void counterConditionNotEqualAssertions(){

        ScriptableCounterCondition condition = new ScriptableCounterCondition(" counter : != : 0");
        Assert.assertFalse(condition.comparedTo(0));
        Assert.assertTrue(condition.comparedTo(1));
        Assert.assertEquals(0, condition.getValue());
        Assert.assertEquals("!=", condition.getCondition());

        condition = new ScriptableCounterCondition(" counter : != : 100");
        Assert.assertTrue("0 != 100", condition.comparedTo(0));
        Assert.assertTrue("10 != 100", condition.comparedTo(10));
        Assert.assertFalse("100 0= 100", condition.comparedTo(100));
        Assert.assertEquals(100, condition.getValue());
        Assert.assertEquals("!=", condition.getCondition());
    }

    @Test
    public void counterConditionGreaterLessThanAssertions(){

        ScriptableCounterCondition condition = new ScriptableCounterCondition(" counter : < : 50");
        Assert.assertTrue(condition.comparedTo(0));
        Assert.assertTrue(condition.comparedTo(49));
        Assert.assertFalse(condition.comparedTo(50));
        Assert.assertFalse(condition.comparedTo(51));
        Assert.assertEquals(50, condition.getValue());
        Assert.assertEquals("<", condition.getCondition());

        condition = new ScriptableCounterCondition(" counter : > : 100");
        Assert.assertTrue("101 > 100", condition.comparedTo(101));
        Assert.assertFalse("100 < 101", condition.comparedTo(100));
        Assert.assertEquals(100, condition.getValue());
        Assert.assertEquals(">", condition.getCondition());
    }

    @Test
    public void counterConditionGreaterThanEqualAssertions(){

        ScriptableCounterCondition condition = new ScriptableCounterCondition(" counter : >= : 50");
        Assert.assertTrue(condition.comparedTo(50));
        Assert.assertTrue(condition.comparedTo(51));
        Assert.assertTrue(condition.comparedTo(100));
        Assert.assertFalse(condition.comparedTo(49));
        Assert.assertEquals(50, condition.getValue());
        Assert.assertEquals(">=", condition.getCondition());

        condition = new ScriptableCounterCondition(" counter : => : 50");
        Assert.assertTrue(condition.comparedTo(50));
        Assert.assertTrue(condition.comparedTo(51));
        Assert.assertTrue(condition.comparedTo(100));
        Assert.assertFalse(condition.comparedTo(49));
        Assert.assertEquals(50, condition.getValue());
        Assert.assertEquals("=>", condition.getCondition());
    }

    @Test
    public void counterConditionLessThanEqualAssertions(){

        ScriptableCounterCondition condition = new ScriptableCounterCondition(" counter : <= : 50");
        Assert.assertTrue(condition.comparedTo(50));
        Assert.assertTrue(condition.comparedTo(49));
        Assert.assertTrue(condition.comparedTo(10));
        Assert.assertFalse(condition.comparedTo(51));
        Assert.assertEquals(50, condition.getValue());
        Assert.assertEquals("<=", condition.getCondition());

        condition = new ScriptableCounterCondition(" counter : =< : 50");
        Assert.assertTrue(condition.comparedTo(50));
        Assert.assertTrue(condition.comparedTo(49));
        Assert.assertTrue(condition.comparedTo(10));
        Assert.assertFalse(condition.comparedTo(51));
        Assert.assertEquals(50, condition.getValue());
        Assert.assertEquals("=<", condition.getCondition());
    }



}
