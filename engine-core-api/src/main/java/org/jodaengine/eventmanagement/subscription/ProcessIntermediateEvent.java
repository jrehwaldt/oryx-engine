package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.process.token.BPMNToken;

/**
 * The Interface IntermediateEvent. An intermediate event, in contrast to the startEvent will not trigger the
 * instantiation of a new process, but a notification to a registered process instance will be sent.
 */
public interface ProcessIntermediateEvent extends ProcessEvent {

    /**
     * Gets the assigned token that has be notified if timer is complete.
     * 
     * @return the definition id
     */
    BPMNToken getToken();

}
