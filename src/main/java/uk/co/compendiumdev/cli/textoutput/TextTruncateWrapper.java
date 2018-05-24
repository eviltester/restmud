package uk.co.compendiumdev.cli.textoutput;

public class TextTruncateWrapper implements TextLayoutEngine {
    private int wrapAt;

    public TextTruncateWrapper at(int charPositionToWrap) {
        this.wrapAt = charPositionToWrap;
        return this;
    }

    public String split(String textToSplitAtWrapPosition) {
        return textToSplitAtWrapPosition.replaceAll(String.format(".{%d}(?=.)", wrapAt), "$0\n");
    }
}
