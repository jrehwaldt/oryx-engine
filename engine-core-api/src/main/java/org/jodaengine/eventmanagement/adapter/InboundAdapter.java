package org.jodaengine.eventmanagement.adapter;


/**
 * This interface should be implemented by {@link EventManager} adapters,
 * which allow incoming communication. This includes,
 * e.g. push-imap email receiving adapter.
 */
public interface InboundAdapter extends CorrelationAdapter {

}
