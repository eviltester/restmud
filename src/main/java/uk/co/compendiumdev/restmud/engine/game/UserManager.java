package uk.co.compendiumdev.restmud.engine.game;

import uk.co.compendiumdev.restmud.output.json.jsonReporting.GetUserDetails;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alan on 04/06/2016.
 */
public class UserManager {

    private final Map<String, MudUser> users;

    public UserManager() {
        users = new HashMap<>();
    }

    public MudUser add(MudUser mudUser) {
        this.users.put(mudUser.userName().toLowerCase(), mudUser);
        return mudUser;
    }

    public List<MudUser> getUsers() {
        List<MudUser> theUsers = new ArrayList<MudUser>();
        theUsers.addAll(users.values());
        return theUsers;
    }

    public int numberOfUsers() {
        return users.size();
    }

    public MudUser getUser(String username) {
        return users.get(username.toLowerCase());
    }

    public GetUserDetails getUserDetails(String username) {
        MudUser user = getUser(username);
        GetUserDetails userDetails = null;

        if (user != null) {
            userDetails = new GetUserDetails(user.userName(), user.displayName(), user.getAuthToken());
        }

        return userDetails;

    }

    public MudUser getUserbyUsername(String username) {

        if (username == null) {
            return null;
        }

        String compareName = username.toLowerCase();

        for (MudUser aUser : getUsers()) {
            if (aUser.userName().toLowerCase().contentEquals(compareName)) {
                return aUser;
            }
        }

        return null;
    }

    public MudUser getUserbyUsernamePassword(String username, String password) {

        if (username == null) {
            return null;
        }

        MudUser aUser = getUserbyUsername(username);
        if (aUser != null) {
            if (password.contentEquals(aUser.getPassword())) {
                return aUser;
            }
        }
        return null;
    }

    public MudUser findUserWithAuthToken(String authToken) {

        if (authToken == null) {
            return null;
        }

        for (MudUser aUser : getUsers()) {
            if (aUser.getAuthToken().contentEquals(authToken)) {
                return aUser;
            }
        }

        return null;
    }

    public List<IdDescriptionPair> getUsersAsIdDescriptionPairs() {
        List<IdDescriptionPair> pairs = new ArrayList<IdDescriptionPair>();

        for (MudUser user : getUsers()) {

            String id = user.userName();
            String description = "[" + id + "] " + user.displayName();
            pairs.add(new IdDescriptionPair(id, description));
        }
        return pairs;
    }
}
