package org.jodaengine.navigator.schedule;

import java.util.List;

import org.jodaengine.process.token.Token;

/**
 * The Interface Scheduler.
 * It is used in order to schedule the process tokens of our navigator.
 */
public interface Scheduler {

    /**
     * Submit a new process token to be scheduled.
     * 
     * @param p
     *            the p
     */
    void submit(Token p);

    /**
     * Retrive a processtoken in order to do your work on it.
     * 
     * @return the process token
     */
    Token retrieve();

    /**
     * Checks if we got nothing to schedule.
     * 
     * @return true, if it is empty
     */
    boolean isEmpty();

    /**
     * Submit all process tokens to be scheduled.
     * 
     * @param listOfTokens
     *            the list of tokens
     */
    void submitAll(List<Token> listOfTokens);

}
