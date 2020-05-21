package uk.co.compendiumdev.restmud.output.json;


import java.util.List;

public class LastAction {

    public String lastactionstate;
    public String lastactionresult;
    public ObjectInspection optionalInspectionReport;
    public InventoryReport optionalInventoryReport;
    public List<GetUserDetails> optionalUserDetailsList;
    public GameMessages optionalGameMessages;
}
