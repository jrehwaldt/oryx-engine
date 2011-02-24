package de.hpi.oryxengine.navigator;

import java.util.List;

import org.apache.log4j.Logger;

import de.hpi.oryxengine.processInstance.ProcessInstance;

// TODO Auto-generated Javadoc
/**
 * The Class NavigationThread.
 * Which is one thread to navigate.
 */
public class NavigationThread extends Thread {

    /** The to navigate. */
    private List<ProcessInstance> toNavigate;

    /** The logger. */
    private Logger logger = Logger.getRootLogger();

    /** The should stop. Indicates if the Thread should stop executing. See the doWork source for details.*/
    private boolean shouldStop = false;

    /**
     * Instantiates a new navigation thread.
     * 
     * @param threadname
     *            the threadname
     * @param activityQueue
     *            the activity queue
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
     * Do work.
     * This method does the real work.
     * Main Loop: Takes an executable process instance and the belonging node
     * and executes the node. After, the true conditions are followed and the next
     * node is set.
     * Now the process instance is added to the Queue again. This has the
     * advantage that the navigator can now
     * handle multiple instances.
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
                    sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Gets the to navigate.
     * 
     * @return the to navigate
     */
    public List<ProcessInstance> getToNavigate() {

        return toNavigate;
    }

    /**
     * Sets the to navigate.
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
     * Checks if is should stop.
     * 
     * @return true, if is should stop
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
