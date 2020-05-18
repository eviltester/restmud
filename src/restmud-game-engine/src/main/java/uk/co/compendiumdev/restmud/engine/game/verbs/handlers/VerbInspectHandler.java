package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ObjectInspection;

import java.util.Random;

public class VerbInspectHandler  implements VerbHandler {
    private MudGame game;
    private String verbName="inspect";

    @Override
    public VerbInspectHandler setGame(MudGame mudGame) {
        this.game = mudGame;
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        MudLocation whereAmI = game.getGameLocations().get(player.getLocationId());

        // does thing exist?
        String thingId = nounPhrase;

        MudCollectable collectable = game.getGameCollectables().get(thingId);

        if(collectable==null) {
            return LastAction.createError(String.format("Sorry I don't know how to inspect %s", nounPhrase));
        }

        return inspect(player, whereAmI, collectable, nounPhrase);
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


    private LastAction inspect(MudUser player, MudLocation whereAmI, MudCollectable collectable, String nounPhrase) {

        int minCost = 5;
        int maxCost = 20;
        int inspectCost = new Random().nextInt(maxCost - minCost + 1) + minCost;

        int score = player.getScore();

        // do I have enough score?
        if(inspectCost>score){
            return LastAction.createError(
                    String.format(
                            "You only have %d score, but this inspect would cost %d. Try Again?",
                            score, inspectCost));

        }

        boolean canInspectItHere = false;

        // is it here?
        if(whereAmI.collectables().contains(collectable.getCollectableId())){
            canInspectItHere=true;
        }

        // am I holding it?
        if(player.inventory().contains(collectable.getCollectableId())){
            canInspectItHere=true;
        }

        if(!canInspectItHere){
            return LastAction.createError(String.format("Sorry I can't see %s here, and I don't seem to be carrying %s.", collectable.getCollectableId(), collectable.getDescription()));
        }

        // OK, I can inspect it, and I have enough points

        player.incrementScoreBy((inspectCost*-1));

        // TODO: this needs to be pulled out into its own class as I should not have to test this in the InspectHandler
        ObjectInspection inspectReport = new ObjectInspection();
        inspectReport.playerName = player.userName();
        inspectReport.costOfInspection = String.valueOf(inspectCost);
        inspectReport.playerScoreIsNow = String.valueOf(player.getScore());
        inspectReport.collectableId = collectable.getCollectableId();
        inspectReport.collectableDescription = collectable.getDescription();
        inspectReport.collectableIsHoardable = String.valueOf(collectable.isHoardable());
        inspectReport.collectableHoardableScore = String.valueOf(collectable.getHoardableScore());
        if(collectable.getAbilityName().length() != 0){
            inspectReport.collectableProvidesAbility = collectable.getAbilityName();
            inspectReport.collectableAbilityPower = String.valueOf(collectable.getAbilityPower());
        }

        LastAction output = LastAction.createSuccess(String.format("You inspect %s at a cost of %d points", collectable.getCollectableId(), inspectCost));
        output.setInspectReport(inspectReport);

        return output;
    }

    @Override
    public VerbInspectHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }

}
