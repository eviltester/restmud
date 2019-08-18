package uk.co.compendiumdev.restmud.engine.game.verbmodes;

/**
 * Created by Alan on 04/01/2016.
 */
public class DefaultVerbModes {
    public VerbModes generate() {
        VerbModes modes = new VerbModes();


        /*
         * Default is access to everything via everything
         */
        VerbMode defaultMode = new VerbMode("default");

        defaultMode.addGUIVerbMapping("look","look");
        defaultMode.addGUIVerbMapping("go\\/\\S*","go");
        defaultMode.addGUIVerbMapping("take\\/\\S*","take");
        defaultMode.addGUIVerbMapping("inventory","inventory");
        defaultMode.addGUIVerbMapping("drop\\/\\S*","drop");
        defaultMode.addGUIVerbMapping("hoard\\/\\S*","hoard");
        defaultMode.addGUIVerbMapping("messages","messages");
        defaultMode.addGUIVerbMapping("open\\/\\S*","open");
        defaultMode.addGUIVerbMapping("close\\/\\S*","close");
        defaultMode.addGUIVerbMapping("scores","scores");
        defaultMode.addGUIVerbMapping("score","score");
        defaultMode.addGUIVerbMapping("examine\\/\\S*","examine");
        defaultMode.addGUIVerbMapping("polish\\/\\S*","polish");
        defaultMode.addGUIVerbMapping("move\\/\\S*","use");
        defaultMode.addGUIVerbMapping("use\\/\\S*","use");
        defaultMode.addGUIVerbMapping("inspect\\/\\S*","inspect");
        defaultMode.addGUIVerbMapping("illuminate","illuminate");
        defaultMode.addGUIVerbMapping("darken","darken");


        defaultMode.addApiGetVerbMapping("look","look");
        defaultMode.addApiGetVerbMapping("go\\/\\S*","go");
        defaultMode.addApiGetVerbMapping("take\\/\\S*","take");
        defaultMode.addApiGetVerbMapping("inventory","inventory");
        defaultMode.addApiGetVerbMapping("drop\\/\\S*","drop");
        defaultMode.addApiGetVerbMapping("hoard\\/\\S*","hoard");
        defaultMode.addApiGetVerbMapping("messages","messages");
        defaultMode.addApiGetVerbMapping("open\\/\\S*", "open");
        defaultMode.addApiGetVerbMapping("close\\/\\S*", "close");
        defaultMode.addApiGetVerbMapping("scores", "scores");
        defaultMode.addApiGetVerbMapping("score","score");
        defaultMode.addApiGetVerbMapping("examine\\/\\S*","examine");
        defaultMode.addApiGetVerbMapping("polish\\/\\S*","polish");
        defaultMode.addApiGetVerbMapping("move\\/\\S*","use");
        defaultMode.addApiGetVerbMapping("use\\/\\S*","use");
        defaultMode.addApiGetVerbMapping("inspect\\/\\S*","inspect");
        defaultMode.addApiGetVerbMapping("illuminate", "illuminate");
        defaultMode.addApiGetVerbMapping("darken", "darken");


        defaultMode.addApiPostVerbMapping("look","look");
        defaultMode.addApiPostVerbMapping("go","go");
        defaultMode.addApiPostVerbMapping("take","take");
        defaultMode.addApiPostVerbMapping("inventory","inventory");
        defaultMode.addApiPostVerbMapping("drop","drop");
        defaultMode.addApiPostVerbMapping("hoard","hoard");
        defaultMode.addApiPostVerbMapping("open","open");
        defaultMode.addApiPostVerbMapping("close", "close");
        defaultMode.addApiPostVerbMapping("scores", "scores");
        defaultMode.addApiPostVerbMapping("score", "score");
        defaultMode.addApiPostVerbMapping("examine","examine");
        defaultMode.addApiPostVerbMapping("polish","polish");
        defaultMode.addApiPostVerbMapping("move","use");
        defaultMode.addApiPostVerbMapping("use","use");
        defaultMode.addApiPostVerbMapping("inspect","inspect");
        defaultMode.addApiPostVerbMapping("illuminate", "illuminate");
        defaultMode.addApiPostVerbMapping("darken","darken");


        modes.add(defaultMode);


        /*
         * Split into indempotent so GET is retrieve and POST is for updates
         */
        VerbMode indempotentSplit = new VerbMode("indempotentSplit");

        indempotentSplit.addGUIVerbMapping("look","look");
        indempotentSplit.addGUIVerbMapping("scores","scores");
        indempotentSplit.addGUIVerbMapping("score","score");
        indempotentSplit.addGUIVerbMapping("go\\/\\S*","go");
        indempotentSplit.addGUIVerbMapping("inventory","inventory");
        indempotentSplit.addGUIVerbMapping("messages","messages");
        indempotentSplit.addGUIVerbMapping("examine\\/\\S*","examine");
        indempotentSplit.addGUIVerbMapping("polish\\/\\S*","polish");
        indempotentSplit.addGUIVerbMapping("move\\/\\S*","use");
        indempotentSplit.addGUIVerbMapping("use\\/\\S*","use");
        indempotentSplit.addGUIVerbMapping("inspect\\/\\S*","inspect");
        indempotentSplit.addGUIVerbMapping("illuminate","illuminate");
        indempotentSplit.addGUIVerbMapping("darken","darken");


        indempotentSplit.addApiGetVerbMapping("look","look");
        indempotentSplit.addApiGetVerbMapping("scores","scores");
        indempotentSplit.addApiGetVerbMapping("score","score");
        indempotentSplit.addApiGetVerbMapping("go\\/\\S*","go");
        indempotentSplit.addApiGetVerbMapping("inventory","inventory");
        indempotentSplit.addApiGetVerbMapping("messages","messages");
        indempotentSplit.addApiGetVerbMapping("examine\\/\\S*","examine");

        indempotentSplit.addApiPostVerbMapping("take","take");
        indempotentSplit.addApiPostVerbMapping("drop","drop");
        indempotentSplit.addApiPostVerbMapping("hoard","hoard");
        indempotentSplit.addApiPostVerbMapping("open", "open");
        indempotentSplit.addApiPostVerbMapping("close", "close");
        indempotentSplit.addApiPostVerbMapping("polish", "polish");
        indempotentSplit.addApiPostVerbMapping("move", "use");
        indempotentSplit.addApiPostVerbMapping("use", "use");
        indempotentSplit.addApiPostVerbMapping("inspect", "inspect");
        indempotentSplit.addApiPostVerbMapping("illuminate", "illuminate");
        indempotentSplit.addApiPostVerbMapping("darken", "darken");

        modes.add(indempotentSplit);


        /*
         * Indempotent split with no GUI at all
         */
        VerbMode noGuiGetsAndApiSplit = new VerbMode("noGuiGetsAndApiSplit");

        noGuiGetsAndApiSplit.addApiGetVerbMapping("look","look");
        noGuiGetsAndApiSplit.addApiGetVerbMapping("scores","scores");
        noGuiGetsAndApiSplit.addApiGetVerbMapping("score","score");
        noGuiGetsAndApiSplit.addApiGetVerbMapping("go\\/\\S*","go");
        noGuiGetsAndApiSplit.addApiGetVerbMapping("inventory","inventory");
        noGuiGetsAndApiSplit.addApiGetVerbMapping("messages","messages");
        noGuiGetsAndApiSplit.addApiGetVerbMapping("examine\\/\\S*","examine");

        noGuiGetsAndApiSplit.addApiPostVerbMapping("take","take");
        noGuiGetsAndApiSplit.addApiPostVerbMapping("drop","drop");
        noGuiGetsAndApiSplit.addApiPostVerbMapping("hoard", "hoard");
        noGuiGetsAndApiSplit.addApiPostVerbMapping("open","open");
        noGuiGetsAndApiSplit.addApiPostVerbMapping("close", "close");
        noGuiGetsAndApiSplit.addApiPostVerbMapping("polish", "polish");
        noGuiGetsAndApiSplit.addApiPostVerbMapping("move", "use");
        noGuiGetsAndApiSplit.addApiPostVerbMapping("go", "go");
        noGuiGetsAndApiSplit.addApiPostVerbMapping("use", "use");
        noGuiGetsAndApiSplit.addApiPostVerbMapping("inspect", "inspect");
        noGuiGetsAndApiSplit.addApiPostVerbMapping("illuminate", "illuminate");
        noGuiGetsAndApiSplit.addApiPostVerbMapping("darken","darken");

        modes.add(noGuiGetsAndApiSplit);



        return modes;

    }
}
