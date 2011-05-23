package org.jodaengine.navigator.schedule;

import org.jodaengine.process.token.BPMNToken;

/**
 * An Event for the scheduler listener, may be extended.
 * For now it contains everything, that it needs to contain.
 * Please refer to the respective getters for more information.
 */
public final class SchedulerEvent {

    /** The number of tokens. */
    private final int numberOfTokens;

    /** The token. */
    private final BPMNToken bPMNToken;

    /** The scheduler action. */
    private final SchedulerAction schedulerAction;

    /**
     * Instantiates a new scheduler event.
     * 
     * @param schedulerAction
     *            the scheduler action
     * @param bPMNToken
     *            the process instance
     * @param numberOfTokens
     *            the number of instances
     */
    public SchedulerEvent(SchedulerAction schedulerAction, BPMNToken bPMNToken, int numberOfTokens) {

        this.schedulerAction = schedulerAction;
        this.bPMNToken = bPMNToken;
        this.numberOfTokens = numberOfTokens;
    }

    /**
     * Gets the number of instances that are currently being scheduled.
     * 
     * @return the number of instances
     */
    public int getNumberOfTokens() {

        return numberOfTokens;
    }

    /**
     * Gets the process token.
     * If the action was SUBMIT it is the submitted processtoken.
     * If the action was RETRIEVE it is the retrieved processtoken.
     * You see, in the latter case this may be null (if the queue is empty)
     * 
     * @return the process instance
     */
    public BPMNToken getProcessToken() {

        return bPMNToken;
    }

    /**
     * Gets the scheduler action.
     * May be SUBMIT or RETRIEVE.
     * 
     * @return the scheduler action
     * @see SchedulerAction
     */
    public SchedulerAction getSchedulerAction() {

        return schedulerAction;
    }

    @Override
    public String toString() {

        return String.format("SchedulerEvent [numberOfTokens=%d; schedulerAction=%s]", numberOfTokens, schedulerAction);
    }

}
