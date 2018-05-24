package uk.co.compendiumdev.restmud.engine.game.scripting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 06/06/2016.
 */
public class PriorityTurnCondition implements ScriptCondition {

    private final List<ScriptClause> when;
    private final List<ScriptClause> commands;
    private String reason;

    public PriorityTurnCondition() {
        this.when = new ArrayList<>();
        this.commands = new ArrayList<>();
    }

    private PriorityTurnCondition(List<ScriptClause> whenClones, List<ScriptClause> commandClones) {
        this.when = whenClones;
        this.commands = commandClones;
    }

    public PriorityTurnCondition andWhen(String matcher, String value) {
        return when(matcher, value);
    }

    public PriorityTurnCondition when(String matcher, String value) {
        this.when.add(new ScriptClause(matcher, value));
        return this;
    }

    public PriorityTurnCondition then(String command, String value) {
        this.commands.add(new ScriptClause(command, value));
        return this;
    }

    public List<ScriptClause> whenClauses() {
        return when;
    }

    public List<ScriptClause> thenClauses() {
        return commands;
    }

    public PriorityTurnCondition andThen(String command, String value) {
        return then(command, value);
    }

    public PriorityTurnCondition getClonedCopy() {
        List<ScriptClause> whenClones = new ArrayList<>();
        List<ScriptClause> commandClones = new ArrayList<>();

        for(ScriptClause aWhen : this.when){
            whenClones.add(aWhen.createCloneCopy());
        }

        for(ScriptClause aCommand : this.commands){
            commandClones.add(aCommand.createCloneCopy());
        }

        return new PriorityTurnCondition(whenClones, commandClones);
    }

    public PriorityTurnCondition because(String reason) {
        this.reason = reason;
        return this;
    }
}
