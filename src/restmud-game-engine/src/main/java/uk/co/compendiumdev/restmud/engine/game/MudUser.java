package uk.co.compendiumdev.restmud.engine.game;

import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.verbs.PlayerCommand;
import uk.co.compendiumdev.restmud.engine.game.verbs.VerbGameAbilities;

import java.util.*;

public class MudUser {
    private final String userName;

    private String password;
    private Inventory inventory;
    private Inventory hoard;
    private PlayerGuiMode guiMode;
    private int score;
    private String displayName;
    private String locationId;
    private boolean isWizard;
    private String authToken;
    private long lastActionTimeStamp;
    private boolean visibleToOthers;


    private BooleanFlags flags;
    private IntegerCounters counters;

    //Player verb handlers
    private PlayerCommand currentCommand;


    public MudUser(String displayName, String userName, String password) {
        this(displayName, userName);

        this.password = password;


    }

    public MudUser(String displayName, String userName) {

        this.reset();

        this.guiMode = PlayerGuiMode.SUPER_EASY;
        this.isWizard=false;

        this.displayName = displayName;
        this.userName = userName.toLowerCase();

        this.authToken = UUID.randomUUID().toString();
        this.password = this.authToken;

        visibleToOthers = true;
    }

    public void reset() {

        this.inventory = new Inventory();
        this.hoard = new Inventory();
        this.score = 0;
        flags = new BooleanFlags();
        counters = new IntegerCounters();
    }


    public String displayName() {
        return this.displayName;
    }

    public MudUser setLocationId(String locationId) {
        if(locationId!=null) {
            this.locationId = locationId;
        }

        return this;
    }

    public String getLocationId() {
        return locationId;
    }

    public String userName() {
        return this.userName;
    }

    public String getPassword() {
        return password;
    }

    public Inventory inventory() {
        return this.inventory;
    }

    public Inventory treasureHoard() {
        return hoard;
    }

    public synchronized MudUser incrementScoreBy(int increment){
        this.score += increment;
        return this;
    }

    public synchronized int getScore(){
        return this.score;
    }

    public synchronized void setScore(int newScore) {
        this.score=newScore;
    }


    public PlayerGuiMode getGuiMode() {
        return guiMode;
    }

    public MudUser setAsWizard() {
        this.isWizard=true;
        return this;
    }

    public boolean isWizard() {
        return this.isWizard;
    }

    public MudUser setAuthToken(String authToken) {
        this.authToken = authToken;
        return this;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public MudUser setPassword(String password){
        this.password = password;
        return this;
    }

    public long getLastActionTimeStamp() {
        return lastActionTimeStamp;
    }

    public void updateLastActionTimeStamp() {
        lastActionTimeStamp = System.currentTimeMillis();
    }

    public boolean canISeeInTheDark() {

        // Having the state as a flag meant that when we drop a torch we have to update everything
        // too  much reliance on logic
        // rely on artifact presence instead
        // this will be a state change based on magic or torches

        MudCollectable artifactToSeeBy = grantedTheAbilityToBy(VerbGameAbilities.ILLUMINATE_DARKEN);
        if(artifactToSeeBy!=null){
            if(artifactToSeeBy.getIsAbilityOn()){
                if(artifactToSeeBy.getAbilityPower()>0){
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isVisibleToOthers() {
        return visibleToOthers;
    }

    public MudUser setVisibilityToOthers(boolean visible) {
        this.visibleToOthers = visible;
        return this;
    }


    public boolean hasTheAbilityTo(String thingAbility) {
        return (grantedTheAbilityToBy(thingAbility)!=null);
    }

    public MudCollectable grantedTheAbilityToBy(String thingAbility) {
        // abilities are granted by collectible in the inventory

        List<MudCollectable> holdingItems = inventory().itemsView();
        for(MudCollectable collectable : holdingItems){
            if(collectable.getAbilityName().equalsIgnoreCase(thingAbility)){
                return collectable;
            }
        }
        return null;
    }

    public BooleanFlags getFlags(){
        return flags;
    }

    public IntegerCounters getCounters(){
        return counters;
    }

    public MudUser setCurrentCommand(PlayerCommand playerCommand) {
        this.currentCommand = playerCommand;
        return this;
    }

    public PlayerCommand getCurrentCommand() {
        return this.currentCommand;
    }
}
