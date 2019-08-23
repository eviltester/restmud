package uk.co.compendiumdev.restmud.engine.game;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alan on 25/07/2016.
 */
public class RestMudHttpRequestDetails {
    public final Set<String> headers;
    public final String httpverb;

    public RestMudHttpRequestDetails(Set<String> request_headers, String requestMethod) {

        headers = new HashSet<String>(request_headers);
        httpverb = requestMethod;
    }

    public RestMudHttpRequestDetails(String requestMethod) {

        headers = new HashSet<String>();
        httpverb = requestMethod;
    }

    public static RestMudHttpRequestDetails EMPTY(){
        return new RestMudHttpRequestDetails("");
    }
}
