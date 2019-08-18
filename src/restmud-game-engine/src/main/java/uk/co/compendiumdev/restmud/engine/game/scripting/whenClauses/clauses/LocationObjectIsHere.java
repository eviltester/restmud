package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.ScriptWhenClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;


public class LocationObjectIsHere implements ScriptWhenClause {
    private final MudGame game;

    public LocationObjectIsHere(MudGame game) {
        this.game = game;
    }

    public String getCommandName(){
        return When.LOCATION_OBJECT_IS_HERE;
    }

    @Override
    public boolean execute(ScriptClause when, MudUser player, PlayerCommand command) {

        MudLocation location = game.getGameLocations().get(player.getLocationId());

        if(location!=null) {
            return location.objects().contains(when.getParameter());
        }else{
            return false;
        }

    }

}
