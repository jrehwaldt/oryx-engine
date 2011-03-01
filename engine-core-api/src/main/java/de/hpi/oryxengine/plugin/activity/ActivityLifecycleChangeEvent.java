package de.hpi.oryxengine.plugin.activity;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.ActivityState;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * Immutable container for activity lifecycle events.
 */
public final class ActivityLifecycleChangeEvent {
    
    private final @Nonnull Activity activity;
    private final @Nonnull ActivityState prevState;
    private final @Nonnull ActivityState newState;
    private final @Nonnull ProcessInstance instance;
    
    /**
     * Default constructor. All fields are non-null.
     * 
     * @param activity the fired activity
     * @param prevState the previous state
     * @param newState the new state
     * @param instance the process instance
     */
    public ActivityLifecycleChangeEvent(@Nonnull Activity activity,
                                        @Nonnull ActivityState prevState,
                                        @Nonnull ActivityState newState,
                                        @Nonnull ProcessInstance instance) {
        this.activity = activity;
        this.prevState = prevState;
        this.newState = newState;
        this.instance = instance;
    }
    
    /**
     * Returns the fired activity.
     * 
     * @return the activity, which fired
     */
    public @Nonnull Activity getActivity() {
        return activity;
    }
    
    /**
     * Returns the previous state.
     * 
     * @return the previous state
     */
    public @Nonnull ActivityState getPreviousState() {
        return prevState;
    }
    
    /**
     * Returns the new state.
     * 
     * @return the new state
     */
    public @Nonnull ActivityState getNewState() {
        return newState;
    }
    
    /**
     * Returns the process instance.
     * 
     * @return the process instance
     */
    public @Nonnull ProcessInstance getProcessInstance() {
        return instance;
    }
    
    /**
     *  {@inheritDoc}
     */
    @Override
    public @Nonnull String toString() {
        return String.format("%s changed from %s to %s", this.activity, this.prevState, this.newState);
    }
}
