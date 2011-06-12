package org.jodaengine.ext.listener.token;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * Immutable container for activity lifecycle events.
 */
public final class ActivityLifecycleChangeEvent {

    private final @Nonnull Node currentNode;
    private final @Nonnull Token token;

    private final @Nonnull ActivityState prevState;
    private final @Nonnull ActivityState newState;

    /**
     * Default constructor. All fields are non-null.
     * 
     * @param currentNode
     *            the fired node
     * @param prevState
     *            the previous state
     * @param newState
     *            the new state
     * @param token
     *            the process token
     */
    public ActivityLifecycleChangeEvent(@Nonnull Node currentNode,
                                        @Nullable ActivityState prevState,
                                        @Nonnull ActivityState newState,
                                        @Nonnull Token token) {
        
        this.currentNode = currentNode;
        this.prevState = prevState;
        this.newState = newState;
        this.token = token;
    }

    /**
     * Returns the fired node.
     * 
     * @return the node, which fired
     */
    public @Nonnull Node getNode() {
        return this.currentNode;
    }

    /**
     * Returns the previous state.
     * 
     * @return the previous state
     */
    public @Nullable ActivityState getPreviousState() {
        return this.prevState;
    }

    /**
     * Returns the new state.
     * 
     * @return the new state
     */
    public @Nonnull ActivityState getNewState() {
        return this.newState;
    }

    /**
     * Returns the process instance.
     * 
     * @return the process instance
     */
    public @Nonnull Token getProcessToken() {
        return this.token;
    }

    /**
     * To string.
     * 
     * @return the string {@inheritDoc}
     */
    @Override
    public @Nonnull String toString() {
        return String.format("%s changed from %s to %s", this.currentNode, this.prevState, this.newState);
    }
}
