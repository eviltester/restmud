package uk.co.compendiumdev.sparktesting;

import uk.co.compendiumdev.integration.http.RestMudInMemoryConfig;
import uk.co.compendiumdev.restmud.web.MainRestMud;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestMudStarter extends SparkStarter{

    String[] args;

    static RestMudStarter storedStarter;

    public RestMudStarter(String[] args){
        this.args = args;
    }

    public static RestMudStarter singleton(final RestMudInMemoryConfig config) {

        List<String> argsList = new ArrayList<>();

        argsList.add("-playermode");
        argsList.add(config.playerMode());
        argsList.add("-port");
        argsList.add(config.port());

        if(config.hasDesiredGame()){
            argsList.add("-gamename");
            argsList.add(config.gameNamed());
        }

        for(String arg : argsList){
            System.out.println("CONFIG TO SINGLETON: " + arg);
        }

        String[] args = new String[argsList.size()];
        argsList.toArray(args);

        // env variables
        // TODO: allow configuration of wizauthcode and defaultusers from properties and command line
        final String RESTMUDEFAULTUSERS = "RESTMUDEFAULTUSERS";
        final String WIZAUTHCODE = "WIZAUTHCODE";

        // env and property variables
        final String GAMESECRETCODE = "GAMESECRETCODE";

        if(config.hasRegistrationCode()) {
            System.setProperty(GAMESECRETCODE, config.registrationCode());
        }

        if(storedStarter == null) {
            System.out.println("Creating new RESTMudStarter");
            storedStarter = new RestMudStarter(args);
        }

        return storedStarter;
    }

    public boolean isRunning() {
        try{
            URL url = new URL("http://localhost:" + sparkport);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            System.out.println("status " + status);
            return status==200;

        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void startServer() {

        System.out.println("STARTING SERVER");

        // TODO: have MainRestMud be a 'new' instance and expose the 'game' for inner manipulation by hybrid tests
        MainRestMud.main(args);

        System.out.println("Run main to start");
    }

    public void stopServer(){
        System.out.println("STOPPING SERVER");
        storedStarter.killServer();
        storedStarter=null;
        System.out.println("CLEARED SINGLETON SERVER");
    }
}
