package uk.co.compendiumdev.restmud.engine.game.scripting;

import java.util.List;

/**
 * Created by Alan on 06/06/2016.
 */
public interface ScriptCondition {
    ScriptCondition andWhen(String matcher, String value);

    ScriptCondition when(String matcher, String value);

    ScriptCondition then(String command, String value);

    List<ScriptClause> whenClauses();

    List<ScriptClause> thenClauses();

    ScriptCondition andThen(String command, String value);
}
