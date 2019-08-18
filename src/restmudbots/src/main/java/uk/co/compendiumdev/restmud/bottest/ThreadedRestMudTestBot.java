package uk.co.compendiumdev.restmud.bottest;


import java.util.HashMap;
import java.util.Map;

public class ThreadedRestMudTestBot implements Runnable{
    private final String username;
    private final String password;
    private final String secretCode;
    RestMudTestBot myBot;
    private Thread thread;
    public boolean running=false;
    public boolean shouldStop=false;

    public final String STARTED = "STARTED";
    public final String EXITED = "EXITED";
    public final String LAST_EXECUTED = "EXECUTED";
    public final String INTERRUPTED = "INTERRUPTED";

    public Map<String,Long> states = new HashMap<>();

    public ThreadedRestMudTestBot(String rest_mud_url, String username, String password, String secretRegistrationCode) {
        this.username = username;
        this.password = password;
        this.secretCode = secretRegistrationCode;

        myBot = new RestMudTestBot(rest_mud_url).setUserName(username);

        if(password.length()>0){
            myBot.setPassword(password);

            if(secretRegistrationCode!=null){
                myBot.setRegistrationSecretCode(secretRegistrationCode);
                myBot.needsToRegisterOnSystem(true);
                myBot.register();
            }

            myBot.api.login(password);
        }
    }

    public RestMudTestBot getBot(){
        return myBot;
    }

    public void run() {
        System.out.println("Running " +  username );
        botState(STARTED);
        running=true;
        int threadLoopCount=0;
        try {
            while(!shouldStop){
                System.out.println("Thread: " + username + ", " + threadLoopCount++);
                myBot.executeARandomActionStrategy();
                // Let the thread sleep for a while.
                myBot.executeARandomWaitingStrategy();
                botState(LAST_EXECUTED);
            }
        }catch (InterruptedException e) {
            System.out.println("Thread " +  username + " interrupted.");
            running = false;
            botState(INTERRUPTED);
        }
        System.out.println("Thread " +  username + " exiting.");
        running = false;
        botState(EXITED);
    }

    private void botState(String state) {
        states.put(state, System.currentTimeMillis());
    }

    public void start () {
        System.out.println("Starting " +  username );
        if (thread == null) {
            thread = new Thread (this, username);
            thread.start ();
        }
    }

    public synchronized void  stop(){
        shouldStop=true;
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized boolean alive(){
        if(thread!=null) {
            return thread.isAlive();
        }else{
            return false;
        }
    }

    public int reportOnStates(){
        System.out.println("BOT: " + username);
        System.out.println("STATES:");
        for(String state : states.keySet()){
            System.out.println(state + ": " + states.get(state));
        }
        System.out.println("STATES HIT: " + states.size());

        return states.size();
    }
}
