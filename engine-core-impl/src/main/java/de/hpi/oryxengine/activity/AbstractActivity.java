package de.hpi.oryxengine.activity;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.plugin.AbstractPluggable;
import de.hpi.oryxengine.plugin.activity.AbstractActivityLifecyclePlugin;
import de.hpi.oryxengine.plugin.activity.ActivityLifecycleChangeEvent;
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

//    @Override
//    public @Nonnull ActivityState getState() {
//        return state;
//    }
//    
//    /**
//     * Changes the state of the node.
//     *
//     * @param token the process token
//     * @param state the new state
//     */
//    protected void changeState(@Nonnull Token token,
//                             @Nonnull ActivityState state) {
//        final ActivityState prevState = this.state;
//        this.state = state;
//        setChanged();
//        notifyObservers(new ActivityLifecycleChangeEvent(this, prevState, this.state, token));
//    }
    
    @Override
    public void execute(@Nonnull Token token) {
//        changeState(token, ActivityState.ACTIVE);
        executeIntern(token);
//        changeState(token, ActivityState.COMPLETED);
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
