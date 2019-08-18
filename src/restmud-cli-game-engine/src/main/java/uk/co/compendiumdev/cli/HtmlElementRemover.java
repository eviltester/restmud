package uk.co.compendiumdev.cli;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlElementRemover {

    public String removeOpening(String tag, String descriptionSnippet) {
        return replaceTag(tag, descriptionSnippet, "");
    }

    public String removeClosing(String tag, String descriptionSnippet) {
        Pattern pattern = Pattern.compile("</" + tag + ">");
        Matcher m = pattern.matcher(descriptionSnippet);
        return m.replaceAll("");
    }

    public String removeTag(String tag, String descriptionSnippet) {

        return removeClosing(tag, removeOpening(tag, descriptionSnippet));
    }

    public String replaceTag(String tag, String snippet, String replace){
        Pattern pattern = Pattern.compile("<" + tag + ".+?>");
        Matcher m = pattern.matcher(snippet);
        return m.replaceAll(replace);
    }

    public String tidy(String snippet){

        String ret = snippet;

        ret = removeTag("span", ret);
        ret = removeTag("strong", ret);
        ret = removeTag("bold", ret);
        ret = replaceTag("br", ret,"\n");

        return ret;
    }
}
