package uk.co.compendiumdev.restmud.web.server.httpmessages;

import spark.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class RequestDataExtractor {

    public static String getSplatterFromRequest(Request request) {
        String splatter = "";

        // no data in the request
        if(request.splat().length==0){
            return splatter;
        }

        splatter = request.splat()[0];

        //http://stackoverflow.com/questions/213506/java-net-urlencoder-encodestring-is-deprecated-what-should-i-use-instead

        try {
            splatter = URLDecoder.decode(splatter.toLowerCase(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }

        // remove any trailing /
        while(splatter.endsWith("/")){
            splatter = splatter.substring(0,splatter.length()-1);
        }

        return splatter;
    }

}
