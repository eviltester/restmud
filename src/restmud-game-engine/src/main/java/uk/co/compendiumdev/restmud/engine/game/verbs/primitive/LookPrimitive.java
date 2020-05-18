package uk.co.compendiumdev.restmud.engine.game.verbs.primitive;

import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocation;
import uk.co.compendiumdev.restmud.engine.game.locations.MudLocationDirectionGate;
import uk.co.compendiumdev.restmud.engine.game.things.MudCollectable;
import uk.co.compendiumdev.restmud.engine.game.things.MudLocationObject;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.*;

import java.util.ArrayList;
import java.util.List;

public class LookPrimitive {

    public LookResultOutput perform(final MudUser player, final MudLocation mudLocation, final List<MudLocationDirectionGate> gatesHere, final List<MudUser> otherUsers) {

        LookResultOutput output = new LookResultOutput();

        // TODO: if player held a reference to a mudlocation then this would not be necessary to pass in
        MudLocation whereAmI = mudLocation;

        // a wizard might be no-where so allow for this
        if (whereAmI == null || whereAmI.getLocationId() == null) {
            return output;
        }

        // am I in darkness or can I see?

        // TODO if multiple players and one has a light then I can see

        boolean isItTooDarkToSee = whereAmI.isLocationDark() && (!player.canISeeInTheDark());

        LookLocation lookLocation = whereAmI.getLookLocation();

        if (isItTooDarkToSee) {
            lookLocation.setDescription("It is too dark to see");
            output.setLocation(lookLocation);
            return output;
        }

        // I can see everything!

        output.setLocation(lookLocation);

        // does not display secret exits
        output.setVisibleExits(whereAmI.getVisibleExits());

        // find the gates status for visible exists

        List<VisibleGate> visibleGates = null;

        // TODO: if gates were on the location then this would not be necessary
        //List<MudLocationDirectionGate> gatesHere = gateManager.getGatesHere(whereAmI);

        for (MudLocationDirectionGate theGate : gatesHere) {
            if (theGate.isVisible()) {
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

        if (locationObjects.size() > 0) {
            for (MudLocationObject anObject : locationObjects) {
                if (!anObject.isSecret()) {
                    visibleLocationObjects.add(new IdDescriptionPair(anObject.getObjectId(), anObject.getDescription()));
                }
            }

            if (visibleLocationObjects.size() > 0) {
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

        if (collectables.size() > 0) {
            for (MudCollectable aCollectable : collectables) {
                visibleCollectables.add(new IdDescriptionPair(aCollectable.getCollectableId(), aCollectable.getDescription()));
            }
        }

        IdDescriptionPair[] lookVisibleCollectables = new IdDescriptionPair[visibleCollectables.size()];
        output.setVisibleCollectables(visibleCollectables.toArray(lookVisibleCollectables));


        /*
         * Hoarding
         */
        if (whereAmI.canHoardTreasureHere()) {
            output.setTreasureHoardContents(new InventoryReport(player.userName(), player.treasureHoard().asIdDescriptionPairs()));
        }

        /*
         *
         * Other Users in the same location
         *
         */


        ArrayList<LookPlayer> otherPeopleHere = new ArrayList<>();

        // TODO: if players 'told' the location when they arrived and left then the location would know who was in it
        // and we would not need to pass in the list of users
        for (MudUser aUser : otherUsers) {
            // wizards are also users, but they might be no-where so don't assume that the user is somewhere
            if (aUser.getLocationId() != null && aUser.getLocationId().contentEquals(whereAmI.getLocationId())) {
                // this user is also here
                if (!aUser.userName().contentEquals(player.userName())) {
                    // users might be invisible if they are wizards or have an artifact (future)
                    if (aUser.isVisibleToOthers()) {
                        otherPeopleHere.add(new LookPlayer(aUser.userName(), aUser.displayName()));
                    }
                }
            }
        }


        if (otherPeopleHere.size() > 0) {
            LookPlayer[] otherPeopleHereArray = new LookPlayer[otherPeopleHere.size()];
            output.setOtherPeopleHere(otherPeopleHere.toArray(otherPeopleHereArray));
        }

        return output;
    }
}
