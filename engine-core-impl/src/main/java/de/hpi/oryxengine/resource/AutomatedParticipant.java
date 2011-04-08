package de.hpi.oryxengine.resource;

/**
 * The Class AutomatedParticipant for the load generator.
 */
public class AutomatedParticipant extends Participant {
    
    /** workingTime is the time this PseudoHumanThread needs to complete one task. Actually he just waits this time */
    private int workingTime;

    /**
     * Gets the working time, the time an Automated Participant needs to complete a task.
     *
     * @return the working time in milliseconds
     */
    public int getWorkingTime() {
    
        return workingTime;
    }

    /**
     * Instantiates a new automated participant with a proper name and a proper waiting time.
     *
     * @param participantName the participant name
     * @param waitingTime the waiting time
     */
    public AutomatedParticipant(String participantName, int waitingTime) {

        super(participantName);
        this.workingTime = waitingTime;
    }
    
    

}
