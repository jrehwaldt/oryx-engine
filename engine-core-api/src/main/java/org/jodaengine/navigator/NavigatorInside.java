package org.jodaengine.navigator;

import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.BPMNToken;

/**
 * Extends the NavigatorInterface so that it provides more methods for the internal classes.
 * 
 * This Interface is expected to be used by internal classes.
 */
public interface NavigatorInside extends Navigator {

    /**
     * Adds a token to that is to be worked on.
     * 
     * @param t
     *            the t
     */
    @Override
    void addWorkToken(BPMNToken t);

    /**
     * Adds a token that is in suspended state.
     * 
     * @param bPMNToken
     *            - the {@link BPMNToken} that should be suspended
     */
    @Override
    void addSuspendToken(BPMNToken bPMNToken);

    /**
     * Removes the suspend token.
     * 
     * @param bPMNToken
     *            - the {@link BPMNToken} that should not be suspended anymore
     */
    @Override
    void removeSuspendToken(BPMNToken bPMNToken);

    /**
     * Signal that a formerly running process instance has ended.
     * 
     * @param instance
     *            the instance
     */
    @Override
    void signalEndedProcessInstance(AbstractProcessInstance instance);
}
