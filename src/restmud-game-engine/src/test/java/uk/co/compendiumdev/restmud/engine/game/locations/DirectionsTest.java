package uk.co.compendiumdev.restmud.engine.game.locations;

import org.junit.Assert;
import org.junit.Test;

public class DirectionsTest {

    Directions directions = new Directions();

    @Test
    public void baseDirectionValuesMapAsExpected(){

        Assert.assertEquals("s",directions.findBaseDirection("s"));
        Assert.assertEquals("s",directions.findBaseDirection("S"));
        Assert.assertEquals("s",directions.findBaseDirection("south"));
        Assert.assertEquals("s",directions.findBaseDirection("South"));
        Assert.assertEquals("s",directions.findBaseDirection("SoUth"));
        Assert.assertEquals("s",directions.findBaseDirection("SOUTH"));

        Assert.assertEquals("n",directions.findBaseDirection("n"));
        Assert.assertEquals("n",directions.findBaseDirection("N"));
        Assert.assertEquals("n",directions.findBaseDirection("north"));
        Assert.assertEquals("n",directions.findBaseDirection("North"));
        Assert.assertEquals("n",directions.findBaseDirection("nOrTH"));
        Assert.assertEquals("n",directions.findBaseDirection("NORTH"));

        Assert.assertEquals("e",directions.findBaseDirection("e"));
        Assert.assertEquals("e",directions.findBaseDirection("E"));
        Assert.assertEquals("e",directions.findBaseDirection("east"));
        Assert.assertEquals("e",directions.findBaseDirection("East"));
        Assert.assertEquals("e",directions.findBaseDirection("eAsT"));
        Assert.assertEquals("e",directions.findBaseDirection("EAST"));

        Assert.assertEquals("w",directions.findBaseDirection("w"));
        Assert.assertEquals("w",directions.findBaseDirection("W"));
        Assert.assertEquals("w",directions.findBaseDirection("west"));
        Assert.assertEquals("w",directions.findBaseDirection("West"));
        Assert.assertEquals("w",directions.findBaseDirection("WeST"));
        Assert.assertEquals("w",directions.findBaseDirection("WEST"));
    }

    @Test
    public void unknownDirectionsReturnEmptyString(){

        Assert.assertEquals("",directions.findBaseDirection("in"));

        Assert.assertEquals("",directions.findOppositeDirection("out"));
    }

    @Test
    public void oppositeDirections(){

        // only do one full pass for root since findOppositeDirection uses findBaseDirection
        Assert.assertEquals("s",directions.findOppositeDirection("n"));
        Assert.assertEquals("s",directions.findOppositeDirection("N"));
        Assert.assertEquals("s",directions.findOppositeDirection("north"));
        Assert.assertEquals("s",directions.findOppositeDirection("North"));
        Assert.assertEquals("s",directions.findOppositeDirection("nOrTH"));
        Assert.assertEquals("s",directions.findOppositeDirection("NORTH"));

        Assert.assertEquals("e",directions.findOppositeDirection("west"));
        Assert.assertEquals("w",directions.findOppositeDirection("EAST"));
        Assert.assertEquals("n",directions.findOppositeDirection("S"));


    }
}
