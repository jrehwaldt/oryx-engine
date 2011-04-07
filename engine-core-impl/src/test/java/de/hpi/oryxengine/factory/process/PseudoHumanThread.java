package de.hpi.oryxengine.factory.process;

import java.util.Set;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.resource.Participant;

// TODO: Auto-generated Javadoc
/**
 * The Class PseudoHumanThread.
 */
public class PseudoHumanThread extends Thread{
    private String name;
    
    public PseudoHumanThread (String name) {
        this.name = name;
    }
    
    /**
     * The run method which starts the execution of the thread.
     * 
     * @see java.lang.Thread#run()
     */
    public void run() {
        Set<Participant> participants = ServiceFactory.getIdentityService().getParticipants();
        

    }



}
