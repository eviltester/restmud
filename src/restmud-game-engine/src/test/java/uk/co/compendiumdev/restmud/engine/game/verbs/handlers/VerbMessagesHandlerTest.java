package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.InventoryReport;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

public class VerbMessagesHandlerTest {

    private static MudGame game;
    private static MudUser player;

    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1", "Room 1", "the room to the left", "");

            game.initFromDefinition(defn);

        }
    }

    @Before
    public void setupTheGame(){
        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.generate(new VerbHandlerGame());

        // generate does not add any users
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addDefaultUser("The Test User", "tester", "aPassword");

        // set the user back to the central room after each path
        game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1");

        player = game.getUserManager().getUser("tester");

    }

    @Test
    public void canSeeAnyNewMessages(){

        // consume default messages
        game.broadcastMessages().clear();

        player.updateLastActionTimeStamp();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // send a message
        game.getCommandProcessor().wizardCommands().broadcast("Hello Everybody");

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(player.getLastActionTimeStamp());

        // receive the message
        VerbMessagesHandler messages = new VerbMessagesHandler().setGame(game);
        LastAction action = messages.doVerb(player, "");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals(1, action.getGameMessages().messages.size());
        Assert.assertEquals("The Wizard Says: Hello Everybody", action.getGameMessages().messages.get(0).message);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // wait a little because messages uses milliseconds to check if time has advanced
        System.out.println(player.getLastActionTimeStamp());

        // no new messages since last checked
        messages = new VerbMessagesHandler().setGame(game);
        action = messages.doVerb(player, "");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals(0, action.getGameMessages().messages.size());
        System.out.println(player.getLastActionTimeStamp());
    }



    @Test
    public void verbHandlerConditions(){

        VerbMessagesHandler handler = new VerbMessagesHandler().setGame(game).usingCurrentVerb("messages");
        Assert.assertFalse("Messages takes no time",
                handler.actionUpdatesTimeStamp());
        Assert.assertFalse("include no messages",
                handler.shouldAddGameMessages());
        Assert.assertFalse(
                "no need to look",
                handler.shouldLookAfterVerb());
    }
}
