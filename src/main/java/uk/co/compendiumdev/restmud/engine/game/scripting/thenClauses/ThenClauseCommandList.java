package uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses;

import uk.co.compendiumdev.restmud.engine.game.MudGame;

import java.util.ArrayList;
import java.util.List;

public class ThenClauseCommandList {
    private final MudGame game;
    private List<ScriptThenCommand> commandList;
    private ThenClauseTokenizer tokenizer;

    public ThenClauseCommandList(MudGame mudGame) {
        this.game = mudGame;
        commandList = new ArrayList<>();
        tokenizer = new ThenClauseTokenizer(mudGame);


    }

    public void registerCommand(ScriptThenCommand command) {
        commandList.add(command);
        tokenizer.add(commandList.lastIndexOf(command),command.getCommandName().toLowerCase());

    }

    public ThenClauseTokenizer getTokenizer() {
        return tokenizer;
    }

    public ScriptThenCommand getCommand(int token) {
        return commandList.get(token);
    }
}
