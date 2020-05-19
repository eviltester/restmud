package uk.co.compendiumdev.restmud.engine.game.locations;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class MudLocationParserTest {

    @Test
    public void parserReturnsASingleSimpleExit(){

        final Map<String, MudLocationExit> exits = new MudLocationParser("1").parse("e:2");
        Assert.assertEquals(1, exits.size());

        MudLocationExit exit = exits.get("e");
        Assert.assertNotNull(exit);

        Assert.assertEquals("2",exit.getDestinationId());
        Assert.assertEquals("e",exit.getDirection());
        Assert.assertTrue(exit.isVisible());
        Assert.assertFalse(exit.isLocal());
    }

    @Test
    public void parserReturnsEmptyMapIfNoexits(){

        final Map<String, MudLocationExit> exits = new MudLocationParser("1").parse("");
        Assert.assertNotNull(exits);
        Assert.assertEquals(0, exits.size());
    }

    @Test
    public void parserReturnsMultipleSimpleExits(){

        final Map<String, MudLocationExit> exits = new MudLocationParser("2").parse("n:1,w:3");
        Assert.assertEquals(2, exits.size());

        MudLocationExit exit = exits.get("n");
        Assert.assertNotNull(exit);

        Assert.assertEquals("1",exit.getDestinationId());
        Assert.assertEquals("n",exit.getDirection());
        Assert.assertTrue(exit.isVisible());
        Assert.assertFalse(exit.isLocal());

        exit = exits.get("w");
        Assert.assertNotNull(exit);

        Assert.assertEquals("3",exit.getDestinationId());
        Assert.assertEquals("w",exit.getDirection());
        Assert.assertTrue(exit.isVisible());
        Assert.assertFalse(exit.isLocal());
    }

    @Test
    public void parserCanReturnASecretExit(){

        final Map<String, MudLocationExit> exits = new MudLocationParser("3").parse("n:1:secret");
        Assert.assertEquals(1, exits.size());

        MudLocationExit exit = exits.get("n");
        Assert.assertNotNull(exit);

        Assert.assertEquals("1",exit.getDestinationId());
        Assert.assertEquals("n",exit.getDirection());
        Assert.assertFalse(exit.isVisible());
        Assert.assertFalse(exit.isLocal());
    }

    @Test
    public void parserCanReturnALocallyProcessedExit(){

        final Map<String, MudLocationExit> exits = new MudLocationParser("4").parse("n:local");
        Assert.assertEquals(1, exits.size());

        MudLocationExit exit = exits.get("n");
        Assert.assertNotNull(exit);

        Assert.assertEquals("n",exit.getDirection());
        Assert.assertTrue(exit.isVisible());
        Assert.assertTrue(exit.isLocal());
    }

    @Test
    public void parserCanReturnASecretLocallyProcessedExit(){

        final Map<String, MudLocationExit> exits = new MudLocationParser("5").parse("n:local:secret");
        Assert.assertEquals(1, exits.size());

        MudLocationExit exit = exits.get("n");
        Assert.assertNotNull(exit);

        Assert.assertEquals("n",exit.getDirection());
        Assert.assertFalse(exit.isVisible());
        Assert.assertTrue(exit.isLocal());
        
    }

    // TODO: parser can report errors in the parsing
    // TODO: parser validates the directions are valid and only adds valid directions
    

}
