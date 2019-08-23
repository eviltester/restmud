package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;


public class NotCondition implements ScriptWhenClause {

    private final MudGame game;

    public NotCondition(MudGame game) {
        this.game = game;
    }

    public String getCommandName(){
        return When.NOT;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, PlayerCommand command) {

        if(!when.isContainer()) {
            System.out.println("NOT condition should be a container");
            return false;
        }

        // should only be one in not
        final ScriptClause clauseToNegate = when.getClauses().get(0);

        final ScriptWhenClause nextCommand = game.scriptingEngine().
                whenClauseCommandList().getCommand(clauseToNegate.getToken());

        final boolean lastMatch = nextCommand.
                execute(clauseToNegate, player, player.getCurrentCommand());

        return !lastMatch;
    }

}
