package de.hpi.oryxengine.navigator.schedule;

import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * An Event for the scheduler listener, may be extended.
 * For now it contains everything, that it needs to contain.
 * Please referr to the respective getters for more information.
 */
public final class SchedulerEvent {
    private final int numberOfInstances;
    private final ProcessInstance processInstance;
    private final SchedulerAction schedulerAction;
    
    /**
     * Instantiates a new scheduler event.
     *
     * @param schedulerAction the scheduler action
     * @param processInstance the process instance
     * @param numberOfInstances the number of instances
     */
    public SchedulerEvent(SchedulerAction schedulerAction, ProcessInstance processInstance, int numberOfInstances) {
        this.schedulerAction = schedulerAction;
        this.processInstance = processInstance;
        this.numberOfInstances = numberOfInstances;
    }
    
    /**
     * Gets the number of instances that are currently being scheduled.
     *
     * @return the number of instances
     */
    public int getNumberOfInstances() {
    
        return numberOfInstances;
    }
    
    /**
     * Gets the process instance.
     * If the action was SUBMIT it is the submitted processinstance.
     * If the action was RETRIEVE it is the retrieved processinstance.
     * You see, in the altter case this may be null (if the queue is empty)
     *
     * @return the process instance
     */
    public ProcessInstance getProcessInstance() {
    
        return processInstance;
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

        return "SchedulerEvent [numberOfInstances=" + numberOfInstances + "schedulerAction=" + schedulerAction + "]";
    }
    

}
