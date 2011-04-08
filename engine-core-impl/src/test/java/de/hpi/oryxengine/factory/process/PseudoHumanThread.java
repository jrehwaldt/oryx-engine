package de.hpi.oryxengine.factory.process;

import java.util.List;
import java.util.Set;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.worklist.WorklistItem;

/**
 * The Class PseudoHumanThread is used to simulate human behaviour on Worklists. We use it for benchmarking our human
 * tasks. As is, it is used in processes that have human tasks in the load generator.
 */
public class PseudoHumanThread extends Thread {
    private String name;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private Scheduler scheduler;

    /**
     * Instantiates a new pseudo human thread with the corresponding name.
     * 
     * @param name
     *            the name of the pseudo human thread
     * @throws SchedulerException 
     */
    public PseudoHumanThread(String name) throws SchedulerException {

        this.name = name;
        final SchedulerFactory factory = new org.quartz.impl.StdSchedulerFactory();
        this.scheduler = factory.getScheduler();
    }

    /**
     * The run method which starts the execution of the thread.
     * 
     * @see java.lang.Thread#run()
     */
    public void run() {

        // this has to be freaking substituted :-o
        Set<Participant> participants = ServiceFactory.getIdentityService().getParticipants();  
        
        // our trigger
        // repeat as long as there is work to be done for every participant
        while (ServiceFactory.getWorklistQueue().size(participants) > 0) {
            for (Participant participant : participants) {
                // we have to check whether the participant has work to do, otherwise we skip him
                if (ServiceFactory.getWorklistQueue().getWorklistItems(participant).size() > 0) {
                    // get an item and complete it
                    List<WorklistItem> items = ServiceFactory.getWorklistQueue().getWorklistItems(participant);
                    WorklistItem item = items.get(0);
                    ServiceFactory.getWorklistQueue().claimWorklistItemBy(item, participant);
                    // wait some time to simulate working duration
                    // do this using the awesome Quarz Scheduler
                    // TODO @Tobi&Jannik implement the waiting stuff
                    
                    // waited enough - work completed
                    ServiceFactory.getWorklistQueue().completeWorklistItemBy(item, participant);
                }
            }
        }

    }

}
