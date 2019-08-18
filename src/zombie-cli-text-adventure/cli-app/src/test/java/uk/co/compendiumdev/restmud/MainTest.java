package uk.co.compendiumdev.restmud;

import org.junit.Ignore;
import org.junit.Test;

public class MainTest {

    @Ignore
    @Test
    public void checkArguments(){

        String[] args= {"-v","-h"};

        Main.main(args);

    }

    @Ignore
    @Test
    public void checkArgumentsVerbose(){

        String[] args= {"-v","-h", "-verbose"};

        Main.main(args);

    }


    @Ignore("used for play testing with args")
    @Test
    public void playUsingArguments(){

        String[] args= {"-cols=60", "-verbose", "-txtlog"};

        Main.main(args);

    }

}
