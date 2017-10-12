package uk.co.compendiumdev.restmud.engine.game;


import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;

import java.util.*;

public class Locations {

    private Map<String, MudLocation> locations = new HashMap<>();

    public List<String> getLocationKeys() {
        return new ArrayList<String>(locations.keySet());
    }

    public List<IdDescriptionPair> getLocationsAsIdDescriptionPairs() {

        List<IdDescriptionPair> pairs = new ArrayList<IdDescriptionPair>();

        for(MudLocation location : locations.values()){
            String isDark = "";
            if(location.isLocationDark()){
                isDark = " [is Dark] ";
            }
            pairs.add(new IdDescriptionPair(location.getLocationId(), location.getShortName() + isDark));
        }
        return pairs;
    }

    public Locations addLocation(MudLocation mudLocation) {
        locations.put(mudLocation.getLocationId().toLowerCase(), mudLocation);
        return this;
    }

    public MudLocation get(String locationId) {
        if(locations.containsKey(locationId.toLowerCase())){
            return locations.get(locationId.toLowerCase());
        }
        return null;
    }

    public Collection<MudLocation> getLocations() {
        return locations.values();
    }
}
