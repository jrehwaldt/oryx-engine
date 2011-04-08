package de.hpi.oryxengine.navigator;

import javax.annotation.Nonnegative;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Container class for statistic information.
 * 
 * @author Jan Rehwaldt
 */
@XmlRootElement
public final class NavigatorStatistic {

    @XmlElement
    private int numberOfFinishedInstances;
    
    @XmlElement
    private int numberOfRunningInstances;
    
    @XmlElement
    private boolean isNavigatorIdle;
    
    @XmlElement
    private int numberOfExecutionThreads;

    /**
     * Default constructor.
     * 
     * @param numberOfFinishedInstances the number of finished instances
     * @param numberOfRunningInstances the number of running instances
     * @param isNavigatorIdle whether the the navigator is idle
     * @param numberOfExecutionThreads number of execution threads
     */
    public NavigatorStatistic(@Nonnegative int numberOfFinishedInstances,
                              @Nonnegative int numberOfRunningInstances,
                              @Nonnegative int numberOfExecutionThreads,
                              boolean isNavigatorIdle) {
        
        this.numberOfFinishedInstances = numberOfFinishedInstances;
        this.numberOfRunningInstances = numberOfRunningInstances;
        this.isNavigatorIdle = isNavigatorIdle;
        this.numberOfExecutionThreads = numberOfExecutionThreads;
    }

    /**
     * Returns the number of finished instances.
     * 
     * @return the number of finished instances
     */
    public int getNumberOfFinishedInstances() {
        return numberOfFinishedInstances;
    }

    /**
     * Returns the number of running instances.
     * 
     * @return the number of running instances
     */
    public int getNumberOfRunningInstances() {
        return numberOfRunningInstances;
    }

    /**
     * Returns, whether the navigator is idle.
     * 
     * @return true, if the navigator is idle
     */
    public boolean isNavigatorIdle() {
        return isNavigatorIdle;
    }
    
    /**
     * Returns the number of execution threads.
     * 
     * @return the number of execution threads
     */
    public int getNumberOfExecutionThreads() {
        return numberOfExecutionThreads;
    }
    
}
