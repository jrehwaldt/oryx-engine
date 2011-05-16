package org.jodaengine.navigator;

import org.jodaengine.navigator.schedule.Scheduler;
import org.jodaengine.process.token.Token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class NavigationThread. Which is one thread to navigate.
 */
public class NavigationThread extends Thread {

    /** The Constant SLEEPTIME defining the time a thread sleeps if it has got nothing to do. */
    private static final int SLEEPTIME = 1000;

    /**
     * The to navigate Queue is common to all Navigation Threads. This is where they get the instances to work on from.
     */
    private Scheduler scheduler;

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** The should stop. Indicates if the Thread should stop executing. See the doWork source for details. */
    private boolean shouldStop = false;

    private boolean threadDone;

    /**
     * Instantiates a new navigation thread.
     * 
     * @param threadname
     *            the thread name
     * @param scheduler
     *            the scheduler
     */
    public NavigationThread(String threadname, Scheduler scheduler) {

        super(threadname);
        this.scheduler = scheduler;
        logger.info("Navigator {} initialized", threadname);
        this.threadDone = false;
    }

    /**
     * The run method which starts the execution of the thread.
     * 
     * @see java.lang.Thread#run()
     */
    public void run() {

        if (!threadDone) {
            doWork();
        }
        logger.info("Navigator {} terminated", this.getName());
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
                threadDone = true;
                break;
            }

            Token token = null;

            // This has to be an atomic operation on toNavigate, otherwise
            // an IndexOutOfBoundsException might occur

            token = this.scheduler.retrieve();

            if (token != null) {
                // List<Token> instances;
                // instances = null;
                try {
                    // the return value of executeStep are the nodes which shall be executed next
                    // instances = token.executeStep();
                    token.executeStep();
                } catch (Exception e) {

                    e.printStackTrace();
                }
                // submit all instances to be executed next to the scheduler
                // scheduler.submitAll(instances);
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
     * Sets the should stop.
     * 
     * @param shouldStop
     *            the new should stop
     */
    public void setShouldStop(boolean shouldStop) {

        this.shouldStop = shouldStop;
    }
}
