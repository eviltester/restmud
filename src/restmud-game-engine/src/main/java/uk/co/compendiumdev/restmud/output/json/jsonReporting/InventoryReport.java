package uk.co.compendiumdev.restmud.output.json.jsonReporting;

public class InventoryReport {
    public final String owner;
    public final IdDescriptionPair[] contents;

    public InventoryReport(String owner, IdDescriptionPair[] inventoryAsIdDescriptionPairs) {
        this.owner = owner;
        // TODO: this should probably be a copy of the array, or pass in the inventory itself
        this.contents = inventoryAsIdDescriptionPairs;
    }
}
