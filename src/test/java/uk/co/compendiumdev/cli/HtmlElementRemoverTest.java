package uk.co.compendiumdev.cli;

import org.junit.Assert;
import org.junit.Test;

public class HtmlElementRemoverTest {

    // HTML remover just has to handle the game data html, which is always well formed and simple

    @Test
    public void canRemoveStartSpan(){
        String descriptionSnippet = "locked with a <span location_object_id='very_strong_padlock'>very strong padlock</span>.";

        HtmlElementRemover remover = new HtmlElementRemover();

        Assert.assertEquals("locked with a very strong padlock</span>.",
                            remover.removeOpening("span", descriptionSnippet));
    }

    @Test
    public void canRemoveEndSpan(){
        String descriptionSnippet = "locked with a <span location_object_id='very_strong_padlock'>very strong padlock</span>.";

        HtmlElementRemover remover = new HtmlElementRemover();

        Assert.assertEquals("locked with a <span location_object_id='very_strong_padlock'>very strong padlock.",
                remover.removeClosing("span", descriptionSnippet));
    }


    @Test
    public void canRemoveAll(){
        String descriptionSnippet = "<span id='locked'>locked</span> with a <span location_object_id='very_strong_padlock'>very strong padlock</span>.";

        HtmlElementRemover remover = new HtmlElementRemover();

        Assert.assertEquals("locked with a very strong padlock.",
                remover.removeTag("span", descriptionSnippet));
    }


    @Test
    public void canReplace(){
        String descriptionSnippet = "you fumble with the key.<br/> you drop the key.<br/>you hear a loud";

        HtmlElementRemover remover = new HtmlElementRemover();

        Assert.assertEquals("you fumble with the key.\n you drop the key.\nyou hear a loud",
                remover.replaceTag("br", descriptionSnippet, "\n"));

    }

    @Test
    public void canTidy(){
        String descriptionSnippet = "you fumble with the <span id='key'>key</span>.<br/> you drop the key.<br/>you hear a loud";

        HtmlElementRemover remover = new HtmlElementRemover();

        Assert.assertEquals("you fumble with the key.\n you drop the key.\nyou hear a loud",
                remover.tidy(descriptionSnippet));

    }
}
