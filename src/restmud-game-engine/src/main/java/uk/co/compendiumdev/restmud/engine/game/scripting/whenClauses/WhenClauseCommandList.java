package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 08/08/2016.
 */
public class WhenClauseCommandList {

    private final List<ScriptWhenClause> commandList;
    private final WhenClauseTokenizer tokenizer;

    public WhenClauseCommandList() {
        commandList = new ArrayList<>();
        tokenizer = new WhenClauseTokenizer();
    }

    public void registerCommand(ScriptWhenClause command) {
        commandList.add(command);
        tokenizer.add(commandList.lastIndexOf(command),command.getCommandName().toLowerCase());

    }

    public WhenClauseTokenizer getTokenizer() {
        return tokenizer;
    }

    public ScriptWhenClause getCommand(int token) {
        return commandList.get(token);
    }

}
