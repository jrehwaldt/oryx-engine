package org.jodaengine.correlation.adapter;

import org.jodaengine.correlation.EventManager;

/**
 * This interface should be implemented by {@link EventManager} adapters,
 * which allow incoming communication. This includes,
 * e.g. push-imap email receiving adapter.
 */
public interface InboundAdapter extends CorrelationAdapter {

}
