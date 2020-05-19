package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.scripting.AttributeParser;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 07/08/2016.
 */
public class ExitRemove implements ScriptThenCommand {
    private final MudGame game;

    public ExitRemove(MudGame game) {
        this.game = game;
    }

    @Override
    public String getCommandName() {
        return Then.REMOVE_EXIT;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {
        ProcessConditionReturn ret = new ProcessConditionReturn();

        // remove an existing exit

        AttributeParser attribs = new AttributeParser(scriptClause.getParameter());
        String fromLocation = attribs.getPart(0);
        String direction = attribs.getPart(1);
        String toLocation = attribs.getPart(2);


        MudLocation location = game.getGameLocations().get(fromLocation);

        // can't remove it if it does not exist
        if(!location.canGo(direction)){
            ret.addNewAction(LastAction.createError("Scripting error : we cannot go " + direction));
            return ret;
        }

        location.removeExit(direction);

        return ret;
    }
}
