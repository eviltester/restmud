package uk.co.compendiumdev.restmud.engine.game.things;

/**
 * Created by Alan on 15/12/2015.
 */
public class MudCollectable {
    private final String collectableId;
    private final String description;
    private boolean hoardable;
    private int hoardableScore
            ;
    private String abilityVerb;
    private int verbActionPoints;
    private boolean toggleAbilityIsOn;
    private boolean isToggleAbility;
    private String reason;

    public MudCollectable(String collectableId, String description) {
        this.collectableId = collectableId.toLowerCase();
        this.description = description;
        this.hoardable = false;
        this.hoardableScore = 0;
        this.abilityVerb="";
        this.verbActionPoints=0;
        this.toggleAbilityIsOn=false;
        this.isToggleAbility=false;

    }

    public String getCollectableId() {
        return collectableId;
    }

    public String getDescription() {
        return description;
    }

    public MudCollectable canHoard(boolean iCanBeHoarded, int scoreForHoarding) {
        this.hoardable = iCanBeHoarded;
        this.hoardableScore = scoreForHoarding;
        return this;
    }

    public boolean isHoardable(){
        return this.hoardable;
    }

    public int getHoardableScore(){
        return this.hoardableScore;
    }

    // an ability allows you to do something that costs points
    public MudCollectable addsAbility(String verb, int verbActionPoints) {
        this.abilityVerb = verb;
        this.verbActionPoints = verbActionPoints;
        return this;
    }

    // a toggle ability is either on, or off and points are removed when on, over time
    public MudCollectable addsToggleAbility(String verbs, int totalPoints, boolean isOnNow) {
        this.isToggleAbility=true;
        this.abilityVerb = verbs;
        this.verbActionPoints = totalPoints;
        this.toggleAbilityIsOn = isOnNow;
        return this;
    }

    public String getAbilityName() {
        return abilityVerb;
    }

    public int getAbilityPower() {
        return verbActionPoints;
    }

    public void setAbilityPower(int abilityPower) {
        this.verbActionPoints = abilityPower;
    }


    public MudCollectable setAbilityOn(boolean abilityOn) {
        this.toggleAbilityIsOn = abilityOn;
        return this;
    }

    public boolean getIsAbilityToggleable() {
        return this.isToggleAbility;
    }

    public boolean getIsAbilityOn() {
        return toggleAbilityIsOn;
    }

    public MudCollectable createClonedCopy() {
        MudCollectable clone = new MudCollectable(this.collectableId, this.description);
        clone.canHoard(this.hoardable, this.hoardableScore);
        clone.addsAbility(this.abilityVerb, this.verbActionPoints);
        clone.setAbilityOn(this.toggleAbilityIsOn);
        clone.canToggleAbility(this.isToggleAbility);
        return clone;
    }

    private void canToggleAbility(boolean isToggleAbility) {
        this.isToggleAbility=isToggleAbility;
    }

    public MudCollectable because(String reason) {
        this.reason = reason;
        return this;
    }
}
