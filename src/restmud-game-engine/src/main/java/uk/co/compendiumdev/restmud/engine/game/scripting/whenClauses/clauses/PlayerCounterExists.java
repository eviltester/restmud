package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptableCounter;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;


public class PlayerCounterExists implements ScriptWhenClause {


    private final ScriptableCounter counter;

    public PlayerCounterExists() {
        this.counter = ScriptableCounter.empty();
    }

    public String getCommandName(){
        return When.DOES_USER_COUNTER_EXIST;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, PlayerCommand command) {

        // re-use existing counter
        counter.setFrom(when.getParameter());

        return player.getCounters().counterExists(counter.name);
    }

}
