package uk.co.compendiumdev.restmud.engine.game;


public enum PlayerGuiMode {
    SUPER_EASY("super_easy_gui", 3), EASY("easy_gui", 2), NORMAL("normal_gui", 1);

    private final String template_gui_mode_string;
    private final int guiWeighting;

    PlayerGuiMode(String template_gui_mode_string, int weighting) {
        this.template_gui_mode_string = template_gui_mode_string;
        this.guiWeighting = weighting;
    }

    public String templateGuiModeString(){
        return this.template_gui_mode_string;
    }

    public int getGuiWeighting() {
        return guiWeighting;
    }

    public static PlayerGuiMode getMode(String gameGuiMode) {

        String compareGameGuiMode = gameGuiMode.toLowerCase();

        if(compareGameGuiMode.contains("super")) {
            return PlayerGuiMode.SUPER_EASY;
        }

        if(compareGameGuiMode.contains("normal")){
            return PlayerGuiMode.NORMAL;
        }

        return PlayerGuiMode.EASY;
    }

}
