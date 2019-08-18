package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptableFlag;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;


public class LocationExitExists implements ScriptWhenClause {

    private final MudGame game;

    public String getCommandName(){
        return When.LOCATION_EXIT_EXISTS;
    }

    public LocationExitExists(MudGame game) {
        this.game = game;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, PlayerCommand command) {

        MudLocation whereAmI = game.getGameLocations().get(player.getLocationId());

        ScriptableFlag locationFlag = new ScriptableFlag(when.getParameter());

        return whereAmI.canGo(locationFlag.name)==locationFlag.getValue();
    }

}
