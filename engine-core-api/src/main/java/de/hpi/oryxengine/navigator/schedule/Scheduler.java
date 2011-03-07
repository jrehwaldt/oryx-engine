package de.hpi.oryxengine.navigator.schedule;

import java.util.List;

import de.hpi.oryxengine.process.token.Token;

/**
 * The Interface Scheduler.
 * It is used in order to schedule the process instances of our navigator.
 */
public interface Scheduler {
    
    
    /**
     * Submit a new process instance to be scheduled.
     *
     * @param p the p
     */
    void submit(Token p);
    
    /**
     * Retrive a processinstance in order to do your work on it.
     *
     * @return the process instance
     */
    Token retrieve();
    
    /**
     * Checks if we got nothing to schedule.
     *
     * @return true, if it is empty
     */
    boolean isEmpty();
    
    /**
     * Submit all process instances to be scheduled.
     *
     * @param listOfInstances the list of instances
     */
    void submitAll(List<Token> listOfInstances);

}
