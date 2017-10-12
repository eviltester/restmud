package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptableCounterCondition;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;


public class PlayerCounterValueMatches implements ScriptWhenClause {
    private final MudGame game;

    ScriptableCounterCondition counterCondition;

    public PlayerCounterValueMatches(MudGame game) {
        this.game = game;
        this.counterCondition = ScriptableCounterCondition.empty();
    }

    public String getCommandName(){
        return When.USER_COUNTER_VALUE_IS;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, String nounPhrase) {

        // re-use existing counterCondition
        counterCondition.setFrom(when.getParameter());
        return  counterCondition.comparedTo(player.getUserCounter(counterCondition.name));
    }

    @Override
    public ScriptWhenClause addHttpDetails(RestMudHttpRequestDetails details) {
        return this;
    }
}
