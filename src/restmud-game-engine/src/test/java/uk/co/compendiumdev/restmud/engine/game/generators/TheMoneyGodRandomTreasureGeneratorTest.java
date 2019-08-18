package uk.co.compendiumdev.restmud.engine.game.generators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;

import java.util.ArrayList;
import java.util.List;

public class TheMoneyGodRandomTreasureGeneratorTest {

    int oldCollectablesCount = 0;
    MudGame game;
    int oldScore;

    @Before
    public void setupTheGame(){

        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        game = theGameInit.getGame();
        theGameInit.generate("defaultenginegame.json");

        oldScore = game.getTotalScore();
    }

    @Test
    public void canAddHoardableTreasuresToAnyGame(){




        // create a money god
        RandomTreasureGenerator moneyGod = new RandomTreasureGenerator(game);

        // I pray oh money god, create treasure that will boost my score


        oldCollectablesCount = game.getGameCollectables().count();

        List<String> treasures = moneyGod.createHoardableTreasures(10);


        // did we create as many as we wanted?
        Assert.assertEquals(oldCollectablesCount+10, game.getGameCollectables().count());

        // They are all hoardable
        for(String treasureKey : treasures){
            Assert.assertTrue(game.getGameCollectables().get(treasureKey).isHoardable());
        }

        // did score update correclty?
        int treasureScore = 0;
        for(String treasureKey : treasures){
            treasureScore += game.getGameCollectables().get(treasureKey).getHoardableScore();
        }
        Assert.assertEquals(treasureScore + oldScore, game.getTotalScore());
    }


    @Test
    public void canAddNonHoardableTreasuresToAnyGameToFrustrateUsers(){



        // create a money god
        RandomTreasureGenerator moneyGod = new RandomTreasureGenerator(game);

        // I pray oh money god, create treasures that will frustrate players

        oldCollectablesCount = game.getGameCollectables().count();

        List<String> treasures = moneyGod.createNonHoardableTreasures(12);


        // did we create as many as we wanted?
        Assert.assertEquals(oldCollectablesCount+12, game.getGameCollectables().count());

        // They are not hoardable
        for(String treasureKey : treasures){
            Assert.assertFalse(game.getGameCollectables().get(treasureKey).isHoardable());
        }

        // did score remain the same
        Assert.assertEquals(oldScore, game.getTotalScore());
    }


    @Test
    public void canAddHoardableJunkToAnyGameToFrustrateUsers(){


        // create a money god
        RandomTreasureGenerator moneyGod = new RandomTreasureGenerator(game);

        // I pray oh money god, create cursed treasures that will frustrate players

        oldCollectablesCount = game.getGameCollectables().count();

        List<String> treasures = moneyGod.createHoardableJunk(13);

        // did we create as many as we wanted?
        Assert.assertEquals(oldCollectablesCount+13, game.getGameCollectables().count());

        // They are hoardable
        for(String treasureKey : treasures){
            Assert.assertTrue(game.getGameCollectables().get(treasureKey).isHoardable());
        }

        // did score remain the same? because these should not be counted in total score
        Assert.assertEquals(oldScore, game.getTotalScore());
    }

    @Test
    public void canAddNonHoardableJunkToAnyGameToFrustrateUsers(){


        // create a money god
        RandomTreasureGenerator moneyGod = new RandomTreasureGenerator(game);

        // I pray oh money god, create cursed treasures that will frustrate players

        oldCollectablesCount = game.getGameCollectables().count();

        List<String> treasures = moneyGod.createNonHoardableJunk(3);

        // did we create as many as we wanted?
        Assert.assertEquals(oldCollectablesCount+3, game.getGameCollectables().count());

        // They are not hoardable
        for(String treasureKey : treasures){
            Assert.assertFalse(game.getGameCollectables().get(treasureKey).isHoardable());
        }

        // did score remain the same? because these should not be counted in total score
        Assert.assertEquals(oldScore, game.getTotalScore());
    }


    @Test
    public void canAddHoardableTreasureToASubsetOfLocations(){

        GameInitializer theGameInit = new GameInitializer();

        int oldCollectablesCount = 0;

        // the game should be empty at this point
        MudGame game = theGameInit.getGame();


        game.getGameLocations().addLocation(new MudLocation("1", "location 1", "this is location 1", ""));
        game.getGameLocations().addLocation(new MudLocation("2", "location 2", "this is location 2", ""));
        game.getGameLocations().addLocation(new MudLocation("3", "location 3", "this is location 3", ""));
        game.getGameLocations().addLocation(new MudLocation("4", "location 4", "this is location 4", ""));

        List<String> locationKeys = new ArrayList<String>();
        locationKeys.add("2");
        locationKeys.add("4");

        // create a money god
        RandomTreasureGenerator moneyGod = new RandomTreasureGenerator(game);
        moneyGod.generateIntoTheseLocations(locationKeys);
        List<String> treasures = moneyGod.createNonHoardableTreasures(300);

        Assert.assertEquals(0, game.getGameLocations().get("1").collectables().itemsView().size());
        Assert.assertEquals(0, game.getGameLocations().get("3").collectables().itemsView().size());


        int from2 = game.getGameLocations().get("2").collectables().itemsView().size();
        int from4 = game.getGameLocations().get("4").collectables().itemsView().size();

        Assert.assertEquals(300, from2 + from4);

        // because allocation is random this might not be true, all might be in
        // one location, but that's why we created 300, because it would be incredibly rare
        // for that situation to occur and it probably means something is wrong if it did
        Assert.assertTrue(game.getGameLocations().get("2").collectables().itemsView().size()>0);
        Assert.assertTrue(game.getGameLocations().get("4").collectables().itemsView().size()>0);


    }

}
