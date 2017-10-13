package uk.co.compendiumdev.restmud.engine.game;

import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationDirectionGate;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptableCounter;
import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptableFlag;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;
import uk.co.compendiumdev.restmud.engine.game.verbs.VerbGameAbilities;
import uk.co.compendiumdev.restmud.engine.game.verbs.VerbInspect;
import uk.co.compendiumdev.restmud.engine.game.verbs.handlers.perPlayer.*;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.*;

import java.util.*;

public class MudUser {
    private final String userName;
    private Map<String, Boolean> userFlags;
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
    private Map<String, Integer> userCounters;

    //Player verb handlers
    private VerbTake take;
    private VerbPolish polish;
    private VerbExamine examine;
    private VerbIlluminate illuminate;
    private VerbDarken darken;
    private VerbDrop drop;
    private VerbHoard verbHoard;
    private VerbInspect inspect;
    private VerbInventory inventoryVerbHandler;
    private VerbScore scoreVerbHandler;


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


        take = new VerbTake(this);
        polish = new VerbPolish(this);
        examine = new VerbExamine(this);
        illuminate = new VerbIlluminate();
        darken = new VerbDarken();
        drop = new VerbDrop(this);
        verbHoard = new VerbHoard(this);
        inspect = new VerbInspect(this);
        inventoryVerbHandler = new VerbInventory(this);
        scoreVerbHandler = new VerbScore(this);
    }

    public void reset() {

        this.inventory = new Inventory();
        this.hoard = new Inventory();
        this.score = 0;
        this.userFlags = new HashMap<String, Boolean>();
        this.userCounters = new HashMap<String, Integer>();

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

    public boolean userFlagExists(String matchThis) {
        return userFlags.containsKey(matchThis);
    }

    public boolean getUserFlag(String matchThis) {
        Boolean flagValue = userFlags.get(matchThis);
        if(flagValue==null){
            return false;
        }

        return flagValue.booleanValue();
    }

    public void setUserFlag(ScriptableFlag userFlag) {
        userFlags.put(userFlag.name, userFlag.getValue());
    }

    public void deleteUserFlag(String name) {
        userFlags.remove(name);
    }

    public void setUserCounter(ScriptableCounter counter) {
        userCounters.put(counter.name, counter.getValue());
    }

    public void incrementUserCounter(String name, int value) {
        if(userCounterExists(name)){
            Integer count = userCounters.get(name);
            count = count.intValue() + value;
            userCounters.put(name, count);
        }
    }

    public boolean userCounterExists(String matchThis) {
        return userCounters.containsKey(matchThis);
    }

    public int getUserCounter(String name) {
        if(userCounterExists(name)) {
            return userCounters.get(name);
        }

        return 0; // by default
    }

    public void deleteUserCounter(String name) {
        userCounters.remove(name);
    }

    public VerbTake take() {
        return take;
    }

    public VerbPolish polish() {
        return polish;
    }

    public VerbExamine examine() {
        return examine;
    }

    public VerbIlluminate illuminate() {
        return illuminate;
    }

    public VerbDarken darken() {
        return darken;
    }

    public VerbDrop drop() {
        return drop;
    }

    public VerbHoard hoard() {
        return verbHoard;
    }

    public VerbInspect inspect() {
        return inspect;
    }

    public VerbInventory handleInventoryVerb() {
        return inventoryVerbHandler;
    }

    public VerbScore handleScoreVerb() {
        return scoreVerbHandler;
    }

    public LookResultOutput look(MudLocation mudLocation, List<MudLocationDirectionGate> gatesHere, List<MudUser> otherUsers) {

        LookResultOutput output = new LookResultOutput();

        // TODO: if player held a reference to a mudlocation then this would not be necessary to pass in
        MudLocation whereAmI = mudLocation;

        // a wizard might be no-where so allow for this
        if(whereAmI==null || whereAmI.getLocationId()==null){
            return output;
        }

        // am I in darkness or can I see?

        // TODO if multiple players and one has a light then I can see

        boolean isItTooDarkToSee = whereAmI.isLocationDark() && (!canISeeInTheDark());

        LookLocation lookLocation = whereAmI.getLookLocation();

        if(isItTooDarkToSee){
            lookLocation.setDescription("It is too dark to see");
            output.setLocation(lookLocation);
            return output;
        }

        // I can see everything!

        output.setLocation(lookLocation);

        // does not display secret exits
        output.setVisibleExits(whereAmI.getVisibleExits());

        // find the gates status for visible exists

        List<VisibleGate> visibleGates=null;

        // TODO: if gates were on the location then this would not be necessary
        //List<MudLocationDirectionGate> gatesHere = gateManager.getGatesHere(whereAmI);

        for(MudLocationDirectionGate theGate : gatesHere){
            if(theGate.isVisible()){
                VisibleGate visibleGate = new VisibleGate(theGate.getDirectionFor(whereAmI), theGate.isOpen(), theGate.shortDescription(), theGate.closedDescription());
                if (visibleGates == null) {
                    visibleGates = new ArrayList<>();
                }
                visibleGates.add(visibleGate);
            }
        }


        output.setVisibleGates(visibleGates);


        /*
         * Location Objects
         */

        List<MudLocationObject> locationObjects = whereAmI.objects().itemsView();
        List<IdDescriptionPair> visibleLocationObjects = new ArrayList<>();

        if(locationObjects.size()>0) {
            for (MudLocationObject anObject : locationObjects) {
                if(!anObject.isSecret()) {
                    visibleLocationObjects.add(new IdDescriptionPair(anObject.getObjectId(), anObject.getDescription()));
                }
            }

            if(visibleLocationObjects.size()>0) {
                IdDescriptionPair[] lookVisibleObjects = new IdDescriptionPair[visibleLocationObjects.size()];
                output.setVisibleObjects(visibleLocationObjects.toArray(lookVisibleObjects));
            }

        }



        /*
         *
         * Collectables
         *
         */


        List<MudCollectable> collectables = whereAmI.collectables().itemsView();
        List<IdDescriptionPair> visibleCollectables = new ArrayList<>();

        if(collectables.size()>0) {
            for (MudCollectable aCollectable : collectables) {
                visibleCollectables.add(new IdDescriptionPair(aCollectable.getCollectableId(),aCollectable.getDescription() ));
            }
        }

        IdDescriptionPair[] lookVisibleCollectables = new IdDescriptionPair[visibleCollectables.size()];
        output.setVisibleCollectables(visibleCollectables.toArray(lookVisibleCollectables)) ;


        /*
         * Hoarding
         */
        if(whereAmI.canHoardTreasureHere()){
            output.setTreasureHoardContents(new InventoryReport(userName(), treasureHoard().asIdDescriptionPairs()));
        }

        /*
         *
         * Other Users in the same location
         *
         */


        ArrayList<LookPlayer> otherPeopleHere = new ArrayList<>();

        // TODO: if players 'told' the location when they arrived and left then the location would know who was in it
        // and we would not need to pass in the list of users
        for(MudUser aUser : otherUsers){
            // wizards are also users, but they might be no-where so don't assume that the user is somewhere
            if(aUser.getLocationId() != null && aUser.getLocationId().contentEquals(whereAmI.getLocationId())) {
                // this user is also here
                if (!aUser.userName().contentEquals(userName())) {
                    // users might be invisible if they are wizards or have an artifact (future)
                    if(aUser.isVisibleToOthers()) {
                        otherPeopleHere.add(new LookPlayer(aUser.userName(), aUser.displayName()));
                    }
                }
            }
        }


        if(otherPeopleHere.size()>0) {
            LookPlayer[] otherPeopleHereArray = new LookPlayer[otherPeopleHere.size()];
            output.setOtherPeopleHere(otherPeopleHere.toArray(otherPeopleHereArray));
        }

        return output;
    }

}
