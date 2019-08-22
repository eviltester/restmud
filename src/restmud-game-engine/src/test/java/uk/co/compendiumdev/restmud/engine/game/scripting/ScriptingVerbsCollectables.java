package uk.co.compendiumdev.restmud.engine.game.scripting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses.TeleportCollectableToLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;

public class ScriptingVerbsCollectables {

    private static MudGame game;

    class simpleWrittenGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {
            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1","The room of failure", "the room of failure", "S:2");

            create.collectable("shiny1", "A shiny Thing", "1");

            create.location("2", "Room 2", "Desc of room 2", "N:1") ;

            game.initFromDefinition(defn);

        }
    }
        @Before
     public void setupTheGame(){
        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.generate(new simpleWrittenGame());

        theGameInit.addDefaultUser("The Test User", "tester", "aPassword");

            // set the user back to the central room after each path
            game.getCommandProcessor().wizardCommands().teleportUserTo("tester", "1");


        }

    @Test
    public void canMoveCollectable(){

        MudCollectable thing = game.getGameCollectables().get("shiny1");

        MudLocation location1 = game.getGameLocations().get("1");
        MudLocation location2 = game.getGameLocations().get("2");

        Assert.assertTrue(location1.collectables().contains("shiny1"));
        Assert.assertFalse(location2.collectables().contains("shiny1"));

        new TeleportCollectableToLocation(game).
                moveCollectableToLocation(
                        thing, game.getGameLocations().get("2"));

        Assert.assertFalse(location1.collectables().contains("shiny1"));
        Assert.assertTrue(location2.collectables().contains("shiny1"));


    }


}
