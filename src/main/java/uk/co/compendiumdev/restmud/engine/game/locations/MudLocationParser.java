package uk.co.compendiumdev.restmud.engine.game.locations;


import java.util.HashMap;
import java.util.Map;

public class MudLocationParser {
    private final String locationId;

    public MudLocationParser(String locationId) {
        this.locationId=locationId;
    }

    public Map<String, MudLocationExit> parse(String routesAndExits) {
        // parse routes and exits

        Map<String, MudLocationExit>exits = new HashMap<>();

        String[] routesExits = routesAndExits.split(",");
        for (String aRoute : routesExits) {
            String trimRoute = aRoute.trim();
            if (trimRoute.length() >= 3) {
                if (trimRoute.contains(":")) {


                    String[] routeExitPair = aRoute.trim().split(":");

                    String direction = routeExitPair[0];
                    String destination = routeExitPair[1];

                    MudLocationExit anExit = new MudLocationExit(locationId, direction, destination);

                    for (String attribute : routeExitPair) {
                        // look for attributes
                        switch (attribute.toLowerCase()) {
                            case "secret":
                                anExit.setSecretRoute(true);
                                break;
                        }
                    }

                    exits.put(direction.toLowerCase(), anExit);
                }
            }
        }

        return exits;
    }
}
