package uk.co.compendiumdev.restmud.output.json.jsonReporting;

/**
 * Created by Alan on 21/12/2015.
 */
public class InventoryReport {
    public final String owner;
    public final IdDescriptionPair[] contents;

    public InventoryReport(String owner, IdDescriptionPair[] inventoryAsIdDescriptionPairs) {
        this.owner = owner;
        this.contents = inventoryAsIdDescriptionPairs;
    }
}
