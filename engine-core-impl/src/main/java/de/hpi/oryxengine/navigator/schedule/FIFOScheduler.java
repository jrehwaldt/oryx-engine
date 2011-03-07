package de.hpi.oryxengine.navigator.schedule;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.plugin.AbstractPluggable;
import de.hpi.oryxengine.plugin.scheduler.AbstractSchedulerListener;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class FIFOScheduler. It is a simple FIFO Scheduler so nothing too interesting going on around here.
 */
public class FIFOScheduler extends AbstractPluggable<AbstractSchedulerListener> implements Scheduler {

    /** The process instances we would like to schedule. */
    private List<ProcessInstance> processinstances;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new simple scheduler queue. Thereby instantiating a synchronized linked list.
     */
    public FIFOScheduler() {

        processinstances = new LinkedList<ProcessInstance>();
        processinstances = Collections.synchronizedList(processinstances);
    }

    /**
     * Adds a process instance to our FIFO queue.
     * 
     * {@inheritDoc}
     */
    @Override
    public void submit(ProcessInstance p) {

        changed(new SchedulerEvent(SchedulerAction.SUBMIT, p, processinstances.size()));
        processinstances.add(p);
    }

    /**
     * Gets the first element of the FIFO queue and returns it. 
     * {@inheritDoc}
     */
    @Override
    public ProcessInstance retrieve() {
        ProcessInstance removedInstance;
        synchronized (this.processinstances) {
            if (this.processinstances.isEmpty()) {
                return null;
            }
            removedInstance = processinstances.remove(0);
            
        }
        changed(new SchedulerEvent(SchedulerAction.RETRIEVE, removedInstance, processinstances.size()));
        return removedInstance;
    }
    
    public boolean isEmpty(){
        return this.processinstances.isEmpty();
    }


    @Override
    // TODO right now we dont care about submitAll plugin/Observerwise
    public void submitAll(List<ProcessInstance> listOfInstances) {
        this.processinstances.addAll(listOfInstances);  
    }
    
    /**
     * We changed, tell everybody now!
     *
     * @param event the event
     */
    private void changed(SchedulerEvent event) {
        setChanged();
        notifyObservers(event);
    }
    
    
}
