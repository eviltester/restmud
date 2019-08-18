package uk.co.compendiumdev.restmud.web.server;

import org.json.JSONObject;
import org.json.XML;
import spark.Request;

/**
 * Created by Alan on 25/07/2016.
 */
public class ApiResponseFor {

    boolean wantXml = false;

    public ApiResponseFor(Request request) {

        String accept = request.headers("accept");

        if ("application/xml".equalsIgnoreCase(accept)) {
            wantXml = true;
        }

    }

    public String getResponseBodyText(String json) {

        if(wantXml) {
            return jsonAsXMLResponse(json);
        }

        return json;
    }


    private String jsonAsXMLResponse(String myJson) {

        JSONObject json = new JSONObject(myJson);
        String xml = XML.toString(json);

        return "<response>" + xml + "</response>";
    }

    public String getResponseFormatHeader() {
        if(wantXml){
            return "application/xml";
        }
        return "application/json";
    }
}
