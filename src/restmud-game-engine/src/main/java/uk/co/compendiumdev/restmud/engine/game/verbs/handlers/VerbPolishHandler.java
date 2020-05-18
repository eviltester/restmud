package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

import java.util.Random;

public class VerbPolishHandler implements VerbHandler{
    private MudGame game;
    private String verbName="polish";

    @Override
    public VerbPolishHandler setGame(MudGame mudGame) {
        this.game = mudGame;
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {
        // User can polish items in inventory if they have the ability to polish

        MudCollectable thing = game.getGameCollectables().get(nounPhrase);
        if(thing==null){
            return LastAction.createError((String.format("Sorry, I can't see %s anywhere", nounPhrase)));
        }

        if(!player.inventory().contains(nounPhrase)){
            return LastAction.createError(String.format("Polish What? You don't have %s", nounPhrase));
        }

        if(!player.hasTheAbilityTo("polish")){
            return LastAction.createError("You try to polish it with your hand but it doesn't work. You need to carry something that let's you polish stuff!");
        }

        MudCollectable polisher = player.grantedTheAbilityToBy("polish");


        return polish(player, thing, polisher, game.getGameLocations().get(MudGame.JUNK_ROOM));
    }

    private LastAction polish(MudUser player, MudCollectable thingToPolish, MudCollectable polisher, MudLocation junkRoom) {


        // Can only polish hoardable things

        if(!thingToPolish.isHoardable()){
            return LastAction.createError("Sorry, it would not be worthwhile polishing that!");
        }

        // generate a random polish amount at max your polish power
        int fullPower = polisher.getAbilityPower();
        int maxPower = fullPower/2;
        int minPower = 1;

        int polishPower;

        if(fullPower<10){
            polishPower = fullPower;
        }else{
            polishPower = new Random().nextInt(maxPower - minPower + 1) + minPower;
        }

        int hoardableAt = thingToPolish.getHoardableScore();
        hoardableAt = hoardableAt + polishPower;
        thingToPolish.canHoard(true, hoardableAt);

        int powerLeft = fullPower-polishPower;
        polisher.setAbilityPower(powerLeft);

        String polishResults =
                String.format("Good work. You polished '%s' by %d and now it is worth %d hoard points. Your '%s' has %d polish power left.",
                        thingToPolish.getDescription(), polishPower, hoardableAt,
                        polisher.getDescription(), powerLeft);

        String disappearMessage="";
        if(powerLeft<=0){
            // it disappears
            disappearMessage = String.format(" Your '%s' vanishes, it must have been magic.", polisher.getDescription());
            junkRoom.moveCollectableFromPlayerInventory(polisher.getCollectableId(), player.inventory());
        }

        return LastAction.createSuccess(String.format("%s %s", polishResults, disappearMessage));
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

    @Override
    public VerbPolishHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }
}
