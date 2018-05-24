package uk.co.compendiumdev.cli.textoutput;

public class TextSpacingWrapper implements TextLayoutEngine {
    private int wrapAt;

    public TextSpacingWrapper at(int charPositionToWrap) {
        this.wrapAt = charPositionToWrap;
        return this;
    }

    public String split(String textToSplitAtWrapPosition) {
        String words[] = textToSplitAtWrapPosition.split(" ");

        StringBuilder para = new StringBuilder();
        StringBuilder line = new StringBuilder();

        for(String word : words){
            int lineLength = line.toString().length();
            int wordLength = word.length();

            if((lineLength + wordLength + 1) > wrapAt ){
                //create a new line
                para.append(line.toString() + "\n");
                line = new StringBuilder();
            }
            if(line.toString().length()>0){
                line.append(" ");
            }
            line.append(word);
        }

        // do I have a last line to handle?

        if(line.toString().length()>0){
            para.append(line.toString() + "\n");
        }

        return para.toString();
    }
}
