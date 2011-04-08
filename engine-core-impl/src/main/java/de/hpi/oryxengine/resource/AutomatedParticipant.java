package de.hpi.oryxengine.resource;

/**
 * The Class AutomatedParticipant for the load generator.
 */
public class AutomatedParticipant extends Participant {
    
    private int waitingTime;

    public int getWaitingTime() {
    
        return waitingTime;
    }

    public AutomatedParticipant(String participantName, int waitingTime) {

        super(participantName);
        this.waitingTime = waitingTime;
    }
    
    

}
