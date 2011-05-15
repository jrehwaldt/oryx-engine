package org.jodaengine.plugin.activity;

import javax.annotation.Nonnull;

import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.token.Token;

/**
 * Immutable container for activity lifecycle events.
 */
public final class ActivityLifecycleChangeEvent {

    /** The activity. */
    private final @Nonnull
    Activity activity;

    /** The prev state. */
    private final @Nonnull
    ActivityState prevState;

    /** The new state. */
    private final @Nonnull
    ActivityState newState;

    /** The instance. */
    private final @Nonnull
    Token token;

    // TODO add the node that this activity was executed on to the event?
    /**
     * Default constructor. All fields are non-null.
     * 
     * @param activity
     *            the fired activity
     * @param prevState
     *            the previous state
     * @param newState
     *            the new state
     * @param token
     *            the process token
     */
    public ActivityLifecycleChangeEvent(@Nonnull Activity activity,
                                        @Nonnull ActivityState prevState,
                                        @Nonnull ActivityState newState,
                                        @Nonnull Token token) {

        this.activity = activity;
        this.prevState = prevState;
        this.newState = newState;
        this.token = token;
    }

    /**
     * Returns the fired activity.
     * 
     * @return the activity, which fired
     */
    public @Nonnull
    Activity getActivity() {

        return activity;
    }

    /**
     * Returns the previous state.
     * 
     * @return the previous state
     */
    public @Nonnull
    ActivityState getPreviousState() {

        return prevState;
    }

    /**
     * Returns the new state.
     * 
     * @return the new state
     */
    public @Nonnull
    ActivityState getNewState() {

        return newState;
    }

    /**
     * Returns the process instance.
     * 
     * @return the process instance
     */
    public @Nonnull
    Token getProcessToken() {

        return token;
    }

    /**
     * To string.
     * 
     * @return the string {@inheritDoc}
     */
    @Override
    public @Nonnull
    String toString() {

        return String.format("%s changed from %s to %s", this.activity, this.prevState, this.newState);
    }
}
