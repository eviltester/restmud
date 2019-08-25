package uk.co.compendiumdev.restmud.engine.game.scripting;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.parser.VerbToken;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;

import java.util.List;

public class UserDefinedRulesProcessor {
    private final MudGame game;

    public UserDefinedRulesProcessor(MudGame mudGame) {
        this.game = mudGame;
    }

    public ProcessConditionReturn processVerbConditions(MudUser player, PlayerCommand currentCommand) {

        ProcessConditionReturn ret = new ProcessConditionReturn();

        if(player==null){
            return ret;
        }

        if(currentCommand.getVerbToken()==null){
            return ret;
        }

        // check for matching verbs and synonyms
        List<VerbCondition> conditions = game.getMatchingVerbConditions(currentCommand.getVerbToken());

        //List<VerbCondition> conditions = game.getVerbConditions().get(currentCommand.getVerbToken().getValue());

        if (conditions == null || conditions.size()==0) {
            return ret;
        }


        MudLocation whereAmI = game.getGameLocations().get(player.getLocationId());


        for (VerbCondition condition : conditions) {

            if (conditionWhenClauseMatches(condition, player, whereAmI)) {
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

            if (conditionWhenClauseMatches(condition, player, whereAmI)) {
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

    private boolean conditionWhenClauseMatches(ScriptCondition condition, MudUser player, MudLocation whereAmI) {

        // 20180520 instead of , String nounPhrase, RestMudHttpRequestDetails httpRequestDetails
        // use player.currentCommand().getNounPhrase(), player.currentCommand().getHttpRequestDetails()

        List<ScriptClause> whens = condition.whenClauses();

        // all conditions must match for the when clause to be true
        // only exit matching if we find a false

        boolean lastMatch;
        ScriptWhenClause command;

        for (ScriptClause when : whens) {


            command = game.scriptingEngine().
                    whenClauseCommandList().getCommand(when.getToken());

            lastMatch = command.
                    execute(when, player, player.getCurrentCommand());

            if(lastMatch != when.executionMatchValue()){
            //if (lastMatch) {
                return false;
            }
        }

        // if we arrived here then everything was true
        return true;
    }



}
