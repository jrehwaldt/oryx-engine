package de.hpi.oryxengine.navigator;

import java.util.List;

import org.apache.log4j.Logger;

import de.hpi.oryxengine.process.instance.ProcessInstance;

// TODO Auto-generated Javadoc
/**
 * The Class NavigationThread. Which is one thread to navigate.
 */
public class NavigationThread extends Thread {

    /** The Constant SLEEPTIME defining the time a thread sleeps if it has got nothing to do. */
    private static final int SLEEPTIME = 1000;

    /** The to navigate Queue is common to all Navigation Threads. 
     * This is where they get the instances to work on from. */
    private List<ProcessInstance> toNavigate;

    /** The logger. */
    private Logger logger = Logger.getRootLogger();

    /** The should stop. Indicates if the Thread should stop executing. See the doWork source for details. */
    private boolean shouldStop = false;

    /**
     * Instantiates a new navigation thread.
     * 
     * @param threadname
     *            the threadname
     * @param activityQueue
     *            the activity queue which the navigation thread should work on. It contains Process Instances.
     */
    public NavigationThread(String threadname, List<ProcessInstance> activityQueue) {

        super(threadname);
        this.toNavigate = activityQueue;
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
            // TODO: Das muss auf jeden fall verändert werden
            if (shouldStop) {
                break;
            }

            ProcessInstance instance = null;

            // This has to be an atomic operation on toNavigate, otherwise
            // an IndexOutOfBoundsException might occur
            synchronized (this.toNavigate) {
                if (this.toNavigate.size() > 0) {
                    instance = this.toNavigate.remove(0);
                }
            }

            if (instance != null) {
                List<ProcessInstance> instances = instance.executeStep();
                toNavigate.addAll(instances);
            } else {
                try {
                    logger.debug("Queue empty");
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
    public List<ProcessInstance> getToNavigate() {

        return toNavigate;
    }

    /**
     * Sets the to navigate. The to navigate Queue is common to all Navigation Threads. This is where they get the
     * instances to work on from.
     * 
     * @param toNavigate
     *            the new to navigate
     */
    public void setToNavigate(List<ProcessInstance> toNavigate) {

        this.toNavigate = toNavigate;
    }

    /**
     * Gets the logger.
     * 
     * @return the logger
     */
    public Logger getLogger() {

        return logger;
    }

    /**
     * Sets the logger.
     * 
     * @param logger
     *            the new logger
     */
    public void setLogger(Logger logger) {

        this.logger = logger;
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
