package uk.co.compendiumdev.restmud.engine.game.scripting;


import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses.LastActionSuccess;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import org.junit.Assert;
import org.junit.Test;

public class ScriptThenCommandTest {

    @Test
    public void canCreateALastActionSuccessScriptCommand(){


        ScriptClause success = new ScriptClause("lastaction.success", "you do the thing");

        GameInitializer theGameInit = new GameInitializer();
        theGameInit.generate("defaultenginegame.json");
        theGameInit.addDefaultUser("The Test User", "tester", "aPassword");
        MudGame game = theGameInit.getGame();
        MudUser player = game.getUserManager().getUser("tester");

        ScriptThenCommand command = new LastActionSuccess(game);
        Assert.assertEquals("lastaction.success", command.getCommandName());
        ProcessConditionReturn ret = command.execute(success, player);
        Assert.assertTrue(ret.hasAnyActions());
    }
}
