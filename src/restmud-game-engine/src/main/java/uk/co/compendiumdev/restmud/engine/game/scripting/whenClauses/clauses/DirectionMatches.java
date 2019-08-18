package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.locations.Directions;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;


public class DirectionMatches implements ScriptWhenClause {


    public DirectionMatches(MudGame game) {

    }

    public String getCommandName(){
        return When.DIRECTION;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, PlayerCommand command) {

        return when.getParameter().contentEquals(Directions.findBaseDirection(command.getNounPhrase()));
    }

}
