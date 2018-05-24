package uk.co.compendiumdev.restmud.engine.game.locations;

import org.junit.Assert;
import org.junit.Test;

public class DirectionsTest {

    @Test
    public void baseDirectionValuesMapAsExpected(){

        Assert.assertEquals("s",Directions.findBaseDirection("s"));
        Assert.assertEquals("s",Directions.findBaseDirection("S"));
        Assert.assertEquals("s",Directions.findBaseDirection("south"));
        Assert.assertEquals("s",Directions.findBaseDirection("South"));
        Assert.assertEquals("s",Directions.findBaseDirection("SoUth"));
        Assert.assertEquals("s",Directions.findBaseDirection("SOUTH"));

        Assert.assertEquals("n",Directions.findBaseDirection("n"));
        Assert.assertEquals("n",Directions.findBaseDirection("N"));
        Assert.assertEquals("n",Directions.findBaseDirection("north"));
        Assert.assertEquals("n",Directions.findBaseDirection("North"));
        Assert.assertEquals("n",Directions.findBaseDirection("nOrTH"));
        Assert.assertEquals("n",Directions.findBaseDirection("NORTH"));

        Assert.assertEquals("e",Directions.findBaseDirection("e"));
        Assert.assertEquals("e",Directions.findBaseDirection("E"));
        Assert.assertEquals("e",Directions.findBaseDirection("east"));
        Assert.assertEquals("e",Directions.findBaseDirection("East"));
        Assert.assertEquals("e",Directions.findBaseDirection("eAsT"));
        Assert.assertEquals("e",Directions.findBaseDirection("EAST"));

        Assert.assertEquals("w",Directions.findBaseDirection("w"));
        Assert.assertEquals("w",Directions.findBaseDirection("W"));
        Assert.assertEquals("w",Directions.findBaseDirection("west"));
        Assert.assertEquals("w",Directions.findBaseDirection("West"));
        Assert.assertEquals("w",Directions.findBaseDirection("WeST"));
        Assert.assertEquals("w",Directions.findBaseDirection("WEST"));
    }

    @Test
    public void unknownDirectionsReturnEmptyString(){

        Assert.assertEquals("",Directions.findBaseDirection("in"));

        Assert.assertEquals("",Directions.findOppositeDirection("out"));
    }

    @Test
    public void oppositeDirections(){

        // only do one full pass for root since findOppositeDirection uses findBaseDirection
        Assert.assertEquals("s",Directions.findOppositeDirection("n"));
        Assert.assertEquals("s",Directions.findOppositeDirection("N"));
        Assert.assertEquals("s",Directions.findOppositeDirection("north"));
        Assert.assertEquals("s",Directions.findOppositeDirection("North"));
        Assert.assertEquals("s",Directions.findOppositeDirection("nOrTH"));
        Assert.assertEquals("s",Directions.findOppositeDirection("NORTH"));

        Assert.assertEquals("e",Directions.findOppositeDirection("west"));
        Assert.assertEquals("w",Directions.findOppositeDirection("EAST"));
        Assert.assertEquals("n",Directions.findOppositeDirection("S"));


    }
}
