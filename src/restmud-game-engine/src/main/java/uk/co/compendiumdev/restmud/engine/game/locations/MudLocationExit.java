package uk.co.compendiumdev.restmud.engine.game.locations;


public class MudLocationExit {
    private final String fromLocationId;
    private final String direction;
    private final String destinationLocationId;
    private boolean secretRoute;
    private String gateName;

    public MudLocationExit(String fromLocationId, String direction, String destinationLocationId) {

        this.fromLocationId = fromLocationId;
        this.direction = direction;
        this.destinationLocationId = destinationLocationId;
        this.secretRoute = false;
    }

    // secret routes are not shown in the look exits but can be navigated as normal
    // game designer should mention them via condition messages or in the location description
    // if they want the player to know about them
    public void setSecretRoute(boolean secretRoute) {
        this.secretRoute = secretRoute;
    }

    public String getDestinationId() {
        return destinationLocationId;
    }

    public String getDirection() {
        return direction;
    }

    public boolean isVisible() {
        return !secretRoute;
    }

    public boolean isLocal() {
        // TODO: this should be a proper attribute, not based on the destination
        return (this.destinationLocationId.contentEquals("local"));
    }

    public boolean isGated() {
        return (this.gateName!=null && this.gateName.length()>0);
    }

    public String getGateName() {
        return this.gateName;
    }

    public MudLocationExit setGateName(final String gateName) {
        this.gateName = gateName;
        return this;
    }
}
