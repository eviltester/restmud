package uk.co.compendiumdev.cli;

public class CommandLineArg {
    private final String argument;
    private final String value;

    private CommandLineArg(String key, String value) {
        this.argument = key;
        this.value = value;
    }

    public static CommandLineArg createFrom(String arg) {

        String []splitArgs = arg.split("=");

        String key = "";
        String value = "";

        if(splitArgs.length>0 && splitArgs[0]!=null){key=splitArgs[0];}
        if(splitArgs.length>1 && splitArgs[1]!=null){value = splitArgs[1];}

        if(key.contentEquals(""))
            return null;

        return new CommandLineArg(key, value);
    }

    public String argument() {
        return this.argument;
    }

    public String value() {
        return this.value;
    }
}
