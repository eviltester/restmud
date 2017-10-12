package uk.co.compendiumdev.restmud.engine.game.scripting;

/**
 * Created by Alan on 07/06/2016.
 */
public class ScriptClause {
    private final String command;
    private final String parameters;
    private int token;

    public ScriptClause(String command, String parameters) {
        this.command = command.toLowerCase();
        this.parameters = parameters.toLowerCase();
    }

    public String getParameter() {
        return parameters;
    }

    public String getCommand() {
        return command;
    }

    public void setCommandToken(int token) {
        this.token = token;
    }

    public int getToken() {
        return token;
    }

    public ScriptClause createCloneCopy() {
        ScriptClause clonedClause = new ScriptClause(this.command, this.parameters);
        clonedClause.setCommandToken(this.token);
        return clonedClause;
    }
}
