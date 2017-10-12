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
public class VerbUseHandler implements VerbHandler{

    private MudGame game;

    @Override
    public void setGame(MudGame mudGame) {
        this.game = mudGame;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        MudLocation location = game.getGameLocations().get(player.getLocationId());

        // create a VerbConditions for new 'use'ages otherwise this is the default


        // if nounphrase is a location obj which is a dispenser then we can use it
        MudLocationObject dispenser = game.getLocationObjects().get(nounPhrase);
        if(dispenser!=null){
            if(dispenser.isThisADispenser()){
                // ok, use the template to create a collectable
                MudCollectable dispensedItem = dispenser.dispense(game.idGenerator().generateId(dispenser.getTemplateId()));
                game.addCollectable(dispensedItem, location);

                return LastAction.createSuccess(
                        String.format("OK, you use '%s' and you see it dispense '<span id='%s'>%s</span>'",
                                dispenser.getDescription(), dispensedItem.getCollectableId(), dispensedItem.getDescription()));
            }
        }

        return LastAction.createError(String.format("Sorry, I don't know how to use %s",nounPhrase));
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
