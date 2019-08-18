package uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions;

import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;


/*

This uses a json definition and generates it into the game

 */
public class FromDefinitionGameGenerator implements GameGenerator {

    private final MudGameDefinition defn;

    public FromDefinitionGameGenerator(MudGameDefinition defn){
        this.defn = defn;
    }

    @Override
    public void generateInto(MudGame game) {

        game.initFromDefinition(defn);

    }


}
