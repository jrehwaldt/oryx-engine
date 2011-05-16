package org.jodaengine.navigator;

import javax.annotation.Nonnegative;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Container class for statistic information.
 * 
 * @author Jan Rehwaldt
 */
public final class NavigatorStatistic {

    private int numberOfFinishedInstances;

    private int numberOfRunningInstances;

    private boolean navigatorIdle;

    private int numberOfExecutionThreads;

    /**
     * Hidden constructor.
     */
    protected NavigatorStatistic() {

    }

    /**
     * Default constructor.
     * 
     * @param numberOfFinishedInstances
     *            the number of finished instances
     * @param numberOfRunningInstances
     *            the number of running instances
     * @param navigatorIdle
     *            whether the the navigator is idle
     * @param numberOfExecutionThreads
     *            number of execution threads
     */
    public NavigatorStatistic(@Nonnegative int numberOfFinishedInstances,
                              @Nonnegative int numberOfRunningInstances,
                              @Nonnegative int numberOfExecutionThreads,
                              boolean navigatorIdle) {

        this.numberOfFinishedInstances = numberOfFinishedInstances;
        this.numberOfRunningInstances = numberOfRunningInstances;
        this.navigatorIdle = navigatorIdle;
        this.numberOfExecutionThreads = numberOfExecutionThreads;
    }

    /**
     * Returns the number of finished instances.
     * 
     * @return the number of finished instances
     */
    @JsonProperty
    public int getNumberOfFinishedInstances() {

        return numberOfFinishedInstances;
    }

    /**
     * Returns the number of running instances.
     * 
     * @return the number of running instances
     */
    @JsonProperty
    public int getNumberOfRunningInstances() {

        return numberOfRunningInstances;
    }

    /**
     * Returns, whether the navigator is idle.
     * 
     * @return true, if the navigator is idle
     */
    @JsonProperty
    public boolean isNavigatorIdle() {

        return navigatorIdle;
    }

    /**
     * Returns the number of execution threads.
     * 
     * @return the number of execution threads
     */
    @JsonProperty
    public int getNumberOfExecutionThreads() {

        return numberOfExecutionThreads;
    }
}
