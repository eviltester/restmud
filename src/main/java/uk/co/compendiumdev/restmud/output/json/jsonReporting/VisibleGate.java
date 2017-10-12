package uk.co.compendiumdev.restmud.output.json.jsonReporting;


public class VisibleGate {
    public final String direction;
    public final String description;
    public final boolean open;
    public final String gateDescription;

    public VisibleGate(String direction, boolean open, String gateDescription, String closedDescription) {
        this.direction = direction;
        this.open = open;
        this.gateDescription = gateDescription;

        String wayDescription = "open";
        if(!open){
            wayDescription = closedDescription;
        }
        this.description = gateDescription + " to the " + direction + " is " + wayDescription;
    }

}
