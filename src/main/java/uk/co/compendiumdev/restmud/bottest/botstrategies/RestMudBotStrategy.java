package uk.co.compendiumdev.restmud.bottest.botstrategies;

import uk.co.compendiumdev.restmud.api.RestMudAPI;
import uk.co.compendiumdev.restmud.api.RestMudResponseProcessor;

public interface RestMudBotStrategy {

    public void setApi(RestMudAPI api);
    public RestMudResponseProcessor execute();
}
