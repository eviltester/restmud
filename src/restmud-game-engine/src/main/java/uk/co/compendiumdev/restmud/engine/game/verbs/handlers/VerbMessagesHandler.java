package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;


import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

public class VerbMessagesHandler implements VerbHandler{
    private MudGame game;
    private String verbName;

    @Override
    public VerbMessagesHandler setGame(MudGame mudGame) {
        this.game = mudGame;
        return this;
    }

    @Override
    public LastAction doVerb(MudUser player, String nounPhrase) {
        LastAction action = LastAction.createSuccess("You check your messages");

        action.setGameMessages(game.broadcastMessages().getMessagesSince(player.getLastActionTimeStamp()));

        player.updateLastActionTimeStamp();

        return action;
    }

    @Override
    public boolean shouldAddGameMessages() {
        return false;
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
    public VerbMessagesHandler usingCurrentVerb(final String actualVerbName) {
        verbName = actualVerbName;
        return this;
    }
}
