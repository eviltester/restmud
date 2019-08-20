package uk.co.compendiumdev.restmud.engine.game.locations;


public class MudLocationExit {
    private final String fromLocationId;
    private final String direction;
    private final String destinationLocationId;
    private boolean secretRoute;

    public MudLocationExit(String fromLocationId, String direction, String destinationLocationId) {

        this.fromLocationId = fromLocationId;
        this.direction = direction;
        this.destinationLocationId = destinationLocationId;
        this.secretRoute = false;
    }

    // secret routes are not shown in the description but can be navigated as normal
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

}
