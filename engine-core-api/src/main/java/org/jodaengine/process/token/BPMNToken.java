package org.jodaengine.process.token;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jodaengine.node.activity.ActivityState;

/**
 * The Interface Token. A Token is able to navigate through the process, but does not make up the whole process
 * instance. Moreover it is a single strand of execution.
 */
public interface BPMNToken extends Token<BPMNToken> {

    /**
     * Gets the state of the activity, that belongs to the node that token currently points to. The token holds this
     * state, as want to have stateless Activity-obejcts.
     * 
     * @return the current activity state
     */
    @JsonProperty
    ActivityState getCurrentActivityState();

    

    /**
     * Stopping the token navigation.
     */
    void suspend();

    /**
     * Continuing the token navigation.
     * 
     */
    // TODO Info-Object muss übergeben werden von wem das Token resumed wurde
    void resume();

    // /**
    // * Returns the currently executed activity or null, if there is no such activity.
    // *
    // * @return the current activity
    // */
    // @JsonProperty
    // Activity getCurrentActivity();
}
