package de.hpi.oryxengine.navigator.schedule;

import de.hpi.oryxengine.process.instance.ProcessInstance;

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
    void submit(ProcessInstance p);
    
    /**
     * Retrive a processinstance in order to do your work on it.
     *
     * @return the process instance
     */
    ProcessInstance retrieve();

}
