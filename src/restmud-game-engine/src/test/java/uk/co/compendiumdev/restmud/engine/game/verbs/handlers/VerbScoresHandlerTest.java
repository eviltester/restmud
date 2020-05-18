package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.VerbScoresHandler;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;

import java.util.HashMap;
import java.util.Map;

public class VerbScoresHandlerTest {

    private static MudGame game;
    private static MudUser player;
    private MudUser player2;

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
        theGameInit.addDefaultUser("Player 2", "player2", "aPassword");

        // set the user back to the central room after each path
        game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1");
        game.getCommandProcessor().wizardCommands().teleportUserTo("player2", "1");

        player = game.getUserManager().getUser("tester");
        player2 = game.getUserManager().getUser("player2");

    }

    @Test
    public void canGetUpToDateScores(){

        // receive the message
        VerbScoresHandler scores = new VerbScoresHandler().setGame(game);
        LastAction action = scores.doVerb(player, "");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals(2, action.getUserDetailsList().size());
        Assert.assertEquals(0, action.getUserDetailsList().get(0).score.intValue());
        Assert.assertEquals(0, action.getUserDetailsList().get(1).score.intValue());

        Map<String,Integer> myscores= new HashMap<>();
        myscores.put("tester", Integer.valueOf(22));
        myscores.put("player2", Integer.valueOf(22));
        player.setScore(myscores.get("tester"));
        player2.setScore(myscores.get("player2"));

        action = scores.doVerb(player, "");
        Assert.assertTrue(action.isSuccess());
        Assert.assertEquals(2, action.getUserDetailsList().size());

        String name = action.getUserDetailsList().get(0).userName;
        Assert.assertEquals(myscores.get(name).intValue(), action.getUserDetailsList().get(0).score.intValue());
        name = action.getUserDetailsList().get(1).userName;
        Assert.assertEquals(myscores.get(name).intValue(), action.getUserDetailsList().get(1).score.intValue());



    }



    @Test
    public void verbHandlerConditions(){

        VerbScoresHandler handler = new VerbScoresHandler().setGame(game).usingCurrentVerb("scores");
        Assert.assertFalse("Scores takes no time",
                handler.actionUpdatesTimeStamp());
        Assert.assertFalse("Scores includes no messages",
                handler.shouldAddGameMessages());
        Assert.assertFalse(
                "no need to look after scores",
                handler.shouldLookAfterVerb());
    }
}
