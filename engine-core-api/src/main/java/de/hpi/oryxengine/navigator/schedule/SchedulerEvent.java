package de.hpi.oryxengine.navigator.schedule;

import de.hpi.oryxengine.process.token.Token;

/**
 * An Event for the scheduler listener, may be extended.
 * For now it contains everything, that it needs to contain.
 * Please referr to the respective getters for more information.
 */
public final class SchedulerEvent {
    private final int numberOfTokens;
    private final Token token;
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
     * @see SchedulerAction
     *
     * @return the scheduler action
     */
    public SchedulerAction getSchedulerAction() {
    
        return schedulerAction;
    }

    // TODO does this work (write a test bitch!)
    @Override
    public String toString() {

        return "SchedulerEvent [numberOfTokens=" + numberOfTokens + "schedulerAction=" + schedulerAction + "]";
    }
    

}
