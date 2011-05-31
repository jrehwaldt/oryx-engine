package org.jodaengine.navigator.schedule;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jodaengine.ext.AbstractListenable;
import org.jodaengine.ext.listener.AbstractSchedulerListener;
import org.jodaengine.process.token.Token;
import org.springframework.ui.context.Theme;

/**
 * The Class RandomScheduler. Random scheduling is important, for modeling languages where are multiple ways possible.
 * An example could be in a petri net, where a token could be consumed by two different transitions.
 */
public class RandomPetriNetScheduler extends AbstractListenable<AbstractSchedulerListener> implements Scheduler {

    /** The process instances we would like to schedule. */
    private List<Token> processtokens;
    
    public RandomPetriNetScheduler() {

        this.processtokens = new LinkedList<Token>();
        this.processtokens = Collections.synchronizedList(processtokens);
    }
    
    @Override
    public void submit(Token p) {

        this.processtokens.add(p);
        
    }

    @Override
    public Token retrieve() {
        Token theChosenOne = null;
        synchronized(this.processtokens) {
            Collections.shuffle(this.processtokens);
            
            //TODO Only take it if not locked
            theChosenOne = this.processtokens.remove(0);
            //TODO lock all other tokens, which....GNARF this solution doesnt work, does it?
        }
            return theChosenOne;
    }

    @Override
    public boolean isEmpty() {

        return this.processtokens.isEmpty();
    }

    @Override
    public void submitAll(List<Token> listOfTokens) {

        this.processtokens.addAll(listOfTokens);        
    }
    

    @Override
    public int size() {

        return processtokens.size();
    }

}
