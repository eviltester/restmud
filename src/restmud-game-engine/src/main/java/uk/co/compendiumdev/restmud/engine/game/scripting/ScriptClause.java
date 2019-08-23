package uk.co.compendiumdev.restmud.engine.game.scripting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 07/06/2016.
 */
public class ScriptClause {
    private final String command;
    private final String parameters;
    private int token;
    private boolean executionMatch;
    private List<ScriptClause> clauses;

    public static ScriptClause or(final ScriptClause ...setClauses) {
        final ScriptClause clause = new ScriptClause("or", "");
        for(ScriptClause aClause : setClauses){
            clause.addToCollection(aClause);
        }
        return clause;
    }

    public static ScriptClause and(final ScriptClause ...setClauses) {
        final ScriptClause clause = new ScriptClause("and", "");
        for(ScriptClause aClause : setClauses){
            clause.addToCollection(aClause);
        }
        return clause;
    }

    public static ScriptClause not(final ScriptClause setClause) {
        final ScriptClause clause = new ScriptClause("not", "");
        clause.addToCollection(setClause.setExecutionMatch(!setClause.executionMatchValue()));
        return clause;
    }

    public static ScriptClause when(final String command, final String params) {
        return new ScriptClause(command, params);
    }

    private void addToCollection(final ScriptClause aClause) {
        if(clauses==null){
            clauses=new ArrayList<>();
        }
        clauses.add(aClause);
    }


    public boolean isContainer(){
        return clauses!=null;
    }
    public ScriptClause(String command, String parameters) {
        this.command = command.toLowerCase();
        this.parameters = parameters.toLowerCase();
        this.executionMatch=true;
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
        clonedClause.setExecutionMatch(this.executionMatch);
        clonedClause.setClauses(getClonedClauses());
        return clonedClause;
    }

    private void setClauses(final List<ScriptClause> clonedClauses) {
        clauses = clonedClauses;
    }

    private List<ScriptClause> getClonedClauses() {

        if(clauses==null){
            return null;
        }

        final ArrayList<ScriptClause> clonedClauses = new ArrayList<ScriptClause>();
        for(ScriptClause clause : clauses ){
            clonedClauses.add(clause.createCloneCopy());
        }
        return clonedClauses;
    }

    public ScriptClause setExecutionMatch(boolean matchValue){
        this.executionMatch = matchValue;
        return this;
    }

    public boolean executionMatchValue(){
        return this.executionMatch;
    }

    public List<ScriptClause> getClauses() {
        return clauses;
    }
}
