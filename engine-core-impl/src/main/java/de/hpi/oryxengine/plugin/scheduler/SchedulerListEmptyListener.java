package de.hpi.oryxengine.plugin.scheduler;

import de.hpi.oryxengine.loadgenerator.LoadGenerator;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The listener interface for receiving schedulerListEmpty events.
 * The class that is interested in processing a schedulerListEmpty
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSchedulerListEmptyListener</code> method. When
 * the schedulerListEmpty event occurs, that object's appropriate
 * method is invoked.
 *
 * @see SchedulerListEmptyEvent
 */
public final class SchedulerListEmptyListener 
extends AbstractSchedulerListener {

    private LoadGenerator hugene;
    private static SchedulerListEmptyListener myself;
    

    /**
     * Instantiates a new scheduler list empty listener.
     * Which is hidden from the outside since we want to have a singleton.
     *
     * @param gene the Load generator we want to report to.
     */
    private SchedulerListEmptyListener(LoadGenerator gene) {
        this.hugene = gene;
    }
    
    /**
     * Gets the single instance of SchedulerListEmptyListener.
     * Bad boy singleton action.
     *
     * @param hugene the Load generator we want to report to.
     * @return single instance of SchedulerListEmptyListener
     */
    public static SchedulerListEmptyListener getInstance(LoadGenerator hugene) {
        if (myself == null) {
            myself = new SchedulerListEmptyListener(hugene);
        }
        return myself;
    }
    
    @Override
    public void processInstanceSubmitted(int numberOfInstances, ProcessInstance processInstance) {

        // does nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInstanceRetrieved(int numberOfInstances, ProcessInstance processInstance) {

        if (numberOfInstances == 0) {
            myself.hugene.schedulerIsEmpty();
        }

    }

}
