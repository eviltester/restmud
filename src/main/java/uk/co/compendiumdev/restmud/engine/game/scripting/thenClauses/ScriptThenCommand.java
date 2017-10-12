package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses;


import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;

public interface ScriptThenCommand {

    String getCommandName();
    ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player);

}
