package uk.co.compendiumdev.restmud.web.server;

import uk.co.compendiumdev.restmud.output.json.JSendOutput;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.LastAction;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.ResultOutput;

/**
 * Created by Alan on 27/06/2016.
 */
public class ResultOutputToJSendOutput {
    public static JSendOutput convert(ResultOutput resultOutput) {

        JSendOutput stateReturn;

        if (resultOutput.resultoutput.lastactionstate.contentEquals(LastAction.SUCCESS)) {
            stateReturn = JSendOutput.Success();
        } else {
            stateReturn = JSendOutput.Fail();
            stateReturn.failMessage("Cannot", resultOutput.resultoutput.lastactionresult);
        }

        stateReturn.data = resultOutput;

        return stateReturn;

    }
}
