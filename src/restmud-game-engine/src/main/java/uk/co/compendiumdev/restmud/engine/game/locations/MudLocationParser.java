package uk.co.compendiumdev.restmud.engine.game.locations;


import uk.co.compendiumdev.restmud.engine.game.BroadcastMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

Consider:

- add gate names into the exit description, this way we don't have to 'find the gates during processing, we know which gates act'
e.g.
- "e:2:reddoor" the reddoor would control the access to the gate,
- secret on gate would not automatically apply to exits
- need semantic checking on game to detect any gates mentioned which do not exist
- this should not impact processing because if we couldn't find a gate during processing we would just ignore it
- the gate would not need to know about to/from because that would be handled by the exit
   - the gate would control messaging and open/close status
- gates could be re-used on multiple exists, so when one is open, they are all open, same with closed
- both-way gates would be created by adding the gate to each exit
- gates would be default one-way - and not reported in the 'other' side
- gates could be reported as one-way if it was `:blocked` which would mean it is reported as a gate,
    but using a `blocked` description, and would not open or close from the blocked side
- this should make it easier to 'open e', 'open w' etc.
- would it make it harder to 'open gatename'?
   - we would need to check for gate exists,
        check directions to see if gate is listed,
        check if gate is blocked,
        if not then open

 */
public class MudLocationParser {
    private final String fromLocationId;
    private List<String> syntaxErrors;

    public MudLocationParser(String fromLocationId) {
        this.fromLocationId=fromLocationId;
        this.syntaxErrors=new ArrayList<>();
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

                    // TODO: validate direction as a valid direction
                    String direction = routeExitPair[0];
                    String destination = routeExitPair[1];

                    MudLocationExit anExit = new MudLocationExit(fromLocationId, direction, destination);

                    // bug, need to iterate over 2 and beyond, not 0 and 1 because they have been handled
                    for (int attributeId=2; attributeId<routeExitPair.length; attributeId++) {
                        String attribute = routeExitPair[attributeId];
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
                            default:
                                // assume that this is a gate name
                                if(anExit.isGated()){
                                    syntaxErrors.add("Found additional gate allocation for exit: " + attribute);
                                }
                                // gate names can be case sensitive
                                anExit.setGateName(attribute);
                                break;
                        }
                    }

                    exits.put(direction.toLowerCase(), anExit);
                }else{
                    syntaxErrors.add("Declared Route has no separators: " + trimRoute);
                }

            }else{
                syntaxErrors.add("Declared Route is not valid: " + trimRoute);
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