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
public class ShowGate implements ScriptThenCommand {
    private final MudGame game;

    public ShowGate(MudGame game) {
        this.game = game;
    }

    @Override
    public String getCommandName() {
        return Then.SHOW_GATE;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {

        ProcessConditionReturn ret = new ProcessConditionReturn();

        String thensValue = scriptClause.getParameter();

        MudLocationDirectionGate gate = game.getGateManager().getGateNamed(thensValue);
        if(gate==null){
            ret.addNewAction(LastAction.createError("Scripting error showgate - there is no gate named " + thensValue));
        }else{
            gate.gateIsHidden(false);
        }

        return ret;
    }
}
