package uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions;

import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.scripting.PriorityTurnCondition;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;
import uk.co.compendiumdev.restmud.engine.game.scripting.VerbCondition;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.Then;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.When;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectableTemplate;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;
import uk.co.compendiumdev.restmud.engine.game.verbs.VerbGameAbilities;


public class DefaultGameDefinitionGenerator {
    public static void define(MudGameDefinition defn) {
        VerbCondition cond;
        PriorityTurnCondition turn = null;
        MudLocationObject locationObj;
        MudLocation objectLocation = null;

        int total_score;




        defn.setGameName("Default Test Game");
        defn.setWelcomeMessage("Welcome to the Very Basic In Built Default Test Game");

        defn.gameLocations().addLocation(new MudLocation("0","The Junk Room", "This is the room of Junk", "S:1"));


        cond = new VerbCondition("go");
        cond.when("direction", "s").
                andWhen("locationId","0").
                then("message", "You are glad to get out of there, that room stunk.").
                because("can create conditions for normal exits");

        defn.addCondition(cond);



        MudLocation startLocation = new MudLocation("1","The Central Cross Roads",
                "This is the room in the center of the test game", "N:2,E:3,S:4,W:5").setCanHoardTreasureHere(true);

        defn.gameLocations().addLocation(startLocation);
        defn.setStartLocationId("1");


        locationObj = new MudLocationObject("ahint","A sign on the wall", "You cannot really do much in this game, ask the wiz to load another game");
        defn.addLocationObjectIn(locationObj, "1");

        defn.gameLocations().addLocation(new MudLocation("2","The room in the north", "This room is in the North", "S:1"));
        defn.gameLocations().addLocation(new MudLocation("3","The East Room", "This is the room in the east", "W:1"));
        defn.gameLocations().addLocation(new MudLocation("4","The South Room", "This is the room in the south", "N:1"));
        defn.gameLocations().addLocation(new MudLocation("5","The West Room", "This is the room in the west", "E:1,W:6"));
        defn.gameLocations().addLocation(new MudLocation("6","The West of West Room", "This is the room in the most west", "E:5").
                makeDark());

        // add a gem in location 2
        defn.addCollectable(new MudCollectable("agem", "A shiny gem"), defn.gameLocations().get("2"));

        // create a torch Dispenser

        locationObj = new MudLocationObject("torchdispenser","A Torch Dispenser", "Use the dispenser to get a torch.");
        locationObj.setIsADispenser(true);
        locationObj.setDispenserTemplate(MudCollectableTemplate.getDispenserTemplate("torch", "A Dispensed Torch", VerbGameAbilities.ILLUMINATE_DARKEN, 4, 20,true));
        defn.addLocationObjectIn(locationObj, "3");

        locationObj = defn.creator().dispenser("6","golddispenser","A Gold Dispenser", "Use the dispenser to get gold.");
        locationObj.setDispenserTemplate(MudCollectableTemplate.getDispenserTemplate("gold", "A Gold Nugget").
                setHoardableAttributes(true, 10, 100));

        defn.addStartupRule(Then.RANDOMLY_GENERATE_HOARDS, "3");
        defn.addStartupRule(Then.RANDOMLY_GENERATE_HOARDABLE_TREASURE, "15");
        defn.addStartupRule(Then.RANDOMLY_GENERATE_NONHOARDABLE_TREASURES, "15");
        defn.addStartupRule(Then.RANDOMLY_GENERATE_HOARDABLE_JUNK, "15");
        defn.addStartupRule(Then.RANDOMLY_GENERATE_NON_HOARDABLE_JUNK, "15");


        // allow creating new verbs as part of verb condition creation
        cond = new VerbCondition("levitate");
        cond.when(
                    ScriptClause.and(
                            ScriptClause.when(
                                    When.NOUNPHRASE_EQUALS, "self"),
                            ScriptClause.when("locationId","1")
                    )).
                then("message", "You levitate.").
                andThen("lastAction.success", "It did, you levitated. It worked.").
                because("can add verbs when creating verb conditions");
        defn.addCondition(cond);

        cond = new VerbCondition("levitate");
        cond.when(ScriptClause.or(new ScriptClause(When.NOUNPHRASE_EQUALS, "this"),
                                  new ScriptClause(When.NOUNPHRASE_EQUALS, "that"))).
                andWhen("locationId","1").
                then("message", "This levitates.").
                andThen("lastAction.success", "It did, it levitated. It worked.").
                because("can add verbs when creating verb conditions");

        defn.addCondition(cond);

        total_score=1000;

        defn.setTotalScore(total_score);


    }
}
