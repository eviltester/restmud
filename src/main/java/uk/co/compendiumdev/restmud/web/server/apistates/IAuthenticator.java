package uk.co.compendiumdev.restmud.web.server.apistates;

import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import spark.Request;

/**
 * Created by Alan on 09/06/2016.
 */
public interface IAuthenticator {
    void addAuthenticatedUser(Request request, MudUser user);

    void removeAuthenticatedUser(Request request);

    MudUser getAuthenticatedUserFromSession(Request request);

    boolean isWizard(Request request, MudGame game);

    boolean isMultiPlayer();

    AuthenticatedUserState getUserAuthenticatedState(Request request, String username, MudGame game);

    StringBuilder checkValidUserDetails(String username, String displayname, String password);

    RegisterUserState currentRegistrationStateOfUser(Request request,
                                                     String username, String displayname, String password, String secretGameCode,
                                                     MudGame game);

    boolean usernameMatchesSession(String username, Request request);

    void addUserToLogMeOutQueue(String username);

    void toggleAuthMode(String mode, boolean value);

    LoginState loginUser(Request request, String username, String password);

    RegisterUserState registerUser(Request request, String username, String displayname, String password, String secretGameCode);

    boolean get_allow_login_form();

    boolean get_allow_auth_header();

    boolean get_allow_basic_auth();
}
