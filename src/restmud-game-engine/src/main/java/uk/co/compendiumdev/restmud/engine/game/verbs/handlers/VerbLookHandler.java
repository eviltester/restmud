package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationDirectionGate;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;
import uk.co.compendiumdev.restmud.engine.game.verbs.primitive.LookPrimitive;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.*;

import java.util.ArrayList;
import java.util.List;

public class VerbLookHandler   implements VerbHandler {

    private String verbName="look";
    private MudGame game;

    @Override
    public VerbLookHandler setGame(MudGame mudGame) {
        game = mudGame;
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {

        // TODO: add the ability to look e, etc.

        MudLocation whereAmI = game.getGameLocations().get(player.getLocationId());
        final List<MudLocationDirectionGate> gates = game.getGateManager().getGatesHere(whereAmI);

        return LastAction.createSuccess(String.format("You %s.", verbName)).
                    setLookOutput(
                            look(player, whereAmI, gates, game.getUserManager().getUsers()));
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

    private LookResultOutput look(MudUser player, MudLocation mudLocation, List<MudLocationDirectionGate> gatesHere, List<MudUser> otherUsers) {

        return new LookPrimitive().perform(player, mudLocation, gatesHere, otherUsers);

    }

    @Override
    public VerbLookHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }
}
