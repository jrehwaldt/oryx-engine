package de.hpi.oryxengine.navigator.schedule;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.process.token.Token;

/**
 * The Class FIFOScheduler. It is a simple FIFO Scheduler so nothing too interesting going on around here.
 */
public class FIFOScheduler implements Scheduler {

    /** The process instances we would like to schedule. */
    private List<Token> processtokens;

    /**
     * Instantiates a new simple scheduler queue. Thereby instantiating a synchronized linked list.
     */
    public FIFOScheduler() {

        processtokens = new LinkedList<Token>();
        processtokens = Collections.synchronizedList(processtokens);
    }

    @Override
    public void submit(Token p) {
        processtokens.add(p);
    }

    @Override
    public Token retrieve() {
        Token removedToken;
        synchronized (this.processtokens) {
            if (this.processtokens.isEmpty()) {
                return null;
            }
            removedToken = processtokens.remove(0);
            
        }
        return removedToken;
    }
    
    @Override
    public boolean isEmpty() {
        return this.processtokens.isEmpty();
    }

    @Override
    public void submitAll(List<Token> listOfTokens) {
        this.processtokens.addAll(listOfTokens);  
    }
}
