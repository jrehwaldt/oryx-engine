package de.hpi.oryxengine.factory.process;

import java.util.Set;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.resource.Participant;

/**
 * The Class PseudoHumanThread is used to simulate human behaviour on Worklists. We use it for benchmarking our human
 * tasks. As is, it is used in processes that have human tasks in the load generator.
 */
public class PseudoHumanThread extends Thread {
    private String name;

    /**
     * Instantiates a new pseudo human thread with the corresponding name.
     * 
     * @param name
     *            the name of the pseudo human thread
     */
    public PseudoHumanThread(String name) {

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
