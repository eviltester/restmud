package uk.co.compendiumdev.restmud.gamedata;

import java.util.Random;

public class RandomStringGenerator {

    String alphabet_lc = "abcdefghijklmnopqrstuvwxyz";

    public String generateAlpha(int length) {

        StringBuilder generated = new StringBuilder();

        Random r = new Random();

        for(int curr = 0; curr<length; curr++){

            int addCharAt = r.nextInt(alphabet_lc.length());
            char addChar = alphabet_lc.charAt(addCharAt);
            generated.append(addChar);

        }

        return generated.toString();
    }
}
