package uk.co.compendiumdev.sparktesting;

import spark.Spark;
import uk.co.compendiumdev.restmud.web.MainRestMud;

import java.net.HttpURLConnection;
import java.net.URL;

public class RestMudStarter extends SparkStarter{

    String[] args;

    public RestMudStarter(String[] args){
        this.args = args;
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

        MainRestMud.main(args);

        System.out.println("Run main to start");
    }
}
