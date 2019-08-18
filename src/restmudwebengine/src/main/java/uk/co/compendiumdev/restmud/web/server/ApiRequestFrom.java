package uk.co.compendiumdev.restmud.web.server;

import org.json.XML;
import spark.Request;

/**
 * Created by Alan on 25/07/2016.
 */
public class ApiRequestFrom {
    private boolean givenXml=false;
    private String rawBody="";

    public ApiRequestFrom(Request request) {

        // Content-Type: application/json
        String contentType = request.headers("Content-Type");

        if ("application/xml".equalsIgnoreCase(contentType)) {
            givenXml = true;
        }

        rawBody = request.body();
    }

    /* no matter what the body contains - return it as xml */
    public String bodyAsJson() {


        // this makes the <request>...</request> xml elements optional
        // TODO: enforce the xml format
        if(givenXml){
            String processBody = rawBody.trim();
            processBody= processBody.replace("<request>","");
            processBody= processBody.replace("</request>","");

            return XML.toJSONObject(processBody).toString();
        }

        return rawBody;
    }
}
