package uk.co.compendiumdev.restmud.engine.game.things;

/**
 * TODO: why have template and non-template?
 * Could I use a MudCollectable as the template and the Template class as
 * something which converts a collectable into a dispensable?
 */
public class MudCollectableTemplate {
    public final String idPrefix;
    public final String description;
    public String ability;
    public int minAbilityPower;
    public int maxAbilityPower;
    public boolean isHoardable;
    public int minHoardablePoints;
    public int maxHoardablePoints;
    public boolean abilityCanToggle;


    public MudCollectableTemplate(String idPrefix, String description) {
        this.idPrefix = idPrefix;
        this.description = description;

        this.ability = "";
    }

    public MudCollectableTemplate(String idPrefix, String description, String ability, int minPower, int maxPower) {

        this(idPrefix, description);

        this.ability = ability;
        this.minAbilityPower = minPower;
        this.maxAbilityPower = maxPower;

        this.setHoardableAttributes(false,0,0);
    }

    public static MudCollectableTemplate getDispenserTemplate(String idPrefix, String description, String ability, int minPower, int maxPower) {

        MudCollectableTemplate template = new MudCollectableTemplate(idPrefix,description, ability, minPower, maxPower);
        template.setAbilityCanToggle(false);
        return template;
    }

    public static MudCollectableTemplate getDispenserTemplate(String idPrefix, String description, String ability, int minPower, int maxPower, boolean canToggle) {

        MudCollectableTemplate template = new MudCollectableTemplate(idPrefix, description, ability, minPower, maxPower);
        template.setAbilityCanToggle(canToggle);
        return template;
    }

    public static MudCollectableTemplate getDispenserTemplate(String idPrefix, String description) {

        MudCollectableTemplate template = new MudCollectableTemplate(idPrefix,description);
        return template;
    }


    public MudCollectableTemplate setHoardableAttributes(boolean isHoardable, int minHoardablePoints, int maxHoardablePoints) {

        this.isHoardable = isHoardable;
        this.minHoardablePoints = minHoardablePoints;
        this.maxHoardablePoints = maxHoardablePoints;
        return this;

    }

    public MudCollectableTemplate setAbilityCanToggle(boolean abilityCanToggle) {
        this.abilityCanToggle = abilityCanToggle;
        return this;
    }
}
