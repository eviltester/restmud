package uk.co.compendiumdev.restmud.engine.game;


public class SecretGameCode {

    private String secretGameCode;

    public void set(String secretGameRegistrationCode) {
        this.secretGameCode = secretGameRegistrationCode;
    }

    public String get() {
        return secretGameCode;
    }

    public boolean isSecretCodeValid(String aSecretGameCode) {

        // guard code
        if(this.secretGameCode==null)
            return false;

        if(aSecretGameCode == null)
            return false;

        if(this.secretGameCode.contentEquals(aSecretGameCode))
            return true;

        // by default return false
        return false;
    }
}
