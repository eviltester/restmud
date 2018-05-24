package uk.co.compendiumdev.restmud.engine.game.verbs;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ObjectInspection;

import java.util.Random;

/**
 * Created by Alan on 04/06/2016.
 */
public class VerbInspect {
    private final MudUser player;

    public VerbInspect(MudUser player) {
        this.player = player;
    }

    public LastAction aCollectable(MudLocation whereAmI, MudCollectable collectable, String nounPhrase) {

        if(collectable==null){
            return LastAction.createError(String.format("Sorry, What? I can't inspect %s", nounPhrase));
        }


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
}
