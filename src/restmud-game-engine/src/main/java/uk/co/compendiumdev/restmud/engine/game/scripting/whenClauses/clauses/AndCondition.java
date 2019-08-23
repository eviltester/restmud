package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.UserDefinedRulesProcessor;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;


public class AndCondition implements ScriptWhenClause {

    private final MudGame game;

    public AndCondition(MudGame game) {
        this.game = game;
    }

    public String getCommandName(){
        return When.AND;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, PlayerCommand command) {

        if(!when.isContainer()) {

            System.out.println("AND condition should be a container");
            return false;
        }

        // loop through all executing them
        for (ScriptClause clause : when.getClauses()) {

            final ScriptWhenClause nextCommand = game.scriptingEngine().
                    whenClauseCommandList().getCommand(clause.getToken());

            final boolean lastMatch = nextCommand.
                    execute(clause, player, player.getCurrentCommand());

            if(lastMatch != clause.executionMatchValue()){
                return false;
            }

        }

        return true;
    }

}
