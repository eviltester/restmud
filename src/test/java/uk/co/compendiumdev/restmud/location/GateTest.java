package uk.co.compendiumdev.restmud.location;

/**
 * Created by Alan on 13/06/2016.
 */
public class GateTest {

    //TODO:
    // create gate with minimal information e.g. from, direction
    // what if locations don't exist?
    // what if there is no matching to direction? With a oneway it shouldn't matter but a two way should
    // create gate with from, direction, to - work out to direction
    // create gate with from, direction, to, to direction // to allow (1)e:4, (4)s:1 and e and s managed by the gate

    // should try and allow gates to be created even if locations don't exist
    // - write warnings to console rather than throw errors, make creation as easy and self checking as possible
    // check that priority is: verb condition, gate, location

}
