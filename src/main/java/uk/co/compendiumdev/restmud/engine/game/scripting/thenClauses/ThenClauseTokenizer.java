package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses;


import uk.co.compendiumdev.restmud.engine.game.scripting.ScriptClause;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThenClauseTokenizer {
    private Map<String, Integer> tokenNameIndex;

    public ThenClauseTokenizer() {
        tokenNameIndex = new HashMap<>();
    }

    public void tokenize(List<ScriptClause> scriptClauses) {

        for(ScriptClause scriptClause : scriptClauses){
            String commandName = scriptClause.getCommand().toLowerCase();
            if(tokenNameIndex.containsKey(commandName)){
                int token = tokenNameIndex.get(commandName);
                scriptClause.setCommandToken(token);
            }else{
                throw new RuntimeException(String.format("Could not find Then Clause Command Name %s | %s ", commandName, scriptClause.getParameter()));
            }
        }
    }

    public void add(int token, String commandName) {
        this.tokenNameIndex.put(commandName, token);
    }
}
