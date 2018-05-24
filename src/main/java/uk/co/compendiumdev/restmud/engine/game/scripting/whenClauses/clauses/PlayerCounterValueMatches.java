package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptableCounterCondition;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;


public class PlayerCounterValueMatches implements ScriptWhenClause {


    private final ScriptableCounterCondition counterCondition;

    public PlayerCounterValueMatches() {
        this.counterCondition = ScriptableCounterCondition.empty();
    }

    public String getCommandName(){
        return When.USER_COUNTER_VALUE_IS;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, PlayerCommand command) {

        // re-use existing counterCondition
        counterCondition.setFrom(when.getParameter());
        return  counterCondition.comparedTo(player.getUserCounter(counterCondition.name));
    }

}
