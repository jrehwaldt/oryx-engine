package de.hpi.oryxengine.navigator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.navigator.schedule.Scheduler;
import de.hpi.oryxengine.process.token.Token;

// TODO: Auto-generated Javadoc
/**
 * The Class NavigationThread. Which is one thread to navigate.
 */
public class NavigationThread
extends Thread {

    /** The Constant SLEEPTIME defining the time a thread sleeps if it has got nothing to do. */
    private static final int SLEEPTIME = 1000;

    /** The to navigate Queue is common to all Navigation Threads. 
     * This is where they get the instances to work on from. */
    private Scheduler scheduler;

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** The should stop. Indicates if the Thread should stop executing. See the doWork source for details. */
    private boolean shouldStop = false;

    /**
     * Instantiates a new navigation thread.
     *
     * @param threadname the thread name
     * @param scheduler the scheduler
     */
    public NavigationThread(String threadname, Scheduler scheduler) {
        
        super(threadname);
        this.scheduler = scheduler;
        logger.info("Navigator {} initialized", threadname);
    }

    /**
     * The run method which starts the execution of the thread.
     * 
     * @see java.lang.Thread#run()
     */
    public void run() {

        doWork();
    }

    /**
     * Do work. This method does the real work, pulling instances out of a queue and then executing. Main Loop: Takes an
     * executable process instance and the belonging node and executes the node. After, the true conditions are followed
     * and the next node is set. Now the process instance is added to the Queue again. This has the advantage that the
     * navigator can now handle multiple instances.
     */
    public void doWork() {

        while (true) {
            // TODO Das muss auf jeden fall verändert werden | English please whoever this was
            if (shouldStop) {
                break;
            }

            Token instance = null;

            // This has to be an atomic operation on toNavigate, otherwise
            // an IndexOutOfBoundsException might occur
            
            instance = this.scheduler.retrieve();


            if (instance != null) {
                List<Token> instances;
                instances = null;
                try {
                    // the return value of executeStep are the nodes which shall be executed next
                    instances = instance.executeStep();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // submit all instances to be executed next to the scheduler
                scheduler.submitAll(instances);
            } else {
                try {
                    // I simply couldn't take it anymore...
                    // logger.debug("Queue empty");
                    sleep(SLEEPTIME);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Gets the to navigate. The to navigate Queue is common to all Navigation Threads. This is where they get the
     * instances to work on from.
     * 
     * @return the to navigate
     */
    public Scheduler getScheduler() {

        return scheduler;
    }

    /**
     * Sets the scheduler. The scheduler is common to all Navigation Threads. This is where they get the
     * instances to work on from.
     *
     * @param scheduler the new scheduler
     */
    public void setToNavigate(Scheduler scheduler) {

        this.scheduler = scheduler;
    }

    /**
     * Checks if it should stop execution.
     * 
     * @return true, if it should stop execution
     */
    public boolean isShouldStop() {

        return shouldStop;
    }

    /**
     * Sets the should stop.
     * 
     * @param shouldStop
     *            the new should stop
     */
    public void setShouldStop(boolean shouldStop) {

        this.shouldStop = shouldStop;
    }
}
