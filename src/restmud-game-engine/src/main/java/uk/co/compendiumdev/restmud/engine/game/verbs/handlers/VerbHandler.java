package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;


import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public interface VerbHandler {

    VerbHandler setGame(MudGame mudGame);
    LastAction doVerb(MudUser player, String nounPhrase);

    boolean shouldAddGameMessages();

    boolean shouldLookAfterVerb();

    boolean actionUpdatesTimeStamp();

    VerbHandler usingCurrentVerb(String actualVerbName);
}
