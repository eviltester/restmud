package uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions;

import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;


/*

This is the game released as the single player demo game. With the maze etc.


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
