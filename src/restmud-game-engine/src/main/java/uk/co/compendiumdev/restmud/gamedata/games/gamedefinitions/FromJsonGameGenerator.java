package uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions;

import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;


/*

This uses a json definition and generates it into the game

 */
public class FromJsonGameGenerator implements GameGenerator {

    private final String defn;

    public FromJsonGameGenerator(String jsonDefn){
        this.defn = jsonDefn;
    }

    @Override
    public void generateInto(MudGame game) {

        MudGameDefinitionSerialiser loader = new MudGameDefinitionSerialiser();

        MudGameDefinition deserialiseddefn = loader.createDefnFromJson(this.defn);

        game.initFromDefinition(deserialiseddefn);

    }


}
