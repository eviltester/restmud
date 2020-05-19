package uk.co.compendiumdev.restmud.engine.game.locations;

/**
 * TODO: a gate should not care where it is going from and to, the exit handles this
 * - the gate should know if it is open, closed, hidden, auto close, auto hide and if player closeable
 * - similarly gate should not know if it one way or two way, it doesn't care
 */
public class MudLocationDirectionGate {

    /* TODO: deprecated fields in gate */
    public MudLocation fromLocation;
    public MudLocation toLocation;
    private GateDirection whichWayDirection;
    private String fromDirection;
    private String toLocationDirection;
    private String toLocationId;
    private String fromLocationId;

    /* TODO: these are the fields we actually want in gate - refactor till this is all */
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

    /* minimal constructor because gates are added to exits */
    public MudLocationDirectionGate(final String gatename, final GateStatus initialStatus) {
        setDefaults();
        this.gateName = gatename.toLowerCase();
        this.gateOpenStatus = initialStatus;
    }

    // TODO: can this be simplified? is it easier to use flags for blocking and opening doors etc
    //       or have blocks which are active on specific flags

    /* avoid all location stuff because that is on the exit definitions */
    @Deprecated
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
    public MudLocationDirectionGate createCopy() {
        MudLocationDirectionGate copyGate = new MudLocationDirectionGate(this.gateName, this.gateOpenStatus);
        copyGate.setClosedDescription(this.closedDescription);
        copyGate.setShortDescription(this.shortDescription);
        copyGate.setThroughDescription(this.throughDescription);
        copyGate.gateIsHidden(this.isHidden);
        copyGate.gateAutoCloses(this.autoCloses);
        copyGate.gateAutoHides(this.autohides);
        copyGate.playerCanOpen(this.canPlayerOpen);
        copyGate.playerCanClose(this.canPlayerClose);
        return copyGate;
    }


    /* avoid all location stuff because that is on the exit definitions */
    @Deprecated
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
    /* avoid all location stuff because that is on the exit definitions */
    @Deprecated
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
        this.gateName=""; // gates now need names
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

    public boolean isVisible() {
        return !isHidden;
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

    public void because(String reason) {
        this.reason = reason;
    }

}
