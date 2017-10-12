package uk.co.compendiumdev.restmud.gamedata;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import org.junit.Assert;
import org.junit.Test;

public class BasicGameInitializerTest {


    @Test
    public void canGenerateAGameBasedOnHardCodedName(){

        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        MudGame game = null;

        game = theGameInit.getGame();

        Assert.assertEquals(0, game.getTotalScore());
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());
        Assert.assertEquals(0, game.getGameLocations().getLocationKeys().size());

        theGameInit.generate("defaultenginegame.json");

        Assert.assertTrue(game.getTotalScore() > 0);
        Assert.assertTrue(game.getGameLocations().getLocationKeys().size() > 0);
        // generate does not add any users
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());
    }

    @Test
    public void canAddAUserToGame(){

        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        MudGame game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.generate("defaultenginegame.json");

        // generate does not add any users
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addDefaultUser("The Test User", "tester", "aPassword");

        MudUser aUser = game.getUserManager().getUser("tester");

        Assert.assertEquals("The Test User", aUser.displayName());
        Assert.assertEquals("aPassword", aUser.getPassword());
        Assert.assertEquals("tester", aUser.userName());
    }


    @Test
    public void canAddAListOfUsersToGameFromAString(){

        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        MudGame game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.generate("defaultenginegame.json");

        theGameInit.addUsersFromString(null);
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addUsersFromString("");
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());


        theGameInit.addUsersFromString("The Mighty Alan|Bob The Mighty,bob|Me The Groovy ,groovy, letmein ");
        Assert.assertEquals(3, game.getUserManager().numberOfUsers());


        MudUser aUser = game.getUserManager().getUser("TheMightyAlan");

        Assert.assertEquals("The Mighty Alan", aUser.displayName());
        Assert.assertEquals("password", aUser.getPassword());

        // usernames should be lowercased
        Assert.assertEquals("themightyalan", aUser.userName());

        aUser = game.getUserManager().getUser("bob");

        Assert.assertEquals("Bob The Mighty", aUser.displayName());
        Assert.assertEquals("password", aUser.getPassword());
        Assert.assertEquals("bob", aUser.userName());

        aUser = game.getUserManager().getUser("groovy");

        Assert.assertEquals("Me The Groovy", aUser.displayName());
        Assert.assertEquals("letmein", aUser.getPassword());
        Assert.assertEquals("groovy", aUser.userName());

    }


    @Test
    public void canCreateAWizardInTheGame() {

        GameInitializer theGameInit = new GameInitializer();

        MudGame game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addAWizard();
        Assert.assertEquals(1, game.getUserManager().numberOfUsers());

        MudUser wiz = game.getUserManager().getUser("wiz");

        Assert.assertEquals("Wizard of RestMud", wiz.displayName());
        // password is a random GUI
        Assert.assertTrue(wiz.getPassword().length() > 10);
    }

}
