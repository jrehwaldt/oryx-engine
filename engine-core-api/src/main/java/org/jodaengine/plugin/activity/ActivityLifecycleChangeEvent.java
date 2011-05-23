package org.jodaengine.plugin.activity;

import javax.annotation.Nonnull;

import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BPMNToken;

/**
 * Immutable container for activity lifecycle events.
 */
public final class ActivityLifecycleChangeEvent {

    private final @Nonnull Node currentNode;
    private final @Nonnull BPMNToken bPMNToken;

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
     * @param bPMNToken
     *            the process token
     */
    public ActivityLifecycleChangeEvent(@Nonnull Node currentNode,
                                        @Nonnull ActivityState prevState,
                                        @Nonnull ActivityState newState,
                                        @Nonnull BPMNToken bPMNToken) {
        
        this.currentNode = currentNode;
        this.prevState = prevState;
        this.newState = newState;
        this.bPMNToken = bPMNToken;
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
    public @Nonnull ActivityState getPreviousState() {
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
    public @Nonnull BPMNToken getProcessToken() {
        return this.bPMNToken;
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
