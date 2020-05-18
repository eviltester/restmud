package uk.co.compendiumdev.restmud.engine.game.verbs.handlers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

public class VerbLookHandlerTest {

    private static MudGame game;
    private static MudUser player;
    MudLocation takeroom;
    MudLocation droproom;
    private MudCollectable thing;

    class VerbHandlerGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            takeroom = create.location("1", "light room", "the room to the left", "e:2").setCanHoardTreasureHere(true);
            droproom = create.location("2","Dark Rom", "the room to the right", "w:1").makeDark();

            thing = create.collectable("athing", "A Thing", "1").canHoard(true,200);
            thing = create.collectable("somejunk", "A Piece of Junk", "1").canHoard(false,100);
            thing = create.collectable("realjunk", "A Real Piece of Junk", "1").canHoard(true,-200);

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
    public void canLook(){

        Assert.assertTrue(game.getGameLocations().get("1").collectables().contains("athing"));

        VerbLookHandler hoard = new VerbLookHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());
        Assert.assertNotNull(action.getLookReport());
    }

    @Test
    public void canLookButItMightBeTooDarkToSee(){

        final ResultOutput p = game.getCommandProcessor().processTheVerbInGame("tester",
                "go", "e", null);
        Assert.assertTrue(p.resultoutput.isSuccess());

        VerbLookHandler hoard = new VerbLookHandler().setGame(game);
        LastAction action = hoard.doVerb(player, "athing");
        Assert.assertTrue(action.isSuccess());
        Assert.assertNotNull(action.getLookReport());
        Assert.assertEquals("It is too dark to see",
                    action.getLookReport().location.description);
    }


    @Test
    public void verbHandlerConditions(){

        VerbLookHandler handler = new VerbLookHandler().setGame(game).usingCurrentVerb("look");
        Assert.assertTrue("looking takes time",
                handler.actionUpdatesTimeStamp());
        Assert.assertTrue("looking generates messages",
                handler.shouldAddGameMessages());
        Assert.assertFalse(
                "looking so no need to look",
                handler.shouldLookAfterVerb());
    }
}
