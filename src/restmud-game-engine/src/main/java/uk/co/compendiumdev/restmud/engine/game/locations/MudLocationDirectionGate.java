package uk.co.compendiumdev.restmud.engine.game.locations;

/**
 * A gate does not care where it is going from and to, the exit handles this
 * - the gate should know if it is open, closed, hidden, auto close, auto hide and if player closeable
 * - similarly gate should not know if it one way or two way, it doesn't care
 *
 */
public class MudLocationDirectionGate {

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

    public boolean getCanPlayerOpen() {
        return canPlayerOpen;
    }

    public boolean getCanPlayerClose() {
        return canPlayerClose;
    }

    public boolean getAutoCloses() {
        return autoCloses;
    }

    public void because(String reason) {
        this.reason = reason;
    }

}
