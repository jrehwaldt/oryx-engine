package de.hpi.oryxengine.activity;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * An activity is the behaviour of a Node, e.g. execution
 * behaviour for sending a mail.
 */
public interface Activity {
    
    /**
     * Execute. Starts the execution of the Activity.
     * 
     * @param instance the instance the activity operates on
     */
    void execute(@Nonnull ProcessInstance instance);
    
    /**
     * Returns the activity's state.
     * 
     * @return the activity's state
     */
    @Nonnull ActivityState getState();
    
}
