package org.jodaengine.eventmanagement.timing.job;

import java.util.HashMap;
import java.util.Map;

import org.jodaengine.eventmanagement.timing.QuartzSpikeTest;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * This Job represents a simple {@link PullAdapterJob}. It belongs to the {@link QuartzSpikeTest#testCriticalJobRace()}
 * scenario.
 */
@DisallowConcurrentExecution
public class SynchronizedJob implements Job {

    private static int numberOfInvocation = 0;
    private static int numberOfInternalInvocation = 0;
    private static String nameOfInternalInvocer = null;
    private static boolean alreadyCalled = false;
    private static Map<String, Long> timeMillsTable = new HashMap<String, Long>();

    @Override
    public void execute(JobExecutionContext context)
    throws JobExecutionException {

        synchronized (SynchronizedJob.class) {

            numberOfInvocation++;

            String jobName = context.getJobDetail().getKey().getName();
            timeMillsTable.put(jobName, System.currentTimeMillis());

            synchronizedMethod(context);
        }
    }

    /**
     * The synchronized method.
     * 
     * @param context
     *            - the {@link JobExecutionContext} that provides more information
     */
    private synchronized void synchronizedMethod(JobExecutionContext context) {

        if (alreadyCalled) {
            return;
        }

        methodInSynchronizedBlock(context);
        alreadyCalled = true;
    }

    /**
     * A method within the synchronized block.
     * 
     * @param context
     *            - the {@link JobExecutionContext} that provides more information
     */
    private void methodInSynchronizedBlock(JobExecutionContext context) {

        nameOfInternalInvocer = context.getJobDetail().getKey().getName();
        numberOfInternalInvocation++;
    }

    /**
     * Resets the {@link SynchronizedJob}.
     */
    public static void reset() {

        numberOfInvocation = 0;
        numberOfInternalInvocation = 0;
        nameOfInternalInvocer = null;
        alreadyCalled = false;
    }

    /**
     * Getter for {@link SynchronizedJob#numberOfInvocation}.
     * 
     * @return a number representing how many time the job was actually executed
     */
    public static int getNumberOfInvocation() {

        return numberOfInvocation;
    }

    /**
     * Getter for {@link SynchronizedJob#numberOfInternalInvocation}.
     * 
     * @return a number representing how many time the
     *         {@link SynchronizedJob#methodInSynchronizedBlock(JobExecutionContext)} was actually executed
     */
    public static int getNumberOfInternalInvocation() {

        return numberOfInternalInvocation;
    }

    /**
     * Getter for {@link SynchronizedJob#nameOfInternalInvocer}.
     * 
     * @return the name of the {@link Job} that called the
     *         {@link SynchronizedJob#methodInSynchronizedBlock(JobExecutionContext)}
     */
    public static String getNameOfInternalInvocer() {

        return nameOfInternalInvocer;
    }

    /**
     * Returns the {@link Long time} when the {@link Job} was called.
     * 
     * @param jobName
     *            - the name of the {@link Job}
     * @return the {@link Long time} when the {@link Job} was called
     */
    public static long getInvocationTimeFor(String jobName) {

        return timeMillsTable.get(jobName);
    }
}
