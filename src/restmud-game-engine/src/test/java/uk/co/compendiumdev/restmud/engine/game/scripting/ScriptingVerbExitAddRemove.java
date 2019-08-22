package uk.co.compendiumdev.restmud.engine.game.scripting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.GameEngineTestDSL;
import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameEntityCreator;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.gamedata.GameInitializer;

public class ScriptingVerbExitAddRemove {

    private static MudGame game;

    class simpleWrittenGame implements GameGenerator {
        @Override
        public void generateInto(MudGame game) {
            MudGameDefinition defn = game.gameDefinition();

            MudGameEntityCreator create = defn.creator();

            create.location("1","The room of failure", "the room of failure", "S:2");

            create.collectable("shiny1", "A shiny Thing", "1");

            create.location("2", "Room 2", "Desc of room 2", "N:1") ;

            defn.addVerb("say"); // custom verb

            create.verbCondition("say").
                    when(When.NOUNPHRASE_EQUALS,"open").
                    andWhen(When.LOCATION_ID_IS,"2").
                    andWhen(When.LOCATION_EXIT_EXISTS, "E", false)
                    .then(Then.CREATE_EXIT, "2:E:1").   // from location 2 go e to location 1
                    andThen(Then.LAST_ACTION_SUCCESS,"An exit to the east appears");

            create.verbCondition("say").
                    when(When.NOUNPHRASE_EQUALS,"open").
                    andWhen(When.LOCATION_ID_IS,"2").
                    andWhen(When.LOCATION_EXIT_EXISTS, "E").
                    andThen(Then.LAST_ACTION_SUCCESS,"I cannot open it now");

            create.verbCondition("say").
                    when(When.NOUNPHRASE_EQUALS,"close").
                    andWhen(When.LOCATION_ID_IS,"2").
                    andWhen(When.LOCATION_EXIT_EXISTS, "E").
                    then(Then.REMOVE_EXIT, "2:E:1").   // from location 2 go e to location 1
                    andThen(Then.LAST_ACTION_SUCCESS,"You can no longer go east from here");

            create.verbCondition("say").
                    when(When.NOUNPHRASE_EQUALS,"close").
                    andWhen(When.LOCATION_ID_IS,"2").
                    andWhenFalse(When.LOCATION_EXIT_EXISTS, "E").
                    andThen(Then.LAST_ACTION_SUCCESS,"Say close you say and nothing happens");

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
    // fail to open gate because of wrong name

    @Test
    public void canCreateAndRemoveExitWithScripting(){

        GameEngineTestDSL dsl = new GameEngineTestDSL(game);
        dsl.successfully(dsl.doVerb("tester", "go", "s"));
        MudUser player = game.getUserManager().getUser("tester");

        Assert.assertEquals("2",player.getLocationId());

        dsl.failTo(dsl.doVerb("tester", "go", "e"));

        dsl.successfully(dsl.doVerb("tester", "say", "open"));
        dsl.successfully(dsl.doVerb("tester", "say", "open"));
        dsl.successfully(dsl.doVerb("tester", "go", "e"));
        Assert.assertEquals("1",player.getLocationId());

        dsl.successfully(dsl.doVerb("tester", "go", "s"));
        Assert.assertEquals("2",player.getLocationId());

        dsl.successfully(dsl.doVerb("tester", "say", "close"));
        dsl.successfully(dsl.doVerb("tester", "say", "close"));
        dsl.failTo(dsl.doVerb("tester", "go", "e"));
        Assert.assertEquals("2",player.getLocationId());

        // CREATE A PLAYER_CAN_GO(direction) when condition
    }



}
