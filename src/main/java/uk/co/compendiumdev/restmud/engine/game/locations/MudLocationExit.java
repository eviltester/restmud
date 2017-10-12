package uk.co.compendiumdev.restmud.engine.game.locations;

/**
 * Created by Alan on 26/03/2016.
 */
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
