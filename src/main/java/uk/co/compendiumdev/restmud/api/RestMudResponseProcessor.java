package uk.co.compendiumdev.restmud.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import uk.co.compendiumdev.restmud.output.json.ResultOutput;
import uk.co.compendiumdev.restmud.output.json.VisibleGate;

/**
 * Created by Alan on 24/01/2017.
 */
public class RestMudResponseProcessor {
    private final String json;
    private JsonObject elements;
    public ResultOutput result;

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public RestMudResponseProcessor(String json) {
        this.json = json;
        JsonParser parser = new JsonParser();
        elements = (JsonObject)parser.parse(json);

    }

    public boolean isSuccessful() {
        if( elements.has("status") &&
            elements.get("status").getAsString().contentEquals(SUCCESS)){

            result = new Gson().fromJson(elements.get("data").getAsJsonObject(), ResultOutput.class);

            return true;
        }
        return false;

    }

    public String getData(){

            try{
                return elements.get("data").getAsJsonObject().get("Cannot").getAsString();
            }catch (Exception e){
                return "";
            }
    }

    public VisibleGate getGateGoing(String chosenDirection) {
        try {
            for (VisibleGate gate : result.look.visibleGates) {
                if (gate.direction.toLowerCase().contentEquals(chosenDirection.toLowerCase())) {
                    return gate;
                }

            }
        }catch(NullPointerException e){
            // suspect there was no gate that way
        }
        return null;
    }

    public int getScore() {

        isSuccessful();
        String scoreText = result.resultoutput.lastactionresult.replaceFirst("Your Score is: ", "");
        return Integer.parseInt(scoreText);

    }
}
