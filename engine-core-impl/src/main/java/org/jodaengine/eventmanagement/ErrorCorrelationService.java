package org.jodaengine.eventmanagement;

import org.jodaengine.exception.JodaEngineRuntimeException;

/**
 * This represents a {@link EventCorrelator} that was not implemented. This can be used as NullObject.
 */
public class ErrorCorrelationService implements EventCorrelator {

    private static final String EXCEPTION_DESCRIPTION = "This CorrelationService cannot correlate any events."
        + "It should not be used by any Adapter.";

    @Override
    public void correlate(AdapterEvent e) {

        throw new JodaEngineRuntimeException(EXCEPTION_DESCRIPTION);
    }

}
