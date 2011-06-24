package org.jodaengine.eventmanagement.adapter;


/**
 * A message with a subject.
 */
public interface AddressableMessageWithSubject extends AddressableMessage {

    /**
     * Get the subject of the message.
     *
     * @return the subject
     */
    String getSubject();
}
