package uk.co.compendiumdev.restmud.engine.game.locations;


import java.util.HashMap;
import java.util.Map;

/**

locations and exists are a string e.g "e:2,n:4"

- comma separated exit definitions
- where an exit definition has the attributes separated by `:`
- the first two items are the 'exit' and the 'destination'
- any remaining items are attributes e.g. secret "e:2:secret"
- local can be used as the destination
    - to signify that a local condition handler is used for the exit control
    - and any verbs that act on destinations will report that they 'cannot'
    - the condition handlers must output a lastAction otherwise it will fall through to 'cannnot'

 */
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
                            case "local":
                                // local signifies that a local condition should handle
                                // this and create a lastAction
                                // if not then go, open and close will report that
                                // we cannot, go, open or close
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

// TODO - at the moment all routes are defined on each location e.g. location 1 N:2, and location 2 S:1
//        allow creating passages e.g. passage(1,2,N) which would create an exit from 1 to 2 going N
//        and a corresponding South passage from 2 to 1
//        make this polymorphic and allow passage(1, 2, N, E) which allows creating an exit from
//        1, 2, going north and 2, 1 going east