package de.hpi.oryxengine.correlation.timing;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.exception.DalmatinaException;

/**
 * This class is responsible for providing timing support.
 */
public interface TimingManager {

    /**
     * Registers a new pull adapter.
     * 
     * @param adapter the adapter
     * @throws DalmatinaException thrown if scheduling fails
     */
    void registerPullAdapter(@Nonnull InboundPullAdapter adapter)
    throws DalmatinaException;
}
