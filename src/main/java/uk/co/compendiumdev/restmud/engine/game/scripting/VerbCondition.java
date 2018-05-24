package uk.co.compendiumdev.restmud.engine.game.scripting;

import java.util.ArrayList;
import java.util.List;

public class VerbCondition implements ScriptCondition{
    private final String verbName;
    private final List<ScriptClause> when;
    private final List<ScriptClause> commands;
    private String reason; // a comment to document this condition


    public VerbCondition(String verbName) {
        this.verbName = verbName;
        this.when = new ArrayList<>();
        this.commands = new ArrayList<>();
    }

    // only used when cloning
    private VerbCondition(String verbName, List<ScriptClause> whenClones, List<ScriptClause> commandClones) {
        this.verbName = verbName;
        this.when = whenClones;
        this.commands = commandClones;
    }



    public VerbCondition andWhen(String matcher, String value, boolean matchValue) {
        return when(matcher, value, matchValue);
    }

    public ScriptCondition andWhenFalse(String matcher, String value) {
        return when(matcher, value, false);
    }

    public VerbCondition when(String matcher, String value) {
        return when(matcher, value, true);
    }

    public VerbCondition andWhen(String matcher, String value) {
        return when(matcher, value);
    }

    public VerbCondition when(String matcher, String value, boolean matchValue) {
        this.when.add(new ScriptClause(matcher, value.toLowerCase()).setExecutionMatch(matchValue));
        return this;
    }

    public VerbCondition whenFalse(String matcher, String value) {
        return when(matcher, value, false);
    }

    public VerbCondition then(String command, String value) {
        this.commands.add(new ScriptClause(command, value.toLowerCase()));
        return this;
    }

    public String getVerbName() {
        return verbName;
    }

    public List<ScriptClause>whenClauses() {
        return when;
    }

    public List<ScriptClause> thenClauses() {
        return commands;
    }

    public VerbCondition andThen(String command, String value) {
        return then(command, value);
    }

    public VerbCondition getClonedCopy() {
        List<ScriptClause> whenClones = new ArrayList<>();
        List<ScriptClause> commandClones = new ArrayList<>();

        for(ScriptClause aWhen : this.when){
            whenClones.add(aWhen.createCloneCopy());
        }

        for(ScriptClause aCommand : this.commands){
            commandClones.add(aCommand.createCloneCopy());
        }

        return new VerbCondition(this.verbName, whenClones, commandClones);
    }

    public void because(String reason) {
        this.reason = reason;
    }


}
