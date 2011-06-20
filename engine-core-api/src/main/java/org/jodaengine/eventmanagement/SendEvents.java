package org.jodaengine.eventmanagement;

import org.jodaengine.eventmanagement.adapter.Message;
import org.jodaengine.eventmanagement.processevent.ProcessEvent;

/**
 * Interface to send events. For now it feels a bit lonely bit it sure will grow.
 */
public interface SendEvents {

    /**
     * Send message from adapter.
     *
     * @param message the message
     * @param event the configuration of the adapter you want to send this message from
     */
    void sendMessageFromAdapter(Message message, ProcessEvent event);
}
