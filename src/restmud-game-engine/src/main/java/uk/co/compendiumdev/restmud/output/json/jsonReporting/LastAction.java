package uk.co.compendiumdev.restmud.output.json.jsonReporting;


import java.util.List;

public class LastAction {

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public String lastactionstate;
    public String lastactionresult;
    private ObjectInspection optionalInspectionReport;
    private InventoryReport optionalInventoryReport;
    private List<GetUserDetails> optionalUserDetailsList;
    private GameMessages optionalGameMessages;

    public LastAction(String state, String lastActionMessage) {
        this.lastactionstate = state;
        this.lastactionresult = lastActionMessage;
        this.optionalInspectionReport=null;
        this.optionalInventoryReport=null;
        this.optionalUserDetailsList=null;
        this.optionalGameMessages=null;
    }

    public static LastAction createSuccess(String lastActionMessage) {
        return new LastAction(SUCCESS, lastActionMessage);
    }

    public static LastAction createError(String lastActionMessage) {
        return new LastAction(FAIL, lastActionMessage);
    }

    public void setInspectReport(ObjectInspection inspectReport) {
        optionalInspectionReport = inspectReport;
    }

    public ObjectInspection getObjectInspectionReport(){
        return optionalInspectionReport;
    }

    public void removeObjectInspectionReport(){
        optionalInspectionReport=null;
    }

    public void setInventoryReport(InventoryReport inventoryReport) {
        optionalInventoryReport = inventoryReport;
    }

    public InventoryReport getInventoryReport(){
        return optionalInventoryReport;
    }

    public void removeInventoryReport(){
        optionalInventoryReport=null;
    }

    public void setUsersDetails(List<GetUserDetails> details) {
        this.optionalUserDetailsList = details;
    }

    public List<GetUserDetails> getUserDetailsList(){
        return optionalUserDetailsList;
    }

    public void removeUserDetailsList(){
        optionalUserDetailsList=null;
    }

    public void setGameMessages(GameMessages gameMessages) {
        optionalGameMessages = gameMessages;
    }

    public GameMessages getGameMessages(){
        return optionalGameMessages;
    }

    public void removeGameMessages(){
        optionalGameMessages=null;
    }

    public boolean isFail() {
        return lastactionstate.contentEquals(FAIL);
    }

    public boolean isSuccess() {
        return lastactionstate.contentEquals(SUCCESS);
    }
}
