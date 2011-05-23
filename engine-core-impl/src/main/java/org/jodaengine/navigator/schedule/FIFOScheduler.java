package org.jodaengine.navigator.schedule;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jodaengine.ext.AbstractPluggable;
import org.jodaengine.ext.listener.AbstractSchedulerListener;
import org.jodaengine.process.token.BPMNToken;


/**
 * The Class FIFOScheduler. It is a simple FIFO Scheduler so nothing too interesting going on around here.
 */
public class FIFOScheduler extends AbstractPluggable<AbstractSchedulerListener> implements Scheduler {

    /** The process instances we would like to schedule. */
    private List<BPMNToken> processtokens;

    /**
     * Instantiates a new simple scheduler queue. Thereby instantiating a synchronized linked list.
     */
    public FIFOScheduler() {

        processtokens = new LinkedList<BPMNToken>();
        processtokens = Collections.synchronizedList(processtokens);
    }

    @Override
    public void submit(BPMNToken p) {

        changed(new SchedulerEvent(SchedulerAction.SUBMIT, p, processtokens.size()));
        processtokens.add(p);
    }

    @Override
    public BPMNToken retrieve() {
        BPMNToken removedToken;
        synchronized (this.processtokens) {
            if (this.processtokens.isEmpty()) {
                return null;
            }
            removedToken = processtokens.remove(0);
            
        }
        changed(new SchedulerEvent(SchedulerAction.RETRIEVE, removedToken, processtokens.size()));
        return removedToken;
    }
    
    @Override
    public boolean isEmpty() {
        return this.processtokens.isEmpty();
    }

    @Override
    public void submitAll(List<BPMNToken> listOfTokens) {
        this.processtokens.addAll(listOfTokens);  
    }
    
    /**
     * We changed, tell everybody now!.
     *
     * @param event the event
     */
    private void changed(SchedulerEvent event) {
        setChanged();
        notifyObservers(event);
    }
    
    
}
