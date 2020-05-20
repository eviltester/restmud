package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationDirectionGate;
import uk.co.compendiumdev.restmud.engine.game.scripting.AttributeParser;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 07/08/2016.
 */
public class ExitCreate implements ScriptThenCommand {
    private final MudGame game;

    public ExitCreate(MudGame game) {
        this.game = game;
    }

    @Override
    public String getCommandName() {
        return Then.CREATE_EXIT;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        ProcessConditionReturn ret = new ProcessConditionReturn();

        // create a new exit

        AttributeParser attribs = new AttributeParser(scriptClause.getParameter());
        String fromLocation = attribs.getPart(0);
        String direction = attribs.getPart(1);
        String toLocation = attribs.getPart(2);


        MudLocation location = game.getGameLocations().get(fromLocation);

        // could possible amend but lets say no
        if(location.canGo(direction)){
            ret.addNewAction(LastAction.createError("Scripting error an exit already exists to the  " + direction));
            return ret;
        }


        location.addExit(direction, toLocation);

        return ret;
    }
}
