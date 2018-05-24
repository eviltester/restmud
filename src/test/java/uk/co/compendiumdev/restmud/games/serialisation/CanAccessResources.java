package uk.co.compendiumdev.restmud.games.serialisation;

import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;

import java.util.List;

public class CanAccessResources {

    @Test
    public void canGetListOfBuiltInGames(){

        MudGameDefinitionSerialiser serializer = new MudGameDefinitionSerialiser();
        List<String> games = serializer.getListOfBuiltInGames();

        Assert.assertEquals("Have only created one built in game", 1, games.size());
        Assert.assertEquals("name is not default length", "defaultenginegame.json".length(), games.get(0).length());
        Assert.assertEquals("name is not default", "defaultenginegame.json", games.get(0));
    }

    @Test
    public void canGetGamesAsAttributePairs(){
        MudGameDefinitionSerialiser serializer = new MudGameDefinitionSerialiser();
        List<IdDescriptionPair> games = serializer.getBuiltInGamesAsIdDescriptionPairs();

        Assert.assertEquals(1, games.size());
        Assert.assertEquals("defaultenginegame.json", games.get(0).id);
        Assert.assertEquals("/games/defaultenginegame.json", games.get(0).description);
    }
}
