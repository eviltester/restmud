package uk.co.compendiumdev.restmud.engine.game.verbs.handlers.perPlayer;

import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

/**
 * Created by Alan on 01/06/2016.
 */
public class VerbDarken {
    private final MudUser player;

    public VerbDarken(MudUser player) {
        this.player = player;
    }

    public LastAction using(MudCollectable torch) {
        torch.setAbilityOn(false);

        String results =
                String.format("Good work. You extinguished the '%s'. Your '%s' has %d power left.",
                        torch.getDescription(), torch.getDescription(), torch.getAbilityPower());

        return LastAction.createSuccess(results);
    }
}
