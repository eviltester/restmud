package uk.co.compendiumdev.restmud.engine.game.scripting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.locations.GateDirection;
import uk.co.compendiumdev.restmud.engine.game.locations.GateStatus;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;

/**
 * Created by alan on 21/08/2016.
 */
public class FailTokenisationTest {

    private static MudGame game;

    class poorlyWrittenGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {
            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1","The room of failure", "the room of failure", "");

            defn.addGate("secretGate22", "22", "e", "12", GateDirection.ONE_WAY, GateStatus.CLOSED).gateIsHidden(true);

            defn.addVerb("fail1");

            create.verbCondition("fail1").
                    andWhen(When.NOUNPHRASE_EQUALS,"boo").
                    then("thisthencommanddoesnotexist","secretGateNotHere"); // can only show named gates

            game.initFromDefinition(defn);

        }
    }
        @Before
     public void setupTheGame() {


        }

    // test should fail because we create a then with wrong name
    @Test(expected = RuntimeException.class)
    public void tokenisationErrorOnThenCommand(){

        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.generate(new poorlyWrittenGame());



    }

}
