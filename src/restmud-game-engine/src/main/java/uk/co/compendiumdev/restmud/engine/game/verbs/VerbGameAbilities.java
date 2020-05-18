package uk.co.compendiumdev.restmud.engine.game.verbs;

/**
 * TODO: I think abilities should probably be handled by conditions
 * illuminate/darken is really a player ability can_see_in_dark
 * - create standard player flags?
 * would remove some handlers - might simplify code
 * e.g. if room is dark, player flag can see in dark - flag controlled by a condition
 * then local condition
 */
public class VerbGameAbilities {
    public static final String ILLUMINATE_DARKEN = "illuminate/darken";
}
