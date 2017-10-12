package uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 08/08/2016.
 */
public class WhenClauseCommandList {

    private final MudGame game;
    private List<ScriptWhenClause> commandList;
    private WhenClauseTokenizer tokenizer;

    public WhenClauseCommandList(MudGame mudGame) {
        this.game = mudGame;
        commandList = new ArrayList<>();
        tokenizer = new WhenClauseTokenizer(mudGame);


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
