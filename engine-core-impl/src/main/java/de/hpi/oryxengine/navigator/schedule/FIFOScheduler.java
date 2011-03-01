package de.hpi.oryxengine.navigator.schedule;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class FIFOScheduler. It is a simple FIFO Scheduler so nothing too interesting going on around here.
 */
public class FIFOScheduler implements Scheduler {

    /** The process instances we would like to schedule. */
    private List<ProcessInstance> processinstances;

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

        processinstances.add(p);
    }

    /**
     * Gets the first element of the FIFO queue and returns it. 
     * {@inheritDoc}
     */
    @Override
    public ProcessInstance retrieve() {

        return processinstances.remove(0);
    }

    @Override
    public boolean isEmpty() {

        return this.processinstances.isEmpty();
    }

    @Override
    public void submitAll(List<ProcessInstance> listOfInstances) {
        
        this.processinstances.addAll(listOfInstances);    
    }
    
    
}
