package uk.co.compendiumdev.restmud.output.json.jsonReporting;

/**
 * Created by Alan on 21/12/2015.
 */
public class LookLocation {
    public final String locationId;
    public final String shortName;
    public String description;
    public final boolean isInDarkness;

    public LookLocation(String locationId, String shortName, String description, Boolean isInDarkness) {
        this.locationId = locationId;
        this.shortName = shortName;
        this.description = description;
        this.isInDarkness = isInDarkness;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
