package org.jodaengine.eventmanagement.processevent.outgoing;

import org.jodaengine.eventmanagement.adapter.Message;
import org.jodaengine.eventmanagement.processevent.ProcessEvent;

/**
 * The Interface OutgoingProcessEvent.
 * Outgoign process events are specified through the fact that they carry some kind of {@link Message}
 */
public interface OutgoingProcessEvent extends ProcessEvent {
    
    /**
     * Gets the message, which is part of the OutgoingProcessEvent.
     *
     * @return the message
     */
    Message getMessage();

}
