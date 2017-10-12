package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.locations.Directions;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;


public class DirectionMatches implements ScriptWhenClause {


    public DirectionMatches(MudGame game) {

    }

    public String getCommandName(){
        return When.DIRECTION;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, String nounPhrase) {

        return when.getParameter().contentEquals(Directions.findBaseDirection(nounPhrase.toLowerCase()));
    }

    @Override
    public ScriptWhenClause addHttpDetails(RestMudHttpRequestDetails details) {
        return this;
    }
}
