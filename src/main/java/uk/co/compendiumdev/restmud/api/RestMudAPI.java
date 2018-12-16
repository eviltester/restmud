package uk.co.compendiumdev.restmud.api;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple API written using JSoup
 *
 */
public class RestMudAPI {
    private final ChangeCaseifier changeCase;
    private Connection connection;
    private final String mainUrl;
    private int getRequestCount;
    private int postRequestCount;
    private URL restMudURL = null;
    private String forUser;
    private String password;
    private String registrationSecretCode;
    private boolean registrationNeed;
    private Map<Integer, Integer> statusCodesCount = new HashMap<>();
    private int errorsCount;
    private RestMudResponseProcessor lastResult;
    private int score;
    private int deleteRequestCount;
    private long rateLimitingMillis;
    private long lastRequestSentAt;
    private String proxyhost;
    private int proxyport;

    public RestMudAPI(String restmudUrl) {
        this.registrationNeed = false;  //assume we do not need to register
        this.mainUrl = restmudUrl;
        try {
            this.restMudURL = new URL(restmudUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.getRequestCount = 0;
        this.postRequestCount = 0;
        this.deleteRequestCount=0;
        errorsCount = 0;
        refreshConnection();
        changeCase = new ChangeCaseifier();
        addRateLimitingTo(0);

    }

    public String getUserName() {
        return this.forUser;
    }

    public void setProxy(String host, int port){
        this.proxyhost = host;
        this.proxyport = port;
        refreshConnection();
    }

    public void clearProxy(){
        this.proxyhost=null;
        refreshConnection();
    }

    public void refreshConnection() {

        if(proxyhost==null) {
            this.connection = Jsoup.connect(this.mainUrl).ignoreContentType(true). // for API it is application/json
                    ignoreHttpErrors(true);                  // ignore errors so we don't get an exception if 400 500 etc.
        }else{
            this.connection = Jsoup.connect(this.mainUrl).proxy(proxyhost, proxyport).ignoreContentType(true). // for API it is application/json
                    ignoreHttpErrors(true);
        }
        setBasicAuthHeader();
    }

    public RestMudAPI setForUser(String forUser) {
        this.forUser = forUser;
        if (this.forUser != null && this.password != null) {
            setBasicAuthHeader();
        }
        return this;
    }

    public RestMudAPI setPassword(String password) {
        this.password = password;
        if (this.forUser != null && this.password != null) {
            setBasicAuthHeader();
        }
        return this;
    }

    public void setBasicAuthHeader() {
        if (this.forUser == null || this.password == null)
            return;

        String basicAuth = this.forUser + ":" + this.password;

        //encode using base64 from Java 1.8
        String encoded =
                new String(Base64.getEncoder().encodeToString(basicAuth.getBytes()));

        connection.header("Authorization", "Basic " + encoded);

    }


    String getApiPlayerUrl() {
        String theUserName = this.forUser;
        //mutation testing for uppercase lowercase handling testing
        //theUserName = changeCase.randomlyChangeCaseOf(theUserName);
        return String.format("/api/player/%s", theUserName);
    }

    private String getApiVerbUrl(String verb) {
        String theVerb = verb;
        //mutation testing for uppercase lowercase handling testing
        //theVerb = changeCase.randomlyChangeCaseOf(theVerb);
        return getApiPlayerUrl() + "/" + theVerb;
    }

    private String getNounUrlFor(String noun, String verbUrl) {
        String theNoun = noun;
        //mutation testing for uppercase lowercase handling testing
        //theNoun = changeCase.randomlyChangeCaseOf(theNoun);
        return verbUrl + "/" + theNoun;
    }


    private RestMudResponseProcessor makeApiCallFor(String verb) {
        return http_GET_Request(getApiVerbUrl(verb));
    }

    public RestMudResponseProcessor makeApiCallFor(String verb, String noun) {
        return http_GET_Request(getNounUrlFor(noun, getApiVerbUrl(verb)));
    }

    public RestMudResponseProcessor go(String direction) {
        return makeApiCallFor("go", direction);
    }

    public RestMudResponseProcessor open(String direction) {
        return makeApiCallFor("open", direction);
    }

    public RestMudResponseProcessor close(String direction) {

        return makeApiCallFor("close", direction);
    }

    public RestMudResponseProcessor examine(String noun) {
        return makeApiCallFor("examine", noun);
    }

    public RestMudResponseProcessor use(String noun) {
        return makeApiCallFor("use", noun);
    }

    public RestMudResponseProcessor take(String noun) {
        return makeApiCallFor("take", noun);
    }

    public RestMudResponseProcessor drop(String noun) {
        return makeApiCallFor("drop", noun);
    }

    public RestMudResponseProcessor hoard(String noun) {
        return makeApiCallFor("hoard", noun);
    }

    public RestMudResponseProcessor inspect(String noun) {
        return makeApiCallFor("inspect", noun);
    }

    public RestMudResponseProcessor polish(String noun) {
        return makeApiCallFor("polish", noun);
    }

    public RestMudResponseProcessor look() {
        return makeApiCallFor("look");
    }

    public RestMudResponseProcessor score() {
        return makeApiCallFor("score");
    }

    public RestMudResponseProcessor inventory() {
        return makeApiCallFor("inventory");
    }

    public RestMudResponseProcessor illuminate() {
        return makeApiCallFor("illuminate");
    }

    public RestMudResponseProcessor darken() {
        return makeApiCallFor("darken");
    }

    public RestMudResponseProcessor genericVerbNoun(String theVerb, String theNoun) {
        return makeApiCallFor(theVerb, theNoun);
    }

    public RestMudResponseProcessor genericVerb(String theVerb) {
        return makeApiCallFor(theVerb);
    }

    public void setRegistrationSecretCode(String registrationSecretCode) {
        this.registrationSecretCode = registrationSecretCode;
    }

    public void userNeedsToRegister(boolean registrationNeed) {
        this.registrationNeed = registrationNeed;
    }

    public RestMudResponseProcessor register() {
        String registerBody = "{\"username\":\"%s\", \"password\":\"%s\", \"displayname\":\"%s\", \"secret\":\"%s\"}";
        return http_POST_Request("/api/register", String.format(registerBody, this.forUser, this.password, this.forUser, this.registrationSecretCode));
    }

    public RestMudResponseProcessor login(String password) {
        if (this.password == null) {
            setPassword(password);
        }
        String loginBody = "{\"username\":\"%s\", \"password\":\"%s\"}";
        return http_POST_Request("/api/login", String.format(loginBody, this.forUser, password));
    }

    public RestMudResponseProcessor http_GET_Request(String urlTemplate) {
        connection.method(Connection.Method.GET);
        connection.requestBody(null);

        getRequestCount++;
        return makeRequestTo(urlTemplate);
    }

    public RestMudResponseProcessor http_POST_Request(String urlTemplate, String body) {
        connection.method(Connection.Method.POST);
        connection.requestBody(body);

        postRequestCount++;
        return makeRequestTo(urlTemplate);
    }

    public RestMudResponseProcessor http_DELETE_Request(String urlTemplate) {
        connection.method(Connection.Method.DELETE);
        connection.requestBody(null);

        deleteRequestCount++;
        return makeRequestTo(urlTemplate);
    }


    private RestMudResponseProcessor makeRequestTo(String urlTemplate) {
        URL urlToCall = this.restMudURL;
        Connection.Response result = null;

        waitIfRateLimiting();

        try {
            urlToCall = new URL(this.restMudURL, urlTemplate);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        connection.url(urlToCall);

        setLastRequestSentAtToCurrentTime();

        try {
            System.out.println(connection.request().method().name() +  " " + urlToCall);
            if(connection.request().requestBody()!=null && connection.request().requestBody().length()>0){
                System.out.println(connection.request().requestBody());
            }
            result = connection.execute();

            if (result != null) {
                System.out.println(result.statusCode());
                System.out.println(result.body());
                increaseStatusCodesCount(result.statusCode());
            }


        } catch (IOException e) {
            this.errorsCount++;
            e.printStackTrace();
        }

        if (result != null) {

            String json = result.body();
            RestMudResponseProcessor response = new RestMudResponseProcessor(json);

            return response;
        }
        return null;
    }

    private void waitIfRateLimiting() {
        if(rateLimitingMillis>0){

            long currentTime = System.currentTimeMillis();
            long timeBetween = currentTime - lastRequestSentAt;

            if(timeBetween<rateLimitingMillis){

                long timeToWait = rateLimitingMillis - timeBetween;
                //System.out.println("Rate Limiting " + timeToWait);

                try {
                    Thread.sleep(timeToWait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void increaseStatusCodesCount(int statusCode) {

        int currentCount = 0;
        if (statusCodesCount.containsKey(statusCode)) {
            currentCount = statusCodesCount.get(statusCode);
        }
        currentCount++;
        statusCodesCount.put(statusCode, currentCount);
    }

    public void reportStats() {
        System.out.println("---");
        System.out.println("errors : " + this.errorsCount);
        for (int statusCode : statusCodesCount.keySet()) {
            System.out.println(statusCode + " : " + statusCodesCount.get(statusCode));
        }
        System.out.println("---");
    }


    public void setCustomHeader(String headername, String headervalue) {
        connection.header(headername, headervalue);
    }

    public int getScore() {
        RestMudResponseProcessor response = genericVerb("score");
        return response.getScore();
    }

    public void deleteCustomHeader(String headername) {
        connection.request().headers().remove(headername);
    }


    public void addRateLimitingTo(long millisecondsBetween){
        this.rateLimitingMillis = millisecondsBetween;
        setLastRequestSentAtToCurrentTime();
    }

    private void setLastRequestSentAtToCurrentTime() {
        this.lastRequestSentAt = System.currentTimeMillis();
    }

}
