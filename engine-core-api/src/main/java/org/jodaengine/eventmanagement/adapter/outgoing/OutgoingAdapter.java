package org.jodaengine.eventmanagement.adapter.outgoing;

import org.jodaengine.eventmanagement.adapter.EventAdapter;


/**
 * This interface should be implemented by {@link EventManager} adapters,
 * which allow outgoing communication. This includes,
 * e.g. email sending adapter.
 */
public interface OutgoingAdapter extends EventAdapter {

    /**
     * Send the message to the specified receipent.
     *
     * @param receipent the receipent (might be email adress or ICQ Number etc...:)
     * @param message the message
     */
    void sendMessage(String receipent, String message);
    
    
    /**
     * Sends the message with the specified subject to the specified receipent.
     *
     * @param receipent the receipent (might be email adress or ICQ Number etc...:)
     * @param subject the subject
     * @param message the message
     */
    void sendMessage(String receipent, String subject, String message);
}
