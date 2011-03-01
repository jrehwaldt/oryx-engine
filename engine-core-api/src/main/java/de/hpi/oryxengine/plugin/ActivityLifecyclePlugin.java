package de.hpi.oryxengine.plugin;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.activity.AbstractActivityImpl;
import de.hpi.oryxengine.activity.ExecutionState;

/**
 * This interface should be implemented by plugins, which
 * aim to react on lifecycle changes of an activity instance.
 */
public interface ActivityLifecyclePlugin
extends Plugin {
    
    /**
     * Hook for lifecycle change.
     * 
     * @param activity the activity, which changed
     * @param prevState the previous lifecycle state
     * @param newState the new lifecycle state
     */
    void stateChanged(@Nonnull AbstractActivityImpl activity,
                      @Nonnull ExecutionState prevState,
                      @Nonnull ExecutionState newState);
}
