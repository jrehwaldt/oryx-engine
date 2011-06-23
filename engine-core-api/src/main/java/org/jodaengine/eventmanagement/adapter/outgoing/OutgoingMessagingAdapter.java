package org.jodaengine.eventmanagement.adapter.outgoing;

import org.jodaengine.eventmanagement.adapter.EventAdapter;
import org.jodaengine.eventmanagement.adapter.Message;


/**
 * This interface should be implemented by {@link EventService} adapters,
 * which allow outgoing communication. This includes,
 * e.g. email sending adapter.
 */
public interface OutgoingMessagingAdapter extends EventAdapter {

   /**
    * Send the message.
    *
    * @param message the message
    */
   void sendMessage(Message message);
}
