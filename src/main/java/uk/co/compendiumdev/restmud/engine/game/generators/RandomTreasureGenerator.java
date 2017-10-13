package uk.co.compendiumdev.restmud.engine.game.generators;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alan on 10/06/2016.
 */
public class RandomTreasureGenerator {

    private final MudGame game;
    private final Random rnd;
    private List<String> locationKeys;

    // randomly generate treasures
    private final String[] treasureTypes = {"ring", "crown", "nugget", "pendant", "sceptre", "scarab", "coin", "dagger", "pig"};
    private final String[] treasureAdjectives = {"shiny", "exuberant", "glowing", "incredible", "amazing", "expensive"};
    private final String[] treasureMaterials = {"gold", "ruby encrusted", "platinum", "silver", "diamond encrusted", "bejeweled"};
    private final String[] dudTypes = {"bread", "oranges", "cups", "sweets", "belly button fluff", "old chewing gum", "thing"};
    private final String[] dudAdjectives = {"mouldy", "smelly", "rotten", "green"};

    public RandomTreasureGenerator(MudGame game) {
        this.game = game;
        this.rnd = new Random();
        this.locationKeys = game.getGameLocations().getLocationKeys();
    }

    public void generateIntoTheseLocations(List<String> locationKeys) {
        this.locationKeys = locationKeys;
    }

    /** turn some of the locations into hoards
     *  the hoard setting is random - we randomly choose numberOfHoardsToChoose
     *  we might choose the same location more than once
     * @param numberOfHoardsToChoose
     */
    public List<String> createHoards(int numberOfHoardsToChoose) {

        List<String> hoardsSet= new ArrayList<>();

        for(int x=0;x<numberOfHoardsToChoose;x++){
            // randomly choose some locations to make hoards

            MudLocation location=game.getGameLocations().get(locationKeys.get(rnd.nextInt(this.locationKeys.size())));
            location.setCanHoardTreasureHere(true);
            hoardsSet.add(location.getLocationId());
        }

        return hoardsSet;
    }

    // TODO - these methods below are all pretty similar, refactor to simplify

    public List<String> createHoardableTreasures(int numberOfTreasures) {

        List<String> treasures = new ArrayList<>();
        int total_hoard_score=0;

        //System.out.println("Generating " + numberOfTreasures);

        int startIdPostFix = game.getGameCollectables().count();

        // create some hoardable treasures
        for(int x=0;x<numberOfTreasures;x++){

            // randomly generate x hoardable treasures
            int treasureType = rnd.nextInt(treasureTypes.length);
            int treasureAdjective = rnd.nextInt(treasureAdjectives.length);
            int treasureMaterial = rnd.nextInt(treasureMaterials.length);
            String thing_name = treasureTypes[treasureType];
            String thing_id = thing_name + "_" + (x+startIdPostFix);
            thing_id = thing_id.replaceAll(" ", "_");

            if(rnd.nextBoolean()){
                thing_name = treasureAdjectives[treasureAdjective] + " " + thing_name;
            }

            if(rnd.nextBoolean()){
                thing_name = treasureMaterials[treasureMaterial] + " " + thing_name;
            }

            int hoardScore = rnd.nextInt(300) + 100;

            total_hoard_score += hoardScore;


            // add to random location
            MudLocation location = game.getGameLocations().get(locationKeys.get(rnd.nextInt(this.locationKeys.size())));

            MudCollectable aCollectable = new MudCollectable(thing_id ,"A " + thing_name);
            aCollectable.canHoard(true,hoardScore);

            //System.out.println("Add treasure " + aCollectable.getCollectableId() + " to " + location.getLocationId());

            game.addCollectable(aCollectable, location);


            treasures.add(aCollectable.getCollectableId());
        }


        game.setTotalScore( game.getTotalScore() + total_hoard_score);
        return treasures;
    }

    public List<String> createNonHoardableTreasures(int numberOfTreasures) {

        List<String> treasures = new ArrayList<>();
        // create some non hoardable treasures

        int startIdPostFix = game.getGameCollectables().count();

        for(int x=0;x<numberOfTreasures;x++){

            // randomly generate x hoardable treasures
            int treasureType = rnd.nextInt(treasureTypes.length);
            int treasureAdjective = rnd.nextInt(treasureAdjectives.length);
            int treasureMaterial = rnd.nextInt(treasureMaterials.length);
            String thing_name = treasureTypes[treasureType];
            String thing_id = thing_name + "_0" + (x+startIdPostFix);
            thing_id = thing_id.replaceAll(" ", "_");

            if(rnd.nextBoolean()){
                thing_name = treasureAdjectives[treasureAdjective] + " " + thing_name;
            }

            if(rnd.nextBoolean()){
                thing_name = treasureMaterials[treasureMaterial] + " " + thing_name;
            }

            // add to random location
            MudLocation location = game.getGameLocations().get(locationKeys.get(rnd.nextInt(this.locationKeys.size())));

            MudCollectable aCollectable = new MudCollectable(thing_id,"A " + thing_name);
            //aCollectable.canHoard(true,hoardScore);
            game.addCollectable(aCollectable, location);


            treasures.add(aCollectable.getCollectableId());
        }

        return treasures;
    }

    public List<String> createHoardableJunk(int numberOfJunkItems) {

        List<String> treasures = new ArrayList<>();

        int startIdPostFix = game.getGameCollectables().count();
        // create some hoardable junk - Beware!!!!  Beware!!!!
        for(int x=0;x<numberOfJunkItems;x++){

            // randomly generate x hoardable treasures
            int dudType = rnd.nextInt(dudTypes.length);
            int dudAdjective = rnd.nextInt(dudAdjectives.length);

            String thing_name = dudTypes[dudType];
            String thing_id = thing_name + "_" + (x+startIdPostFix);
            thing_id = thing_id.replaceAll(" ", "_");

            if(rnd.nextBoolean()){
                thing_name = dudAdjectives[dudAdjective] + " " + thing_name;
            }

            int hoardScore = rnd.nextInt(200);
            hoardScore *=-1;
            //total_score += hoardScore;

            // add to random location
            MudLocation location = game.getGameLocations().get(locationKeys.get(rnd.nextInt(this.locationKeys.size())));

            MudCollectable aCollectable = new MudCollectable(thing_id,"Some " + thing_name);
            aCollectable.canHoard(true,hoardScore);
            game.addCollectable(aCollectable, location);

            treasures.add(aCollectable.getCollectableId());
        }

        return treasures;
    }

    public List<String> createNonHoardableJunk(int numberOfJunkItems) {

        List<String> treasures = new ArrayList<>();

        int startIdPostFix = game.getGameCollectables().count();
        // create some non hoardable junk - Beware!!!!
        for(int x=0;x<numberOfJunkItems;x++){

            // randomly generate x hoardable treasures
            int dudType = rnd.nextInt(dudTypes.length);
            int dudAdjective = rnd.nextInt(dudAdjectives.length);

            String thing_name = dudTypes[dudType];
            String thing_id = thing_name + "_0" + (x+startIdPostFix);
            thing_id = thing_id.replaceAll(" ", "_");

            if(rnd.nextBoolean()){
                thing_name = dudAdjectives[dudAdjective] + " " + thing_name;
            }

            //int hoardScore = rnd.nextInt(-200);
            //total_score += hoardScore;

            // add to random location
            MudLocation location = game.getGameLocations().get(locationKeys.get(rnd.nextInt(this.locationKeys.size())));

            MudCollectable aCollectable = new MudCollectable(thing_id,"Some " + thing_name);
            //aCollectable.canHoard(true,hoardScore);
            game.addCollectable(aCollectable, location);


            treasures.add(aCollectable.getCollectableId());
        }

        return treasures;

    }
}
