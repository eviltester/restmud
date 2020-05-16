package uk.co.compendiumdev.restmud.web.server.apistates;


import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import spark.Request;

import java.util.concurrent.ConcurrentHashMap;

public class SinglePlayerAuthenticator implements IAuthenticator{


    // Single player authenticator to force single user play
    // single user is always user/password
    public static final String SINGLE_PLAYER_USERNAME = "user";
    public static final String SINGLE_PLAYER_PASSWORD = "password";
    public static final String SINGLE_PLAYER_DISPLAYNAME = "You";

    public static final String WIZARD_AUTH_HEADER = "X-RESTMUD-WIZ-AUTH";
    public static final String USER_AUTH_HEADER = "X-RESTMUD-USER-AUTH";

    private final MudGame game;

    private boolean allow_login_form=true;
    private boolean allow_basic_auth=true;
    private boolean allow_auth_header=true;

    public static final String USER_SESSION_ID = "user";

    private ConcurrentHashMap<String, Boolean> logMeOutQueue = new ConcurrentHashMap<>();

    public SinglePlayerAuthenticator(MudGame game) {
        this.game = game;
    }

    public void addAuthenticatedUser(Request request, MudUser user) {
        request.session().attribute(USER_SESSION_ID, user);
    }

    public void removeAuthenticatedUser(Request request) {
        // in single player mode you cannot logout

        // should possibly invalidate the session, this leaves the session active but adds a user to it
        //request.session().removeAttribute(USER_SESSION_ID);
    }

    // allow us to force login for a single user if login called
    public MudUser getAuthenticatedUserFromSession(Request request) {
        MudUser user = request.session().attribute(USER_SESSION_ID);

        // in single player mode force the user to be logged in
        if(user==null){
            user = game.getUserManager().getUserbyUsernamePassword(SINGLE_PLAYER_USERNAME, SINGLE_PLAYER_PASSWORD);
            addAuthenticatedUser(request, user);
            return user;
        }

        return request.session().attribute(USER_SESSION_ID);
    }


    public boolean isWizard(Request request, MudGame game){

        // does request have an X-RESTMUD-WIZ-AUTH header?
        // does a user with that Auth code exist?
        // are they a wizard? if so, return true

        MudUser authUser = game.getUserManager().findUserWithAuthToken(request.headers(WIZARD_AUTH_HEADER));
        if(authUser!=null && authUser.isWizard()){
            return true;
        }


        // find the user in the request
        authUser = getAuthenticatedUserFromSession(request);

        // perform any wiz user checks here
        // if the user loggedin as a wizard then find out now
        if(isUserAWizard(authUser, game)){
            return true;
        }

        // if request has any wizardy headers then check here

        return false;
    }

    @Override
    public boolean isMultiPlayer() {
        return false;
    }

    public AuthenticatedUserState getUserAuthenticatedState(Request request, String username, MudGame game) {


        username=SINGLE_PLAYER_USERNAME;

        // wizard overrides not for single user
        // wizard authentication trumps everything
        //if(isWizard(request, game)){
        //    return new AuthenticatedUserState(AuthenticatedUserStates.SUCCESS_AUTHENTICATED_USER,  "Wizards are always welcome");
        //}


        // does the request have a user authentication header that matches a user?
        MudUser authUser=null;

        // we can switch on or off authentication via header
        if(allow_auth_header) {
            authUser = game.getUserManager().findUserWithAuthToken(request.headers(USER_AUTH_HEADER));
        }

        if(authUser!=null){
            // if the auth code username matches the user we are trying to access
            if(authUser.userName().toLowerCase().contentEquals(username.toLowerCase())) {
                return new AuthenticatedUserState(
                        AuthenticatedUserStates.SUCCESS_AUTHENTICATED_USER,
                        String.format("This User request is Authenticated with %s", SinglePlayerAuthenticator.USER_AUTH_HEADER));
            }
        }

        // does the request have an authenticated user in it
        authUser = getAuthenticatedUserFromSession(request);

        if(authUser != null) {
            // if user is in the logout queue then invalidate their session
            if (logMeOutQueue.containsKey(authUser.userName())) {
                removeAuthenticatedUser(request);
                logMeOutQueue.remove(username);
                authUser = null;
            }
        }


        // if no authenticated user in session then check basic authentication headers and create session for user
        if(authUser == null) {

            // for single player force login as user
            authUser = game.getUserManager().getUserbyUsernamePassword(SINGLE_PLAYER_USERNAME, SINGLE_PLAYER_PASSWORD);
            addAuthenticatedUser(request, authUser);
            return new AuthenticatedUserState(AuthenticatedUserStates.SUCCESS_AUTHENTICATED_USER, "User is Authenticated");


//            // is there a username/password (Basic authentication in the message, if so log them in if it is correct)
//            // https://groups.google.com/forum/#!topic/sparkjava/HymQ04gVTNs
//            String auth = null;
//
//            // we can toggle on and off the Basic Auth
//            if(allow_basic_auth) {
//                auth = request.headers("Authorization");
//            }
//
//            if(auth != null && auth.startsWith("Basic")) {
//                String b64Credentials = auth.substring("Basic".length()).trim();
//                String credentials = new String(Base64.getDecoder().decode(b64Credentials));
//                String[] credentialsParsed = credentials.split(":");
//                String credentialsUsername = credentialsParsed[0];
//                String credentialsPassword = credentialsParsed[1];
//
//                authUser = game.getUserManager().getUserbyUsernamePassword(credentialsUsername, credentialsPassword);
//                if(authUser==null){
//
//                    return new AuthenticatedUserState(AuthenticatedUserStates.ERROR_INVALID_BASIC_AUTH_DETAILS, "Basic Auth Credentials User not Found");
//
//                }else {
//                    // user is registered and credentials are good, authenticate user
//                    addAuthenticatedUser(request, authUser);
//                    return new AuthenticatedUserState(AuthenticatedUserStates.SUCCESS_AUTHENTICATED_USER, "User is Authenticated");
//                }
//            }
//
//            // if a non Basic Authorization header was supplied then we can't authenticate the user
//            if(authUser==null) {
//                // user is not authenticated
//                return new AuthenticatedUserState(AuthenticatedUserStates.ERROR_INVALID_BASIC_AUTH_DETAILS, "Basic Auth Credentials User not Found");
//            }
        }




        // check session user against the users in game
        MudUser profileUser = game.getUserManager().getUserbyUsername(username);
        if(profileUser == null) {
            // if we are authenticated, but the user does not exist
            return new AuthenticatedUserState(AuthenticatedUserStates.ERROR_USER_NOT_REGISTERED, "Registered User not found");
        }

        // check if username in request matches the username for the session
        if(!authUser.userName().contentEquals(profileUser.userName())){
            return new AuthenticatedUserState(AuthenticatedUserStates.ERROR_DIFFERENT_USER_LOGGED_IN,  "You cannot act on behalf of that user");
        }

        // by default log them in - warning there may be error conditions we have not checked for
        return new AuthenticatedUserState(AuthenticatedUserStates.SUCCESS_AUTHENTICATED_USER,  "User is Authenticated by default");

    }

    private boolean isUserAWizard(MudUser authUser, MudGame game) {

        if(authUser != null){
            MudUser profileUser = game.getUserManager().getUserbyUsername(authUser.userName());
            if(profileUser!=null){
                return profileUser.isWizard();
            }
        }

        // default to false
        return false;
    }

    public StringBuilder checkValidUserDetails(String username, String displayname, String password) {

        username=SINGLE_PLAYER_USERNAME;
        displayname = SINGLE_PLAYER_DISPLAYNAME;
        password =  SINGLE_PLAYER_PASSWORD;

        // are user details valid?
        StringBuilder validationErrors = new StringBuilder();

        if(username==null || username.trim().length()==0){
            validationErrors.append("- username must have a value\n");
        }
        if(displayname==null || displayname.trim().length()==0){
            validationErrors.append("- displayname must have a value\n");
        }
        if(password==null || password.trim().length()==0){
            validationErrors.append("- password must have a value\n");
        }

        return validationErrors;
    }

    public RegisterUserState currentRegistrationStateOfUser(Request request,
                                                            String username, String displayname, String password, String secretGameCode,
                                                            MudGame game) {

        username = SINGLE_PLAYER_USERNAME;
        displayname = SINGLE_PLAYER_DISPLAYNAME;
        password = SINGLE_PLAYER_PASSWORD;

        StringBuilder validationErrors = checkValidUserDetails(username, displayname, password);

        if(validationErrors.length()>0){
            validationErrors.insert(0,"Cannot Register because ");
            return new RegisterUserState(RegisterUserStates.ERROR_INVALID_DETAILS, validationErrors.toString() );
        }

        // is user already logged in?
        MudUser authenticatedUser = getAuthenticatedUserFromSession(request);
        if (authenticatedUser != null) {
            return new RegisterUserState(RegisterUserStates.ERROR_ALREADY_LOGGEDIN,
                    String.format("You cannot register a user because you are already logged in as %s", authenticatedUser.userName()));
        }

        // is game secret code valid?
        if(game.getSecretGameRegistrationCode().isSecretCodeValid(secretGameCode)){
            return new RegisterUserState(RegisterUserStates.INVALID_GAME_CODE_SUPPLIED,
                    String.format("You cannot register this game does not have this code %s", secretGameCode));
        }

        // is username already registered?
        MudUser existingUser = game.getUserManager().getUserbyUsername(username);
        if (existingUser != null) {
            // user is already registered
            return new RegisterUserState(RegisterUserStates.ERROR_ALREADY_REGISTERED, String.format("User %s is already registered.%n", username));
        }

        return new RegisterUserState(RegisterUserStates.USER_DOES_NOT_EXIST, String.format("User %s is not registered.%n", username));}

    public boolean usernameMatchesSession(String username, Request request) {
        username= SINGLE_PLAYER_USERNAME;
        return getAuthenticatedUserFromSession(request).userName().equalsIgnoreCase(username);
    }

    public void addUserToLogMeOutQueue(String username) {
        username=SINGLE_PLAYER_USERNAME;
        logMeOutQueue.put(username, Boolean.TRUE);
    }

    public void toggleAuthMode(String mode, boolean value) {
        if(mode.equalsIgnoreCase("header")){
            allow_auth_header = value;
        }
        if(mode.equalsIgnoreCase("basicauth")){
            allow_basic_auth = value;
        }
        if(mode.equalsIgnoreCase("loginform")){
            allow_login_form = value;
        }
    }

    public LoginState loginUser(Request request, String username, String password) {

        username=SINGLE_PLAYER_USERNAME;
        password=SINGLE_PLAYER_PASSWORD;

        // Error: you are already loggedin
        if(getAuthenticatedUserFromSession(request)!=null){
            return LoginState.ERROR_ALREADY_LOGGEDIN;
        }

        // find the details for a registered user
        MudUser user = game.getUserManager().getUserbyUsernamePassword(username, password);

        if(user==null){
            // Error: your details are not correct
            return LoginState.ERROR_INVALID_DETAILS;
        }
        // Success: you have logged in

        boolean allowLogin = true;

        if(!user.isWizard()){
            // wizard can always login through the GUI
            allowLogin = allow_login_form;
        }

        if(!allowLogin){
            // TODO: should really be a login disabled state
            return LoginState.ERROR_INVALID_DETAILS;
        }

        addAuthenticatedUser(request, user);
        user.setLocationId(game.getStartLocationId());

        return LoginState.SUCCESS_LOGGEDIN;
    }


    public RegisterUserState registerUser(Request request, String username, String displayname, String password, String secretGameCode) {


        username=SINGLE_PLAYER_USERNAME;
        displayname=SINGLE_PLAYER_DISPLAYNAME;
        password=SINGLE_PLAYER_PASSWORD;

        RegisterUserState registerUserState = currentRegistrationStateOfUser(
                request, username, displayname, password, secretGameCode, game);

        if(registerUserState.getState()==RegisterUserStates.USER_DOES_NOT_EXIST) {

            // user does not exist, create user and give them a session
            MudUser newUser = new MudUser(displayname, username, password);
            game.getUserManager().add(newUser);

            addAuthenticatedUser(request, newUser);
            newUser.setLocationId("1");

            return new RegisterUserState(RegisterUserStates.SUCCESS_REGISTERED_USER,
                    String.format("Registered user %s as '%s'", newUser.userName(), newUser.displayName()));

        }else{
            return registerUserState;
        }
    }

    @Override
    public boolean get_allow_login_form() {
        return allow_login_form;
    }

    @Override
    public boolean get_allow_auth_header() {
        return allow_auth_header;
    }

    @Override
    public boolean get_allow_basic_auth() {
        return allow_basic_auth;
    }
}
