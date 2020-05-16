package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class VerbTakeHandler implements VerbHandler{
    private MudGame game;

    public VerbTakeHandler setGame(MudGame mudGame){
        this.game = mudGame;
        return this;
    }

    public LastAction doVerb(MudUser player, String nounPhrase) {

        MudLocation whereAmI = game.getGameLocations().get(player.getLocationId());

        if(whereAmI.isLocationDark() && !player.canISeeInTheDark()){
            return  LastAction.createError("It is too dark for me to try and pick anything up, who knows what I might grab!");
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

        return take(player, whereAmI, thing);

    }

    private LastAction take(MudUser player, MudLocation whereAmI, MudCollectable thing) {
        // does the thing grant any abilities?
        String extraMessage="";
        String thingAbility = thing.getAbilityName();

        if(thingAbility.length()>0){

            String abilityOn = "things";

            if(thingAbility.contains("/")){
                thingAbility = thingAbility.replaceFirst("/", "' and '");
                abilityOn="";
            }
            extraMessage=String.format(" , oooh, and you now have the ability to '%s' %s (power=%d).", thingAbility, abilityOn, thing.getAbilityPower());
        }

        if(whereAmI.moveCollectableToPlayerInventory(thing.getCollectableId(), player.inventory())){
            return LastAction.createSuccess(String.format("You took: %s. You now have the %s %s",thing.getCollectableId(), thing.getDescription(), extraMessage));
        }else{
            return LastAction.createError(String.format("You took: Nothing (I couldn't take %s)", thing.getCollectableId()));
        }

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
