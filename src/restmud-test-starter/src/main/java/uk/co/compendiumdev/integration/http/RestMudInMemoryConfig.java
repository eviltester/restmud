package uk.co.compendiumdev.integration.http;

public class RestMudInMemoryConfig {
    private String multiPlayerGameMode;
    private String configPort;
    private String requiredRegistrationCode;
    private String useGame;

    public RestMudInMemoryConfig(){
        //defaults
        this.configPort ="4567";
        this.multiPlayerGameMode = "single";
        requiredRegistrationCode = null;
        useGame=null;

    }
    public RestMudInMemoryConfig port(final String desiredport) {
        this.configPort = desiredport;
        return this;
    }

    public RestMudInMemoryConfig playerMode(final String multiORsingle) {
        this.multiPlayerGameMode = multiORsingle;
        return this;
    }

    public RestMudInMemoryConfig registrationCode(final String desiredRegistrationCode) {
        this.requiredRegistrationCode = desiredRegistrationCode;
        return this;
    }

    public String playerMode() {
        return multiPlayerGameMode;
    }

    public String port() {
        return configPort;
    }

    public boolean hasRegistrationCode() {
        return this.requiredRegistrationCode!=null;
    }

    public String registrationCode() {
        return this.requiredRegistrationCode;
    }

    public RestMudInMemoryConfig gameNamed(final String desiredGame) {
        this.useGame = desiredGame;
        return this;
    }

    public boolean hasDesiredGame(){
        return this.useGame!=null;
    }

    public String gameNamed() {
        return this.useGame;
    }
}
