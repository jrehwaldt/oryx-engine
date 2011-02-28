package de.hpi.oryxengine.activity;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Interface Activity.
 * An activity is the behaviour of a Node. So what the node does exactly.
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
    @Nonnull ExecutionState getState();
}
