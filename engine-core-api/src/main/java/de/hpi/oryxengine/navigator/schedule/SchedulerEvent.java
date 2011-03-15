package de.hpi.oryxengine.navigator.schedule;

import de.hpi.oryxengine.process.token.Token;

// TODO: Auto-generated Javadoc
/**
 * An Event for the scheduler listener, may be extended.
 * For now it contains everything, that it needs to contain.
 * Please referr to the respective getters for more information.
 */
public final class SchedulerEvent {
    
    /** The number of tokens. */
    private final int numberOfTokens;
    
    /** The token. */
    private final Token token;
    
    /** The scheduler action. */
    private final SchedulerAction schedulerAction;
    
    /**
     * Instantiates a new scheduler event.
     *
     * @param schedulerAction the scheduler action
     * @param token the process instance
     * @param numberOfTokens the number of instances
     */
    public SchedulerEvent(SchedulerAction schedulerAction, Token token, int numberOfTokens) {
        this.schedulerAction = schedulerAction;
        this.token = token;
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
    public Token getProcessToken() {
    
        return token;
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

    // TODO does this work (write a test bitch!)
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        return "SchedulerEvent [numberOfTokens=" + numberOfTokens + "schedulerAction=" + schedulerAction + "]";
    }
    

}
