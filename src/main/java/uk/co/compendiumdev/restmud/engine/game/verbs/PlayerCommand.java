package uk.co.compendiumdev.restmud.engine.game.verbs;

import uk.co.compendiumdev.restmud.engine.game.RestMudHttpRequestDetails;
import uk.co.compendiumdev.restmud.engine.game.parser.VerbToken;

public class PlayerCommand {
    private final String verbToHandle;
    private final VerbToken verbToken;
    private final String nounPhrase;
    private final RestMudHttpRequestDetails httpRequestDetails;

    public PlayerCommand(String verbToHandle, VerbToken verbToken, String nounPhrase, RestMudHttpRequestDetails httpRequestDetails) {
        this.verbToHandle = verbToHandle;
        this.verbToken = verbToken;
        this.nounPhrase = nounPhrase;
        this.httpRequestDetails = httpRequestDetails;
    }

    public String getVerbToHandle() {
        return verbToHandle.toLowerCase();
    }

    public VerbToken getVerbToken() {
        return verbToken;
    }

    public String getNounPhrase() {
        return nounPhrase.toLowerCase();
    }

    public RestMudHttpRequestDetails getHttpDetails() {
        return httpRequestDetails;
    }
}
