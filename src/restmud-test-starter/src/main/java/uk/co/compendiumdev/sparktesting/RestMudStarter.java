package uk.co.compendiumdev.sparktesting;

import uk.co.compendiumdev.restmud.web.MainRestMud;

import java.net.HttpURLConnection;
import java.net.URL;

public class RestMudStarter extends SparkStarter{

    String[] args;

    static RestMudStarter storedStarter;

    public RestMudStarter(String[] args){
        this.args = args;
    }

    public static RestMudStarter singleton(final String[] args) {

        if(storedStarter == null) {
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
            return false;
        }
    }

    @Override
    public void startServer() {

        // TODO: have MainRestMud be a 'new' instance and expose the 'game' for inner manipulation by hybrid tests
        MainRestMud.main(args);

        System.out.println("Run main to start");
    }
}
