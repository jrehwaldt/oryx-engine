package org.jodaengine.eventmanagement.adapter.outgoing;

import org.jodaengine.eventmanagement.adapter.EventAdapter;


/**
 * This interface should be implemented by {@link EventManager} adapters,
 * which allow outgoing communication. This includes,
 * e.g. email sending adapter.
 */
public interface OutboundAdapter extends EventAdapter {

    void sendMessage(String Message);
}
