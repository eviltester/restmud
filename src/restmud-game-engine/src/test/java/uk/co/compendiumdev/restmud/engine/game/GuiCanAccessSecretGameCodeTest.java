package uk.co.compendiumdev.restmud.engine.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;

public class GuiCanAccessSecretGameCodeTest {

    private MudGame game;

    @Before
    public void generateTheGame(){
        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        game = theGameInit.getGame();
        theGameInit.generate("defaultenginegame.json");
    }

    @Test
    public void guiCanCheckIfSecretGameCodePlayerEnteredIsValid(){

        game.getSecretGameRegistrationCode().set("bob");
        Assert.assertEquals("bob", game.getSecretGameRegistrationCode().get());

        Assert.assertFalse(game.getSecretGameRegistrationCode().isSecretCodeValid(null));
        Assert.assertFalse(game.getSecretGameRegistrationCode().isSecretCodeValid(""));
        Assert.assertFalse(game.getSecretGameRegistrationCode().isSecretCodeValid("bobby"));
        Assert.assertFalse(game.getSecretGameRegistrationCode().isSecretCodeValid("bo"));
        Assert.assertTrue(game.getSecretGameRegistrationCode().isSecretCodeValid("bob"));

    }

    @Test
    public void disallowANullSecretGameCode(){

        game.getSecretGameRegistrationCode().set(null);

        Assert.assertFalse(game.getSecretGameRegistrationCode().isSecretCodeValid(null));
        Assert.assertFalse(game.getSecretGameRegistrationCode().isSecretCodeValid(""));

    }
}
