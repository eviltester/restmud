package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 09/08/2016.
 */
public class VerbTakeHandler implements VerbHandler{
    private MudGame game;

    public VerbTakeHandler() {
    }

    public void setGame(MudGame mudGame){
        this.game = mudGame;
    }

    public LastAction doVerb(MudUser player, String nounPhrase) {

        MudLocation whereAmI = game.getGameLocations().get(player.getLocationId());

        if(whereAmI.isLocationDark() && !player.canISeeInTheDark()){
            return  LastAction.createError(String.format("It is too dark for me to try and pick anything up, who knows what I might grab!"));
        }

        MudLocationObject theObject = game.getLocationObjects().get(nounPhrase);
        if(theObject!=null){
            if(whereAmI.objects().contains(nounPhrase)) {
                return  LastAction.createError(String.format("You tried to take: %s but you can't take that", theObject.getDescription()));
            }
        }

        MudCollectable thing = game.getGameCollectables().get(nounPhrase);
        if(thing == null){
            return LastAction.createError(String.format("You took: Nothing (%s is not here)", nounPhrase));
        }

        if(thing.getAbilityName().trim().length()>0) {
            if (player.hasTheAbilityTo(thing.getAbilityName())) {
                return LastAction.createError(String.format("You took: Nothing (You already have the ability to %s)", thing.getAbilityName()));
            }
        }

        return player.take().removeFromLocationAndAddToInventory(whereAmI, thing);

    }

    @Override
    public boolean shouldAddGameMessages() {
        return true;
    }

    @Override
    public boolean shouldLookAfterVerb() {
        return false;
    }

    @Override
    public boolean actionUpdatesTimeStamp() {
        return true;
    }

}
