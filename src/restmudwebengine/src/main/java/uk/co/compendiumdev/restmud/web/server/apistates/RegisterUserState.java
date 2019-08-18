package uk.co.compendiumdev.restmud.web.server.apistates;


public class RegisterUserState {

    private final String description;
    private RegisterUserStates state;

    public RegisterUserState(RegisterUserStates errorState, String errorDetails) {
        this.state = errorState;
        this.description = errorDetails;
    }

    public RegisterUserStates getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }
}
