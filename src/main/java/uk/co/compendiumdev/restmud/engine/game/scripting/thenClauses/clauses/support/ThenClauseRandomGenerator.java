package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses.support;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.generators.RandomTreasureGenerator;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Supporting methods for Random treasure generation refactored out of individual clauses for re-use
 */
public class ThenClauseRandomGenerator {

    public static RandomTreasureGenerator configuredFor(MudGame game, ScriptClause scriptClause, String thenClauseCommandName, ProcessConditionReturn ret) {
        // a set of : separated values - first is the number of treasures, optional rest are the locations

        String thensValue = scriptClause.getParameter();

        String []vals = parseThenArguments(scriptClause.getParameter(), thenClauseCommandName, ret);
        
        RandomTreasureGenerator moneygod = new RandomTreasureGenerator(game);

        List<String> locations = getLocationsFromArguments(vals);

        if(locations.size()>0) {
            moneygod.generateIntoTheseLocations(locations);
        }

        return moneygod;
    }

    private static List<String> getLocationsFromArguments(String[] vals) {
        List<String> locations = new ArrayList();


        for(int x=1; x<vals.length; x++){
            locations.add(String.valueOf(vals[x]));
        }
        return locations;
    }

    private static String[] parseThenArguments(String parameter, String thenClauseCommandName, ProcessConditionReturn ret) {

        String []vals = parameter.split(":");
        if(vals==null) {
            ret.addNewAction(LastAction.createError("Scripting error " + thenClauseCommandName+  " - there are no params - should be number and then optional locations seperated by :"));
        }

        return vals;
    }


    public static int getNumberToGenerateFrom(ScriptClause scriptClause, String commandName, ProcessConditionReturn ret) {

        String []vals = parseThenArguments(scriptClause.getParameter(), commandName, ret);

        int numberOfTreasures = Integer.parseInt(vals[0]);
        return numberOfTreasures;
    }
}
