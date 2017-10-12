package uk.co.compendiumdev.restmud.engine.game.scripting;


import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.parser.VerbTokenizer;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.*;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses.*;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.*;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses.*;

public class RestMudScriptingEngine {

    private final UserDefinedRulesProcessor rulesProcessor;
    private final ThenClauseTokenizer thenClauseTokenizer;
    private final VerbTokenizer verbTokenizer;
    private final ThenClauseCommandList thenClauseCommands;
    private final WhenClauseTokenizer whenClauseTokenizer;
    private final WhenClauseCommandList whenClauseCommands;

    public RestMudScriptingEngine(MudGame mudGame) {

        rulesProcessor = new UserDefinedRulesProcessor(mudGame);
        verbTokenizer = new VerbTokenizer();
        thenClauseCommands = new ThenClauseCommandList(mudGame);

        thenClauseCommands.registerCommand(new DeleteUserCounter(mudGame));
        thenClauseCommands.registerCommand(new DeleteUserFlag(mudGame));
        thenClauseCommands.registerCommand(new DisplayMessage(mudGame));
        thenClauseCommands.registerCommand(new ForceLook(mudGame));
        thenClauseCommands.registerCommand(new IncrementScore(mudGame));
        thenClauseCommands.registerCommand(new IncrementUserCounter(mudGame));
        thenClauseCommands.registerCommand(new LastActionError(mudGame));
        thenClauseCommands.registerCommand(new LastActionSuccess(mudGame));
        thenClauseCommands.registerCommand(new ObjectSetAsSecret(mudGame));
        thenClauseCommands.registerCommand(new OpenGate(mudGame));
        thenClauseCommands.registerCommand(new SetUserCounter(mudGame));
        thenClauseCommands.registerCommand(new SetUserFlag(mudGame));
        thenClauseCommands.registerCommand(new ShowCollectableInLook(mudGame));
        thenClauseCommands.registerCommand(new ShowGate(mudGame));
        thenClauseCommands.registerCommand(new ShowLocationObjectInLook(mudGame));
        thenClauseCommands.registerCommand(new TeleportCollectableToLocation(mudGame));
        thenClauseCommands.registerCommand(new TeleportUserToLocation(mudGame));
        thenClauseCommands.registerCommand(new RandomlyGenerateTreasure(mudGame));
        thenClauseCommands.registerCommand(new RandomlyGenerateHoardableJunk(mudGame));
        thenClauseCommands.registerCommand(new RandomlyGenerateHoards(mudGame));
        thenClauseCommands.registerCommand(new RandomlyGenerateNonHoardableJunk(mudGame));
        thenClauseCommands.registerCommand(new RandomlyGenerateNonHoardableTreasure(mudGame));



        thenClauseTokenizer = thenClauseCommands.getTokenizer();


        whenClauseCommands = new WhenClauseCommandList(mudGame);

        whenClauseCommands.registerCommand(new DirectionMatches(mudGame));
        whenClauseCommands.registerCommand(new LocationIdMatches(mudGame));
        whenClauseCommands.registerCommand(new LocationObjectIsHere(mudGame));
        whenClauseCommands.registerCommand(new NounPhraseMatches(mudGame));
        whenClauseCommands.registerCommand(new PlayerCounterExists(mudGame));
        whenClauseCommands.registerCommand(new PlayerCounterValueMatches(mudGame));
        whenClauseCommands.registerCommand(new PlayerFlagExists(mudGame));
        whenClauseCommands.registerCommand(new PlayerFlagIsSet(mudGame));
        whenClauseCommands.registerCommand(new UserIsCarrying(mudGame));

        // HTTP extensions for adventure
        whenClauseCommands.registerCommand(new HttpVerbIs(mudGame));
        whenClauseCommands.registerCommand(new HttpHeaderExists(mudGame));

        whenClauseTokenizer = whenClauseCommands.getTokenizer();
    }

    public UserDefinedRulesProcessor rulesProcessor(){
        return rulesProcessor;
    }

    public ThenClauseTokenizer thenClauseTokenizer() {
        return thenClauseTokenizer;
    }

    public WhenClauseTokenizer whenClauseTokenizer() {
        return whenClauseTokenizer;
    }

    public ThenClauseCommandList thenClauseCommandList(){
        return thenClauseCommands;
    }

    public WhenClauseCommandList whenClauseCommandList() {
        return whenClauseCommands;
    }
}
