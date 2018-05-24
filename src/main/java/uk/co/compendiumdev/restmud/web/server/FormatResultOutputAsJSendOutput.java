package uk.co.compendiumdev.restmud.web.server;

import uk.co.compendiumdev.restmud.output.json.JSendOutput;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;
import spark.Response;

/**
 * Created by Alan on 25/07/2016.
 */
public class FormatResultOutputAsJSendOutput {

    public static String asString (ResultOutput resultOutput, Response response){
        return asString(resultOutput, response, 400);
    }

    public static String asString (ResultOutput resultOutput, Response response, int httpErrorCode){

        JSendOutput stateReturn;

        stateReturn = ResultOutputToJSendOutput.convert(resultOutput);

        if (resultOutput.resultoutput.lastactionstate.contentEquals(LastAction.SUCCESS)) {
            response.status(200);
        } else {
            response.status(httpErrorCode);
        }

        stateReturn.data = resultOutput;

        String stateReturnJson = stateReturn.asJson();

        System.out.println(stateReturnJson);

        return stateReturnJson;
    }
}
