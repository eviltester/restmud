package uk.co.compendiumdev.restmud.web.server.apistates;


public class AuthenticatedUserState {

    private final String description;
    private AuthenticatedUserStates state;

    public AuthenticatedUserState(AuthenticatedUserStates state, String stateDetails) {
        this.state = state;
        this.description = stateDetails;
    }

    public AuthenticatedUserStates getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }
}
