package de.hpi.oryxengine.navigator;

import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.token.Token;

public interface NavigatorInside extends Navigator {

    /**
     * Adds a token to that is to be worked on.
     * 
     * @param t
     *            the t
     */
    void addWorkToken(Token t);

    /**
     * Adds a token that is in suspended state.
     * 
     * @param token
     *            - the {@link Token} that should be suspended
     */
    void addSuspendToken(Token token);

    /**
     * Removes the suspend token.
     * 
     * @param token
     *            - the {@link Token} that should not be suspended anymore
     */
    void removeSuspendToken(Token token);

    /**
     * Signal that a formerly running process instance has ended.
     * 
     * @param instance
     *            the instance
     */
    void signalEndedProcessInstance(AbstractProcessInstance instance);
}
