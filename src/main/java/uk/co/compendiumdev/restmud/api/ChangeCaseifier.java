package uk.co.compendiumdev.restmud.api;

/**
 * Created by Alan on 08/02/2017.
 */
public class ChangeCaseifier {
    public String upperCaseOf(String aString, int position) {

        StringBuffer buffer = new StringBuffer(aString);
        char changedChar = Character.toUpperCase(buffer.charAt(position));
        buffer.replace(position, position+1, String.valueOf(changedChar));

        return buffer.toString();
    }

    public String lowerCaseOf(String aString, int position) {
        StringBuffer buffer = new StringBuffer(aString);
        char changedChar = Character.toLowerCase(buffer.charAt(position));
        buffer.replace(position, position+1, String.valueOf(changedChar));

        return buffer.toString();
    }

    public String reverseCaseOf(String aString, int position) {
        if(Character.isUpperCase(aString.charAt(position))){
            return lowerCaseOf(aString, position);
        }else{
            return upperCaseOf(aString,position);
        }
    }

    public int selectRandomLetterPosition(String aString) {
        return (int)(Math.random() * aString.length());
    }

    public String randomlyChangeCaseOf(String aString) {
        String theString = aString;

        int numberOfCharsToChange = (int)(Math.random() * (theString.length()+1));

        for(int charLoop=0; charLoop<numberOfCharsToChange; charLoop++){
            int charToChange = selectRandomLetterPosition(theString);
            theString = reverseCaseOf(theString, charToChange);
        }
        return theString;
    }
}
