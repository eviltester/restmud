package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptableFlag;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;


public class PlayerFlagExists implements ScriptWhenClause {
    private final MudGame game;

    ScriptableFlag flag;

    public PlayerFlagExists(MudGame game) {
        this.game = game;
        this.flag = ScriptableFlag.empty();
    }

    public String getCommandName(){
        return When.DOES_FLAG_EXIST;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, String nounPhrase) {

        // re-use existing flag
        flag.setFrom(when.getParameter());

        return (player.userFlagExists(flag.name)==flag.getValue());


    }

    @Override
    public ScriptWhenClause addHttpDetails(RestMudHttpRequestDetails details) {
        return this;
    }
}
