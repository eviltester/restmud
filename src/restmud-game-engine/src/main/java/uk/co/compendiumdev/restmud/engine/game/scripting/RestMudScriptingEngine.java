package uk.co.compendiumdev.restmud.engine.game.scripting;


import uk.co.compendiumdev.restmud.engine.game.MudGame;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ThenClauseCommandList;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.ThenClauseTokenizer;
import uk.co.compendiumdev.restmud.engine.game.scripting.thenClauses.clauses.*;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.WhenClauseCommandList;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.WhenClauseTokenizer;
import uk.co.compendiumdev.restmud.engine.game.scripting.whenClauses.clauses.*;

public class RestMudScriptingEngine {

    private final UserDefinedRulesProcessor rulesProcessor;
    private final ThenClauseTokenizer thenClauseTokenizer;
    private final ThenClauseCommandList thenClauseCommands;
    private final WhenClauseTokenizer whenClauseTokenizer;
    private final WhenClauseCommandList whenClauseCommands;

    public RestMudScriptingEngine(MudGame mudGame) {

        rulesProcessor = new UserDefinedRulesProcessor(mudGame);
        thenClauseCommands = new ThenClauseCommandList(mudGame);

        thenClauseCommands.registerCommand(new DeleteUserCounter(mudGame));
        thenClauseCommands.registerCommand(new DeleteUserFlag(mudGame));
        thenClauseCommands.registerCommand(new DisplayMessage());
        thenClauseCommands.registerCommand(new ForceLook());
        thenClauseCommands.registerCommand(new IncrementScore());
        thenClauseCommands.registerCommand(new IncrementUserCounter());
        thenClauseCommands.registerCommand(new LastActionError());
        thenClauseCommands.registerCommand(new LastActionSuccess());
        thenClauseCommands.registerCommand(new ObjectSetAsSecret(mudGame));
        thenClauseCommands.registerCommand(new OpenGate(mudGame));
        thenClauseCommands.registerCommand(new SetUserCounter());
        thenClauseCommands.registerCommand(new SetUserFlag());
        thenClauseCommands.registerCommand(new ShowCollectableInLook());
        thenClauseCommands.registerCommand(new ShowGate(mudGame));
        thenClauseCommands.registerCommand(new ShowLocationObjectInLook());
        thenClauseCommands.registerCommand(new TeleportCollectableToLocation(mudGame));
        thenClauseCommands.registerCommand(new TeleportUserToLocation());
        thenClauseCommands.registerCommand(new RandomlyGenerateTreasure(mudGame));
        thenClauseCommands.registerCommand(new RandomlyGenerateHoardableJunk(mudGame));
        thenClauseCommands.registerCommand(new RandomlyGenerateHoards(mudGame));
        thenClauseCommands.registerCommand(new RandomlyGenerateNonHoardableJunk(mudGame));
        thenClauseCommands.registerCommand(new RandomlyGenerateNonHoardableTreasure(mudGame));
        thenClauseCommands.registerCommand(new ExitCreate(mudGame));
        thenClauseCommands.registerCommand(new ExitRemove(mudGame));



        thenClauseTokenizer = thenClauseCommands.getTokenizer();


        whenClauseCommands = new WhenClauseCommandList();

        whenClauseCommands.registerCommand(new DirectionMatches(mudGame));
        whenClauseCommands.registerCommand(new LocationIdMatches());
        whenClauseCommands.registerCommand(new LocationObjectIsHere(mudGame));
        whenClauseCommands.registerCommand(new NounPhraseMatches());
        whenClauseCommands.registerCommand(new PlayerCounterExists());
        whenClauseCommands.registerCommand(new PlayerCounterValueMatches());
        whenClauseCommands.registerCommand(new PlayerFlagExists());
        whenClauseCommands.registerCommand(new PlayerFlagIsSet());
        whenClauseCommands.registerCommand(new UserIsCarrying());
        whenClauseCommands.registerCommand(new LocationExitExists(mudGame));
        whenClauseCommands.registerCommand(new AndCondition(mudGame));
        whenClauseCommands.registerCommand(new OrCondition(mudGame));
        whenClauseCommands.registerCommand(new NotCondition(mudGame));

        // HTTP extensions for adventure
        // TODO consider if this should be more generic and make these attributes on a command e..g attribute "HTTP-VERB" value "PUT"
        whenClauseCommands.registerCommand(new HttpVerbIs());
        whenClauseCommands.registerCommand(new HttpHeaderExists());

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
