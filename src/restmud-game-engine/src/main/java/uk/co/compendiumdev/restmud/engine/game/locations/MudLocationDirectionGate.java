package uk.co.compendiumdev.restmud.engine.game.locations;

/**
 * Created by Alan on 19/03/2016.
 */
public class MudLocationDirectionGate {
    public MudLocation fromLocation;
    public MudLocation toLocation;
    private final GateDirection whichWayDirection;
    private final String fromDirection;
    private final String toLocationDirection;
    private String toLocationId;
    private String fromLocationId;
    private GateStatus gateOpenStatus;
    private String shortDescription;
    private String closedDescription;
    private String throughDescription;
    private boolean isHidden;
    private boolean autoCloses;
    private boolean autohides;
    private String gateName;
    private boolean canPlayerOpen;
    private boolean canPlayerClose;
    private String reason;

    public MudLocationDirectionGate createCopy(MudLocation fromLocation, MudLocation toLocation) {
        MudLocationDirectionGate copyGate = new MudLocationDirectionGate(fromLocation, this.fromDirection, toLocation, this.toLocationDirection, this.whichWayDirection, this.gateOpenStatus);
        copyGate.setClosedDescription(this.closedDescription);
        copyGate.setShortDescription(this.shortDescription);
        copyGate.setThroughDescription(this.throughDescription);
        copyGate.gateIsHidden(this.isHidden);
        copyGate.gateAutoCloses(this.autoCloses);
        copyGate.gateAutoHides(this.autohides);
        copyGate.gateIsNamed(this.gateName);
        copyGate.playerCanOpen(this.canPlayerOpen);
        copyGate.playerCanClose(this.canPlayerClose);
        return copyGate;
    }

    public MudLocationDirectionGate(MudLocation fromLocation, String fromDirection, MudLocation toLocation, String toLocationDirection, GateDirection whichWayDirection, GateStatus startingGateStatus) {

        setDefaults();

        this.fromLocation = fromLocation;
        this.fromLocationId = fromLocation.getLocationId();
        this.toLocation = toLocation;
        this.toLocationId = toLocation.getLocationId();
        this.gateOpenStatus = startingGateStatus;
        this.whichWayDirection = whichWayDirection;
        this.fromDirection = fromDirection.toLowerCase();  // the direction that goes from -> to
        this.toLocationDirection= toLocationDirection.toLowerCase();   // the direction that goes to -> from
    }

    // this constructor used for definitions only, TODO: should really make this a new object I suppose
    public MudLocationDirectionGate(String fromLocationId, String fromDirection, String toLocationId, String toLocationDirection, GateDirection whichWayDirection, GateStatus startingGateStatus) {

        setDefaults();

        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.gateOpenStatus = startingGateStatus;
        this.whichWayDirection = whichWayDirection;
        this.fromDirection = fromDirection.toLowerCase();  // the direction that goes from -> to
        this.toLocationDirection= toLocationDirection.toLowerCase();   // the direction that goes to -> from
    }

    private void setDefaults() {
        this.shortDescription = "the door";
        this.closedDescription= "closed";
        this.throughDescription="through";
        this.isHidden=false; // visible by default
        this.autoCloses=false; // by default it does not autoclose
        this.autohides=false; // by default it does not autohide
        this.gateName=""; // most gates do not need a name
        this.canPlayerOpen=true;
        this.canPlayerClose=true;
    }

    public MudLocationDirectionGate setShortDescription(String description){
        this.shortDescription = description;
        return this;
    }

    public MudLocationDirectionGate setClosedDescription(String description){
        this.closedDescription = description;
        return this;
    }

    public MudLocationDirectionGate setThroughDescription(String description){
        this.throughDescription = description;
        return this;
    }

    public boolean mightBlock(MudLocation location, MudLocation destination, String direction) {
        if(this.whichWayDirection == GateDirection.BOTH_WAYS){
            return true;
        }

        if(this.whichWayDirection == GateDirection.ONE_WAY){
            if(this.fromLocation.equals(location) && this.toLocation.equals(destination) && direction.toLowerCase().contentEquals(this.fromDirection)){
                return true;
            }
        }

        return false;
    }

    public boolean isOpen() {
        return (gateOpenStatus == GateStatus.OPEN);
    }

    public String shortDescription() {
        return this.shortDescription;
    }

    public String closedDescription() {
        return this.closedDescription;
    }

    public String throughDescription() {
        return this.throughDescription;
    }

    public MudLocationDirectionGate open() {
        gateOpenStatus = GateStatus.OPEN;
        return this;
    }

    public MudLocationDirectionGate close() {
        gateOpenStatus = GateStatus.CLOSED;
        if(autohides){
            isHidden=true;
        }
        return this;
    }

    public MudLocationDirectionGate goThrough() {
        if(autoCloses){
            close();
        }
        return this;
    }


    public MudLocationDirectionGate gateIsHidden(boolean isHidden) {
        this.isHidden=isHidden;
        return this;
    }

    public MudLocationDirectionGate gateAutoCloses(boolean autoCloseOnThrough) {
        this.autoCloses = autoCloseOnThrough;
        return this;
    }

    public MudLocationDirectionGate gateAutoHides(boolean autoHideOnThrough) {
        this.autohides = autoHideOnThrough;
        return this;
    }

    public MudLocationDirectionGate gateIsNamed(String aName) {
        this.gateName = aName.toLowerCase();
        return this;
    }

    public String getGateName() {
        return gateName.toLowerCase();
    }

    public MudLocationDirectionGate playerCanOpen(boolean canPlayerOpen) {
        this.canPlayerOpen=canPlayerOpen;
        return this;
    }

    public MudLocationDirectionGate playerCanClose(boolean canPlayerClose) {
        this.canPlayerClose=canPlayerClose;
        return this;
    }

    public GateDirection getGateDirection() {
        return whichWayDirection;
    }

    public boolean isVisible() {
        return !isHidden;
    }

    public String getDirectionFor(MudLocation whereAmI) {
        if(fromLocation.getLocationId().contentEquals(whereAmI.getLocationId())){
            return fromDirection;
        }

        if(toLocation.getLocationId().contentEquals(whereAmI.getLocationId())){
            return toLocationDirection;
        }
        return "";
    }

    public String getFromDirection() {
        return fromDirection;
    }

    public String getToDirection() {
        return toLocationDirection;
    }

    public boolean getCanPlayerOpen() {
        return canPlayerOpen;
    }

    public boolean getCanPlayerClose() {
        return canPlayerClose;
    }

    public String getLocationIdFor(String baseDirection) {
        if(toLocationDirection.toLowerCase().contentEquals(baseDirection.toLowerCase())){
            return fromLocation.getLocationId();
        }

        if(fromDirection.toLowerCase().contentEquals(baseDirection.toLowerCase())){
            return toLocation.getLocationId();
        }

        return "";
    }

    public boolean getAutoCloses() {
        return autoCloses;
    }

    public String getFromLocationId() {
        return fromLocationId;
    }

    public String getToLocationId() {
        return toLocationId;
    }

    public GateDirection getWhichWayDirection() {
        return whichWayDirection;
    }

    public GateStatus getStartingGateStatus() {
        return gateOpenStatus;
    }

    public void because(String reason) {
        this.reason = reason;
    }
}
