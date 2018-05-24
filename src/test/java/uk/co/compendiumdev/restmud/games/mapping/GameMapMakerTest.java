package uk.co.compendiumdev.restmud.games.mapping;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.locations.Directions;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.VisibleExit;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import uk.co.compendiumdev.restmud.testing.dsl.GameTestDSL;

import java.util.List;

/**
 * Created by Alan on 20/01/2017.
 */
public class GameMapMakerTest {

    private static MudGame game;
    private static GameTestDSL dsl;

    /* Example Documented Game is deterministic and well documented - it should be used to automate gameplay paths */

    @BeforeClass
    static public void setupTheGame(){
        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.generate("example_game");

        // generate does not add any users
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addDefaultUser("The Test User", "tester", "aPassword");

        game.teleportUserTo("tester", "1"); // set the user back to the central room after each path

        dsl = new GameTestDSL(game);

    }

    @Ignore("This does create a basic graph of visible exits but layout is pretty poor")
    @Test
    public void makeMapForVisibleGameExits(){
        System.out.println("digraph game {");
        System.out.println("node [shape = square];");

        for( IdDescriptionPair pair : game.getGameLocations().getLocationsAsIdDescriptionPairs()){
            System.out.println(String.format("%s [label = \"%s\"];", pair.id, pair.description.replaceAll("\"", "'").replaceAll(" ", "\\\\n")));
        }

        for( String id : game.getGameLocations().getLocationKeys()){
            MudLocation location = game.getGameLocations().get(id);
            List<VisibleExit> exits = location.getVisibleExits();

            for( VisibleExit exit : exits){
                String direction = exit.direction;
                String goesTo = location.destinationFor(direction);

                System.out.println(String.format("%s:%s -> %s:%s [ label = \"%s\"];", id, direction.toLowerCase(), goesTo, Directions.findOppositeDirection(direction),direction.toUpperCase()));

            }
        }
        System.out.println("}");
    }
}
