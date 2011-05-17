package org.jodaengine.eventmanagement.adapter;

import org.jodaengine.eventmanagement.EventManager;

/**
 * This interface should be implemented by {@link EventManager} adapters,
 * which allow outgoing communication. This includes,
 * e.g. email sending adapter.
 */
public interface OutboundAdapter extends CorrelationAdapter {

}
