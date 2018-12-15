package uk.co.compendiumdev.restmud.bottest;

import uk.co.compendiumdev.restmud.api.RestMudAPI;
import uk.co.compendiumdev.restmud.api.RestMudResponseProcessor;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotStrategy;
import uk.co.compendiumdev.restmud.bottest.botstrategies.RestMudBotWaitingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RestMudTestBot {
    private String userName;
    final RestMudAPI api;
    private List<RestMudBotStrategy> actionStrategies;
    private List<RestMudBotWaitingStrategy> waitingStrategies;
    private String password;
    private String registrationSecretCode;
    private boolean registrationNeed;
    private boolean registered;

    public RestMudTestBot(String restmudUrl) {
        api = new RestMudAPI(restmudUrl);
        actionStrategies = new ArrayList<RestMudBotStrategy>();
        waitingStrategies = new ArrayList<RestMudBotWaitingStrategy>();
        registrationNeed = false; // by default assume we do not need to register
        api.userNeedsToRegister(this.registrationNeed);
    }


    public RestMudTestBot setUserName(String userName) {
        this.userName = userName;
        api.setForUser(userName);
        return this;
    }

    public RestMudAPI api(){
        return this.api;
    }

    public RestMudTestBot setPassword(String password) {
        this.password = password;
        api.setPassword(password);
        return this;
    }


    public void addActionStrategy(RestMudBotStrategy aStrategy) {
        aStrategy.setApi(api);
        this.actionStrategies.add(aStrategy);
    }

    public void addWaitingStrategy(RestMudBotWaitingStrategy aStrategy) {
        this.waitingStrategies.add(aStrategy);
    }

    public RestMudResponseProcessor executeARandomActionStrategy() {
        if(actionStrategies==null){
            return null;
        }
        if(actionStrategies.size()==0) {
            return null;
        }

        int chosenStrategy = new Random().nextInt(actionStrategies.size());
        return actionStrategies.get(chosenStrategy).execute();
    }

    public void executeARandomWaitingStrategy() throws InterruptedException {
        if(waitingStrategies==null){
            return;
        }
        if(waitingStrategies.size()==0) {
            return;
        }

        int chosenStrategy = new Random().nextInt(waitingStrategies.size());
        waitingStrategies.get(chosenStrategy).execute();
    }

    public void waitAWhile(){
        try {
            executeARandomWaitingStrategy();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public RestMudTestBot setRegistrationSecretCode(String registrationSecretCode) {
        this.registrationSecretCode = registrationSecretCode;
        api.setRegistrationSecretCode(registrationSecretCode);
        return this;
    }

    public RestMudTestBot needsToRegisterOnSystem(boolean registrationNeed) {
        this.registrationNeed = registrationNeed;
        return this;
    }

    public RestMudTestBot register() {
        if(this.registrationSecretCode==null || this.userName==null || this.password==null){
            new RuntimeException("User needs 'secret' code, username and password to register");
        }
        RestMudResponseProcessor returnMsg = api.register();
        if(returnMsg.isSuccessful()){
            this.registered = true;
        }
        return this;
    }

    public void login() {
        if(registrationNeed && !registered){
            new RuntimeException("User needs to register first");
        }
        this.api.login(this.password);
    }

    public String getUserName() {
        return userName;
    }
}
