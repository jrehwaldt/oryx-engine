package org.jodaengine.eventmanagement.adapter.incoming;

import org.jodaengine.eventmanagement.adapter.EventAdapter;


/**
 * This interface should be implemented by {@link EventManager} adapters,
 * which allow incoming communication. This includes,
 * e.g. push-imap email receiving adapter.
 */
public interface IncomingAdapter extends EventAdapter {

}
