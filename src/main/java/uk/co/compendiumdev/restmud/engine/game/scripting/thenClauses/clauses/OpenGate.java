package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationDirectionGate;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 07/08/2016.
 */
public class OpenGate implements ScriptThenCommand {
    private final MudGame game;

    public OpenGate(MudGame game) {
        this.game = game;
    }

    @Override
    public String getCommandName() {
        return Then.OPEN_GATE;
    }

    // will open gate even if it is hidden
    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        ProcessConditionReturn ret = new ProcessConditionReturn();
        MudLocationDirectionGate gate = game.getGateManager().getGateNamed(scriptClause.getParameter());
        if(gate==null){
            ret.addNewAction(LastAction.createError("Scripting error opengate - there is no gate named " + scriptClause.getParameter()));
        }else{
            gate.open();
        }
        return ret;
    }
}
