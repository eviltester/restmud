package uk.co.compendiumdev.restmud.engine.game.scripting;

/**
 * Created by Alan on 07/06/2016.
 */
public class ScriptableCounterCondition {
    public String name;
    private String condition;
    private int value;

    public ScriptableCounterCondition(String thensValue) {
        setFrom(thensValue);
    }

    public void setFrom(String thensValue) {
        String[] parts = thensValue.split(":");

        this.name = parts[0].trim().toLowerCase();

        if(parts.length>1){
            this.condition = parts[1].trim();
        }

        if(this.condition == null || this.condition.trim().length()==0){
            this.condition = "==";
        }

        this.value = 0;

        if(parts.length>2){
            this.value = Integer.parseInt(parts[2].trim());
        }
    }


    public int getValue(){
        return value;
    }
    public String getCondition(){
        return condition;
    }

    public boolean comparedTo(int userCounter) {

        boolean ret=false;

        switch(condition){
            case "==":
            case "=":
                ret = (userCounter==value);
                break;
            case ">":
                ret = (userCounter>value);
                break;
            case "<":
                ret = (userCounter<value);
                break;
            case ">=":
            case "=>":
                ret = (userCounter>=value);
                break;
            case "<=":
            case "=<":
                ret = (userCounter<=value);
                break;
            case "!=":
                ret = (userCounter!=value);
                break;
            default:
                System.out.println("Could not compare symbol " + condition);
        }

        return ret;
    }

    public static ScriptableCounterCondition empty() {
        return new ScriptableCounterCondition(" :");
    }


}
