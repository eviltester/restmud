package uk.co.compendiumdev.restmud.output.json;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * http://labs.omniti.com/labs/jsend
 */
public class JSendOutput {
    public final String status;

    //http://stackoverflow.com/questions/4802887/gson-how-to-exclude-specific-fields-from-serialization-without-annotations
    private final transient Map<String, String> messages;

    public Object data;
    public String message;

    private JSendOutput(String status) {
        this.status = status;
        this.messages = new HashMap<String,String>();
    }

    /**
     * Success: When an API call is successful, the JSend object is used as a simple envelope for the results, using the data key, as in the following:
     {
     status : "success",
     data : {
     "posts" : [
     { "id" : 1, "title" : "A blog post", "body" : "Some useful content" },
     { "id" : 2, "title" : "Another blog post", "body" : "More content" },
     ]
     }
     }

     * @return
     */
    public static JSendOutput Success(){
        return new JSendOutput("success");
    }

    /**
     * Error: When an API call fails due to an error on the server. For example:
     *
     * {
     "status" : "error",
     "message" : "Unable to communicate with database"
     }
     * @return
     */
    public static JSendOutput Error(){
        return new JSendOutput("error");
    }

    /**
     * Fail: When an API call is rejected due to invalid data or call conditions, the JSend object's data key contains an object explaining what went wrong, typically a hash of validation errors. For example:
     *
     * POST /posts.json (with data body: "Trying to creating a blog post"):
     *
     * {
     "status" : "fail",
     "data" : { "title" : "A title is required" }
     }
     * @return
     */
    public static JSendOutput Fail(){
        return new JSendOutput("fail");
    }

    public JSendOutput message(String message) {
        this.message = message;
        return this;
    }

    public JSendOutput failMessage(String messageKey, String message) {
        messages.put(messageKey, message);
        return this;
    }

    public String asJson() {
        Gson json = new Gson();

        if(status.contentEquals("fail")){
            data = messages;
        }

        // if error then we should already have set the message
        // if success then we should already have set the data

        // if success and we did not set the data then
        if(status.contentEquals("success") && data==null){
            // if we didn't add data then we may have added a message
            data = messages;
        }

        return json.toJson(this); // assuming it excludes private

    }

    // JSend format requires data so we will use a Hash for the data message
    public JSendOutput successMessage(String description) {
        return addMessage("successMessage", description);
    }

    public JSendOutput addMessage(String messageKey, String description) {
        messages.put(messageKey, description);
        return this;
    }
}
