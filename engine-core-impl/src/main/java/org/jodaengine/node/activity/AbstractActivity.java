package org.jodaengine.node.activity;

import javax.annotation.Nonnull;

import org.jodaengine.process.token.BPMNToken;


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
    public void execute(@Nonnull BPMNToken bPMNToken) {
        executeIntern(bPMNToken);
    }
    
    /**
     * Method, which implements the concrete's activity's implementation.
     * 
     * @param bPMNToken the instance this activity operates on
     */
    protected abstract void executeIntern(@Nonnull BPMNToken bPMNToken);
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
    /**
     * Override this, if need to cleanup, if the activity is cancelled (e.g. event deregistration).
     *
     * @param executingToken the executing token
     */
    @Override
    public void cancel(BPMNToken executingToken) {
        
    }
    
    /**
     * Override this, if you need to do cleanup after the executing token has been resumed.
     *
     * @param bPMNToken the token
     */
    @Override
    public void resume(BPMNToken bPMNToken) {
        
    }
}
