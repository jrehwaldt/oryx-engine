package org.jodaengine.eventmanagement.adapter;

import org.jodaengine.eventmanagement.EventManager;

/**
 * This interface should be implemented by {@link EventManager} adapters,
 * which allow incoming communication. This includes,
 * e.g. push-imap email receiving adapter.
 */
public interface InboundAdapter extends CorrelationAdapter {

}
