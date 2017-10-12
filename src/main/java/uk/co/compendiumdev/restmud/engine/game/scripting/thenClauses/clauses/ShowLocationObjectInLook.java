package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.AttributePair;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;

/**
 * Created by Alan on 07/08/2016.
 */
public class ShowLocationObjectInLook implements ScriptThenCommand {
    private final MudGame game;

    public ShowLocationObjectInLook(MudGame game) {
        this.game = game;
    }

    @Override
    public String getCommandName() {
        return Then.SHOW_LOCATION_OBJECT_IN_LOOK;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        ProcessConditionReturn ret = new ProcessConditionReturn();
        ret.addLocationObject(new AttributePair(scriptClause.getParameter()));
        return ret;
    }
}
