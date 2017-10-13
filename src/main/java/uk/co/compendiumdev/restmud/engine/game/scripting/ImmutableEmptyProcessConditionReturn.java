package uk.co.compendiumdev.restmud.engine.game.scripting;


public class ImmutableEmptyProcessConditionReturn extends ProcessConditionReturn{


    // TODO this should really implement an interface and have method implementations
    // that enforce immutability
    // At the moment it is just an extend to always return the same EmptyProcessCondition
    // to cut down on objects being created and then thrown away almost immediately

    private static final ProcessConditionReturn returnProcessConditionReturn = new ProcessConditionReturn();

    public static ProcessConditionReturn get(){
        return returnProcessConditionReturn;
    }
}
