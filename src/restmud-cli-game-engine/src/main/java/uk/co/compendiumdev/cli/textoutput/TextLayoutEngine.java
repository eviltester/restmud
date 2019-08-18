package uk.co.compendiumdev.cli.textoutput;

public interface TextLayoutEngine {
    TextLayoutEngine at(int charPositionToWrap);
    String split(String textToSplitAtWrapPosition);
}
