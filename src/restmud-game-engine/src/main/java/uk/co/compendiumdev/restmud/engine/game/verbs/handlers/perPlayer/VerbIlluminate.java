package uk.co.compendiumdev.restmud.engine.game.verbs.handlers.perPlayer;

import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 01/06/2016.
 */
public class VerbIlluminate {

    public LastAction using(MudCollectable torch) {
        torch.setAbilityOn(true);

        int power = torch.getAbilityPower();

        if(power>0){

            return LastAction.createSuccess(
                                String.format("Good work. You illuminated the '%s'. Your '%s' has %d power left.",
                                            torch.getDescription(), torch.getDescription(), power));
        }else{

            return LastAction.createError(
                        String.format("No, that won't work. Your '%s' has no power left.",
                            torch.getDescription()));

        }


    }
}
