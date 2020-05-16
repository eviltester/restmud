package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.locations.GateDirection;
import uk.co.compendiumdev.restmud.engine.game.locations.GateStatus;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationDirectionGate;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class DefaultVerbHandlerTest {

    @Test
    public void defaultVerbsAlwaysFail(){

        // because the success conditions are handled by the condition rules

        DefaultVerbHandler closeHandler =  new DefaultVerbHandler().setGame(null);
        final LastAction action = closeHandler.doVerb(null,"whatever");

        Assert.assertTrue(action.lastactionresult.contains(" whatever"));
        Assert.assertTrue(action.isFail());
    }

    @Test
    public void defaultNamedVerbsAlwaysFail(){

        // because the success conditions are handled by the condition rules

        DefaultVerbHandler closeHandler =  new DefaultVerbHandler().setGame(null).usingCurrentVerb("doit");
        final LastAction action = closeHandler.doVerb(null,"whatever");

        Assert.assertTrue(action.lastactionresult.contains("doit whatever"));
        Assert.assertTrue(action.isFail());
    }

    @Test
    public void defaultVerbHandlerConditions(){

        // Default verb handler used for custom verbs
        DefaultVerbHandler handler = new DefaultVerbHandler().setGame(null);
        Assert.assertTrue("Doing something should take time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("Doing something can generate messages",
                handler.shouldAddGameMessages());
        Assert.assertFalse(
                "Custom verbs do not look unless that is part of the condition",
                handler.shouldLookAfterVerb());
    }
}
