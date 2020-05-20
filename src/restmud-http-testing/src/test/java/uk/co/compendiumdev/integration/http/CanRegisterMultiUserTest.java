package uk.co.compendiumdev.integration.http;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.sparktesting.RestMudStarter;

import java.io.IOException;
import java.util.Map;

public class CanRegisterMultiUserTest {

    @Test
    public void canRegisterAUser() throws IOException {

        // env variables
        final String RESTMUDEFAULTUSERS = "RESTMUDEFAULTUSERS";
        final String GAMESECRETCODE = "GAMESECRETCODE";
        final String WIZAUTHCODE = "WIZAUTHCODE";

        // start app
        String[] args = {"-playermode", "multi", "-port", "1234"};

        System.setProperty(GAMESECRETCODE,"1234567");

        final RestMudStarter restmud = new RestMudStarter(args);

        restmud.startSparkAppIfNotRunning(1234);


        Connection torestmud = Jsoup.connect("http://localhost:1234/register");

        Connection.Response register = torestmud.method(Connection.Method.GET).execute();

        Assert.assertEquals(200,register.statusCode());

        // register a user by submitting the form
        final Connection.Response registered = torestmud
                .data("username", "bob")
                .data("displayname", "bob")
                .data("password", "bob")
                .data("secret", "1234567")
                .cookies(register.cookies()).
                        method(Connection.Method.POST).
                        execute();

        Assert.assertEquals(200, registered.statusCode());

        Document page = registered.parse();
        Assert.assertEquals("RESTMud: bob Look", page.title());
        Assert.assertTrue(page.body().html().contains("{\"lastactionstate\":\"success\",\"lastactionresult\":\"You look.\"}"));


        // double check I can re-use the connection to do stuff

        //http://localhost:4567/player/bob/examine/ahint

        final Connection.Response examine = torestmud.url("http://localhost:1234/player/bob/examine/ahint").
                                            method(Connection.Method.GET).
                                            execute();

        Assert.assertEquals(200, examine.statusCode());

        page = examine.parse();
        final Element h2 = page.getElementsByTag("h2").first();

        System.out.println(h2.text());
        Assert.assertTrue(h2.text().startsWith("You Examine: A sign on the wall"));


        restmud.killServer();
    }

}
