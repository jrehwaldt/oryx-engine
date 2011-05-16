package org.jodaengine.correlation.adapter;

import org.jodaengine.correlation.EventManager;

/**
 * This interface should be implemented by {@link EventManager} adapters,
 * which allow outgoing communication. This includes,
 * e.g. email sending adapter.
 */
public interface OutboundAdapter extends CorrelationAdapter {

}
