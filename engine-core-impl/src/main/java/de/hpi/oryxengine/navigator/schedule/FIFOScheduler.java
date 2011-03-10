package de.hpi.oryxengine.navigator.schedule;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.plugin.AbstractPluggable;
import de.hpi.oryxengine.plugin.scheduler.AbstractSchedulerListener;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class FIFOScheduler. It is a simple FIFO Scheduler so nothing too interesting going on around here.
 */
public class FIFOScheduler extends AbstractPluggable<AbstractSchedulerListener> implements Scheduler {

    /** The process instances we would like to schedule. */
    private List<Token> processtokens;

    /**
     * Instantiates a new simple scheduler queue. Thereby instantiating a synchronized linked list.
     */
    public FIFOScheduler() {

        processtokens = new LinkedList<Token>();
        processtokens = Collections.synchronizedList(processtokens);
    }

    /**
     * Adds a process instance to our FIFO queue.
     * 
     * {@inheritDoc}
     */
    @Override
    public void submit(Token p) {

        changed(new SchedulerEvent(SchedulerAction.SUBMIT, p, processtokens.size()));
        processtokens.add(p);
    }

    /**
     * Gets the first element of the FIFO queue and returns it. 
     * {@inheritDoc}
     */
    @Override
    public Token retrieve() {
        Token removedToken;
        synchronized (this.processtokens) {
            if (this.processtokens.isEmpty()) {
                return null;
            }
            removedToken = processtokens.remove(0);
            
        }
        changed(new SchedulerEvent(SchedulerAction.RETRIEVE, removedToken, processtokens.size()));
        return removedToken;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return this.processtokens.isEmpty();
    }


    @Override
    // TODO right now we dont care about submitAll plugin/Observerwise
    public void submitAll(List<Token> listOfTokens) {
        this.processtokens.addAll(listOfTokens);  
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
