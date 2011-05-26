package org.jodaengine.navigator;

import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.Token;

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
    void addWorkToken(Token t);

    /**
     * Adds a token that is in suspended state.
     * 
     * @param token
     *            - the {@link Token} that should be suspended
     */
    @Override
    void addSuspendToken(Token token);

    /**
     * Removes the suspend token.
     * 
     * @param token
     *            - the {@link Token} that should not be suspended anymore
     */
    @Override
    void removeSuspendToken(Token token);

    /**
     * Signal that a formerly running process instance has ended.
     * 
     * @param instance
     *            the instance
     */
    @Override
    void signalEndedProcessInstance(AbstractProcessInstance instance);
}
