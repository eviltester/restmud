package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.generators.RandomTreasureGenerator;
import uk.co.compendiumdev.restmud.engine.game.scripting.ProcessConditionReturn;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ScriptThenCommand;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 07/08/2016.
 */
public class RandomlyGenerateTreasure implements ScriptThenCommand {
    private final MudGame game;

    public RandomlyGenerateTreasure(MudGame game) {
        this.game = game;
    }

    @Override
    public String getCommandName() {
        return Then.RANDOMLY_GENERATE_HOARDABLE_TREASURE;
    }

    @Override
    public ProcessConditionReturn execute(ScriptClause scriptClause, MudUser player) {

        ProcessConditionReturn ret = new ProcessConditionReturn();

        String thensValue = scriptClause.getParameter();

        // a set of : separated values - first is the number of treasures, optional rest are the locations

        String []vals = thensValue.split(":");
        if(vals==null) {
            ret.addNewAction(LastAction.createError("Scripting error RandomlyGenerateTreasure - there are no params - shoudld be number and then locations seperated by :"));
        }

        RandomTreasureGenerator moneygod = new RandomTreasureGenerator(game);

        int numberOfTreasures = Integer.parseInt(vals[0]);

        List<String> locations = new ArrayList();

        for(int x=1; x<vals.length; x++){
            locations.add(String.valueOf(vals[x]));
        }

        if(locations.size()>0) {
            moneygod.generateIntoTheseLocations(locations);
        }

        moneygod.createHoardableTreasures(numberOfTreasures);

        return ret;
    }
}
