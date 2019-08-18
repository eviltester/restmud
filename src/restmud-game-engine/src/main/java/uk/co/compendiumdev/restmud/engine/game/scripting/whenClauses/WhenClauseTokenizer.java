package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses;

import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alan on 08/08/2016.
 */
public class WhenClauseTokenizer{

    private final Map<String, Integer> tokenNameIndex;

    public WhenClauseTokenizer() {
        tokenNameIndex = new HashMap<>();
    }

    public void tokenize(List<ScriptClause> scriptClauses) {

        for(ScriptClause scriptClause : scriptClauses){
            String commandName = scriptClause.getCommand().toLowerCase();
            if(tokenNameIndex.containsKey(commandName)){
                int token = tokenNameIndex.get(commandName);
                scriptClause.setCommandToken(token);
            }else{
                throw new RuntimeException(String.format("Could not find When Clause Command Name %s | %s", commandName, scriptClause.getParameter()));
            }

        }

    }

    public void add(int token, String commandName) {
        this.tokenNameIndex.put(commandName, token);
    }
}
