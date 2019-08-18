package uk.co.compendiumdev.restmud.engine.game.scripting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameEngineTestDSL;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.locations.GateDirection;
import uk.co.compendiumdev.restmud.engine.game.locations.GateStatus;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;

/**
 * Created by alan on 21/08/2016.
 */
public class FailGateScriptingTest {

    private static MudGame game;
    private static GameEngineTestDSL dsl;

    class poorlyWrittenGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {

            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1","The room of failure", "the room of failure", "");
            create.location("22","The room of gate", "the room of failure", "");
            create.location("12","The room end of gate", "the room of failure", "");

            defn.addGate("secretGate22", "22", "e", "12", GateDirection.ONE_WAY, GateStatus.CLOSED).gateIsHidden(true);

            defn.addVerb("fail1");

            create.verbCondition("fail1").
                    andWhen(When.NOUNPHRASE_EQUALS,"boo").
                    then(Then.SHOW_GATE,"secretGateNotHere"). // can only show named gates
                    then(Then.LAST_ACTION_SUCCESS,"You say boo and are aware of more stuff.");

            defn.addVerb("fail2");
            create.verbCondition("fail2").
                    andWhen(When.NOUNPHRASE_EQUALS,"boo").
                    then(Then.SHOW_GATE,"secretGateNotHere"). // can only show named gates
                    then(Then.LAST_ACTION_SUCCESS,"You say boo and are aware of more stuff.");

            game.initFromDefinition(defn);

        }
    }
        @Before
     public void setupTheGame(){
        GameInitializer theGameInit = new GameInitializer();

        // the game should be empty at this point
        game = theGameInit.getGame();
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.generate(new poorlyWrittenGame());

        // generate does not add any users
        Assert.assertEquals(0, game.getUserManager().numberOfUsers());

        theGameInit.addDefaultUser("The Test User", "tester", "aPassword");

        game.teleportUserTo("tester", "1"); // set the user back to the central room after each path

        dsl = new GameEngineTestDSL(game);

    }
    // fail to open gate because of wrong name

    @Test
    public void failToOpenGateBecauseOfWrongNameInScript(){

        game.teleportUserTo("tester","1");
        dsl.failTo(dsl.doVerb("tester","fail1", "boo"));

        // TODO: bug - too many last actions shown
        // TODO: remove the need for this test by checking that all gates exist in rules during game setup
    }

    // fail to close gate because of wrong name
    @Test
    public void failToCloseGateBecauseOfWrongNameInScript(){


        game.teleportUserTo("tester","1");
        dsl.failTo(dsl.doVerb("tester","fail2", "boo"));

        // TODO: bug - too many last actions shown the processing of the verb cond should fail
        // TODO: remove the need for this test by checking that all gates exist in rules during game setup
    }

}
