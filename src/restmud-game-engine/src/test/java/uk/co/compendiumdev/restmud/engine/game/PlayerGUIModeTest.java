package uk.co.compendiumdev.restmud.engine.game;

import org.junit.Assert;
import org.junit.Test;

public class PlayerGUIModeTest {

    @Test
    public void canReturnAModeBasedOnText(){

        Assert.assertEquals(PlayerGuiMode.SUPER_EASY, PlayerGuiMode.getMode("super"));
        Assert.assertEquals(PlayerGuiMode.NORMAL, PlayerGuiMode.getMode("normal"));
        Assert.assertEquals(PlayerGuiMode.EASY, PlayerGuiMode.getMode("easy"));

        // by default if we don't knonw what it is then it is easy
        Assert.assertEquals(PlayerGuiMode.EASY, PlayerGuiMode.getMode("impossible to play"));

    }
}
