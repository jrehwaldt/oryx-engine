package org.jodaengine.node.activity;

import org.jodaengine.process.token.Token;

import javax.annotation.Nonnull;


/**
 * The Class AbstractActivityImpl.
 * An activity is the behaviour of a node. It does not perform any control flow routing operations.
 */
public abstract class AbstractActivity
implements Activity {
    
    /**
     * Instantiates a new activity. State is set to INIT, but observers will not
     * be notified (since no observer are registered so far).
     * 
     */
    protected AbstractActivity() {
    }
    
    @Override
    public void execute(@Nonnull Token token) {
        executeIntern(token);
    }
    
    /**
     * Method, which implements the concrete's activity's implementation.
     * 
     * @param token the instance this activity operates on
     */
    protected abstract void executeIntern(@Nonnull Token token);
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
    /**
     * Override this, if need to cleanup, if the activity is cancelled (e.g. event deregistration).
     */
    @Override
    public void cancel() {
        
    }
}
