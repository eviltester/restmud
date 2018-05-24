package uk.co.compendiumdev.restmud.engine.game.things;

import java.util.Random;

public class MudLocationObject {

    private final String id;
    private final String description;
    private final String examineDescription;
    private boolean isSecret;  // not visible in the 'look' description when isSecret==true
    private boolean isADispenser;
    private MudCollectableTemplate dispenserTemplate;
    private String reason;

    public MudLocationObject(String objectId, String description, String examineDescription) {
        this.id = objectId.toLowerCase();
        this.description = description;
        if (examineDescription.length() == 0) {
            this.examineDescription = "There is nothing more to say about this.";
        } else {
            this.examineDescription = examineDescription;
        }
        isSecret = false; // by default it is visible
        this.isADispenser = false; // by default this is not a dispenser
        this.dispenserTemplate = null;
    }

    public MudLocationObject createClonedCopy() {
        MudLocationObject clonedCopy = new MudLocationObject(this.id, this.description, this.examineDescription);
        clonedCopy.setAsSecret(this.isSecret);
        clonedCopy.setIsADispenser(this.isADispenser);
        // TODO: this should probably be a cloned dispenser template
        clonedCopy.setDispenserTemplate(dispenserTemplate);
        return clonedCopy;
    }

    public String getObjectId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getExamineDescription() {
        return examineDescription;
    }

    public MudLocationObject setAsSecret(boolean asSecret) {
        this.isSecret = asSecret;
        return this;
    }

    public boolean isSecret() {
        return this.isSecret;
    }

    public void setIsADispenser(boolean isADispenser) {
        this.isADispenser = isADispenser;
    }

    public void setDispenserTemplate(MudCollectableTemplate dispenserTemplate) {
        this.dispenserTemplate = dispenserTemplate;
    }

    public boolean isThisADispenser() {
        return isADispenser;
    }

    public MudCollectable dispense(String dispensedId) {

        if (!isADispenser) {
            return null;
        }

        MudCollectable dispensed = new MudCollectable(dispensedId, dispenserTemplate.description);

        int maxPow = dispenserTemplate.maxAbilityPower;
        int minPow = dispenserTemplate.minAbilityPower;
        int powDiff = maxPow - minPow;

        int abilityPower = 0;
        if (powDiff > 0) {
            abilityPower = new Random().nextInt(maxPow - minPow + 1) + minPow;
        }

        if(dispenserTemplate.abilityCanToggle) {
            dispensed.addsToggleAbility(dispenserTemplate.ability, abilityPower, false);
        }else {
            dispensed.addsAbility(dispenserTemplate.ability, abilityPower);
        }

        int hoardableScore = 0;
        int maxHPow = dispenserTemplate.maxHoardablePoints;
        int minHPow = dispenserTemplate.minHoardablePoints;
        if (dispenserTemplate.isHoardable) {
            hoardableScore = new Random().nextInt(maxHPow - minHPow + 1) + minHPow;
        }

        dispensed.canHoard(dispenserTemplate.isHoardable, hoardableScore);

        return dispensed;
    }

    public String getTemplateId() {
        if (!isADispenser) {
            return "";
        }

        if (dispenserTemplate == null) {
            return "";
        }

        return dispenserTemplate.idPrefix;
    }


    public MudLocationObject because(String reason) {
        this.reason = reason;
        return this;
    }
}
