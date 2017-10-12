package uk.co.compendiumdev.restmud.engine.game.scripting;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.parser.VerbToken;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;

import java.util.List;

public class UserDefinedRulesProcessor {
    private final MudGame game;

    public UserDefinedRulesProcessor(MudGame mudGame) {
        this.game = mudGame;
    }


    public ProcessConditionReturn processVerbConditions(MudUser player, VerbToken verbToHandle, String nounPhrase, RestMudHttpRequestDetails httpRequestDetails) {

        ProcessConditionReturn ret = new ProcessConditionReturn();

        if(player==null){
            return ret;
        }

        if(verbToHandle==null){
            return ret;
        }

        List<VerbCondition> conditions = game.getVerbConditions().get(verbToHandle.getValue());

        if (conditions == null) {
            return ret;
        }


        MudLocation whereAmI = game.getGameLocations().get(player.getLocationId());


        for (VerbCondition condition : conditions) {

            if (conditionWhenClauseMatches(condition, player, whereAmI, nounPhrase, httpRequestDetails)) {
                // do the thens
                ret = processConditionThenActions(condition, player);
            }

            // we only want to do one verb condition at a time so if we 'did' something then stop
            if (ret.hasAnyActions()) {
                break;
            }

        }

        return ret;
    }

    public ProcessConditionReturn processTurnConditions(MudUser player) {

        ProcessConditionReturn ret = new ProcessConditionReturn();

        if(player==null){
            return ret;
        }

        MudLocation whereAmI = game.getGameLocations().get(player.getLocationId());

        List<PriorityTurnCondition> conditions = game.getPriorityTurnConditions();

        for (PriorityTurnCondition condition : conditions) {

            if (conditionWhenClauseMatches(condition, player, whereAmI, "", null)) {
                // do the thens
                ProcessConditionReturn tempRet = processConditionThenActions(condition, player);

                // ignore the actions but get the messages and the collectables
                ret.addMessages(tempRet.getMessagesAsList());
                ret.addCollectables(tempRet.getCollectables());
                ret.addLocationObjects(tempRet.getLocationObjects());
            }
        }

        return ret;
    }


    private ProcessConditionReturn processConditionThenActions(ScriptCondition condition, MudUser player) {

        ProcessConditionReturn ret = new ProcessConditionReturn();

        List<ScriptClause> thens = condition.thenClauses();


        for (ScriptClause then : thens) {

            ret.append(
                    game.scriptingEngine().
                            thenClauseCommandList().getCommand(then.getToken()).
                            execute(then, player));
        }

        return ret;
    }

    private boolean conditionWhenClauseMatches(ScriptCondition condition, MudUser player, MudLocation whereAmI, String nounPhrase, RestMudHttpRequestDetails httpRequestDetails) {
        List<ScriptClause> whens = condition.whenClauses();

        // all conditions must match for the when clause to be true
        // only exit matching if we find a false

        boolean lastMatch;
        ScriptWhenClause command;

        for (ScriptClause when : whens) {


            command = game.scriptingEngine().
                    whenClauseCommandList().getCommand(when.getToken());

            command.addHttpDetails(httpRequestDetails);

            lastMatch = command.
                    execute(when, player, nounPhrase.toLowerCase());


            if (lastMatch == false) {
                return false;
            }
        }

        // if we arrived here then everything was true
        return true;
    }


}
