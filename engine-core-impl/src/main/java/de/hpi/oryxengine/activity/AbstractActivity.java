package de.hpi.oryxengine.activity;

import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.plugin.AbstractPluggable;
import de.hpi.oryxengine.plugin.activity.AbstractActivityLifecyclePlugin;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class AbstractActivityImpl.
 * An activity is the behaviour of a node. So to say what it does.
 */
public abstract class AbstractActivity
extends AbstractPluggable<AbstractActivityLifecyclePlugin>
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
    
    @Override
    public void registerPlugin(@Nonnull AbstractActivityLifecyclePlugin plugin) {
        addObserver(plugin);
    }
    
    /**
     * Override this, if need to cleanup, if the activity is cancelled (e.g. event deregistration).
     */
    @Override
    public void cancel() {
        
    }
}
