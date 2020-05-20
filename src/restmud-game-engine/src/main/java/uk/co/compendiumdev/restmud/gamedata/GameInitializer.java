package uk.co.compendiumdev.restmud.gamedata;

import uk.co.compendiumdev.restmud.engine.game.GameGenerator;
import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.MudUser;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinition;
import uk.co.compendiumdev.restmud.engine.game.gamedefinition.MudGameDefinitionSerialiser;
import uk.co.compendiumdev.restmud.engine.game.verbmodes.DefaultVerbModes;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.FromDefinitionGameGenerator;
import uk.co.compendiumdev.restmud.gamedata.games.gamedefinitions.FromJsonGameGenerator;

import java.util.Map;
import java.util.Properties;

public class GameInitializer {

    public static final String RESTMUDEFAULTUSERS = "RESTMUDEFAULTUSERS";
    public static final String GAMESECRETCODE = "GAMESECRETCODE";
    public static final String WIZAUTHCODE = "WIZAUTHCODE";


    private final MudGame game;
    private boolean displayStartUpMessages;
    private String wizardAuthHeader;

    public GameInitializer(){
        this.game = new MudGame();
        displayStartUpMessages = true;
    }

    public void setDisplayStartupMessages(boolean display){
        this.displayStartUpMessages=display;
    }


    public void generate(String game_name) {
        GameGenerator gameGenerator;

        if(game_name.endsWith(".json")) {
            String defn = new MudGameDefinitionSerialiser().readJsonFromResource("/games/" + game_name);
            gameGenerator = new FromJsonGameGenerator(defn);
        }else{
            throw new RuntimeException("Unrecognised Game Name " + game_name);
        }

        this.generate(gameGenerator);

    }


    public void generate(MudGameDefinition aGameDefn) {
        GameGenerator gameGenerator = new FromDefinitionGameGenerator(aGameDefn);
        this.generate(gameGenerator);
    }

    public void generateFromJson(String jsonDefn) {

        GameGenerator gameGenerator = new FromJsonGameGenerator(jsonDefn);
        this.generate(gameGenerator);

    }

    public void generate(GameGenerator generator) {

        generator.generateInto(game);
        generate();

    }

    public void generate() {

        game.tokenizeScriptConditions();


        game.ensureGameHasJunkRoom();
        game.setVerbModes(new DefaultVerbModes().generate());

        RandomStringGenerator randomStringG = new RandomStringGenerator();

        // TODO set secret code from commandline argument variable if present
        String defaultSecretCode=null;
        Map<String, String> env = System.getenv();
        if(env.containsKey(GAMESECRETCODE)) {
            defaultSecretCode = env.get(GAMESECRETCODE);
        }
        final Properties properties = System.getProperties();
        if(properties.containsKey(GAMESECRETCODE)){
            defaultSecretCode = (String)properties.get(GAMESECRETCODE);
        }


        String secretCode = randomStringG.generateAlpha(10);

        if(defaultSecretCode!=null){
            secretCode = defaultSecretCode;
        }

        logStartupMessage(String.format("SECRET GAME CODE for REGISTRATION is %s", secretCode));
        game.getSecretGameRegistrationCode().set(secretCode);

    }


    public void setWizardAuthHeader(String wizardAuthHeader) {
        this.wizardAuthHeader = wizardAuthHeader;
    }

    public void addAWizard() {
        MudUser wizard = new MudUser("Wizard of RestMud", "wiz").setAsWizard().setVisibilityToOthers(false);

        Map<String, String> env = System.getenv();
        String defaultWizAuthCode = env.get(WIZAUTHCODE);

        if(defaultWizAuthCode!=null){
            wizard.setPassword(defaultWizAuthCode);
            wizard.setAuthToken(defaultWizAuthCode);
        }


        logStartupMessage(String.format("Wizard (wiz) password (%s) is %s", wizardAuthHeader, wizard.getAuthToken()));
        game.getUserManager().add(wizard);
    }

    public void addDefaultUsers(){
        Map<String, String> env = System.getenv();
        String defaultUserNamesEnv = env.get(RESTMUDEFAULTUSERS);

        if(defaultUserNamesEnv!=null){

            logStartupMessage("Processing Default Users in " + RESTMUDEFAULTUSERS);
            addUsersFromString(defaultUserNamesEnv);

        }else{
            logStartupMessage("No Default Users Found in " + RESTMUDEFAULTUSERS);
        }
    }

    public MudGame getGame() {
        return game;
    }

    public void addDefaultUser(String displayName, String uName, String password) {

        logStartupMessage(String.format("Adding user %s(%s)", displayName, uName));
        String gameStartLocation = game.getStartLocationId();

        if(displayName.length()>0){
            if(uName.length()>0){
                game.getUserManager().add(new MudUser(displayName,uName,password).setLocationId(gameStartLocation));
                logStartupMessage(String.format("Added user %s(%s)", displayName, uName));
                return;
            }
        }

        logStartupMessage(String.format("Could not add user %s(%s)", displayName, uName));
    }

    public void logStartupMessage(String message){
        if(displayStartUpMessages)
            System.out.println(message);
    }

    /**
     * Users string is a set of csv user attributes
     * Each user is separated by "|"
     * Each attribute is separated by ","
     * Attributes are in the order: username, displayname, password
     * If password is not supplied then the default is "password"
     *
     * @param userDefinitions
     */
    public void addUsersFromString(String userDefinitions) {

        if(userDefinitions!=null){

            String userNamePairs[] = userDefinitions.split("\\|");
            for(String username : userNamePairs){

                String userAttributes[] = username.split(",");

                String displayName;
                String uName;
                String password = "password";

                if(userAttributes.length>0) {


                    // by default use for both displayname and username
                    displayName = userAttributes[0].trim();
                    uName = displayName.replace(" ", "");


                    if(userAttributes.length>=2){
                        displayName = userAttributes[0].trim();
                        uName = userAttributes[1].trim();
                    }

                    if(userAttributes.length>=3){
                        password = userAttributes[2].trim();
                    }

                    addDefaultUser(displayName, uName, password);
                }
            }
        }
    }


}
