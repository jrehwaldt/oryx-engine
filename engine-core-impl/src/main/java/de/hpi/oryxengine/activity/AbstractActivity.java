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
    
    // TODO Refactor: move activity state into ProcessInstance
    private ActivityState state = ActivityState.INIT;
    
    /**
     * Instantiates a new activity. State is set to INIT, but observers will not
     * be notified (since no observer are registered so far).
     * 
     */
    protected AbstractActivity() {
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull ActivityState getState() {
        return state;
    }
    
    /**
     * Changes the state of the node.
     *
     * @param instance the process instance
     * @param state the new state
     */
    private void changeState(@Nonnull Token instance,
                             @Nonnull ActivityState state) {
        final ActivityState prevState = this.state;
        this.state = state;
        setChanged();
        notifyObservers(new ActivityLifecycleChangeEvent(this, prevState, this.state, instance));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void execute(@Nonnull Token instance) {
        changeState(instance, ActivityState.ACTIVE);
        executeIntern(instance);
        changeState(instance, ActivityState.COMPLETED);
    }
    
    /**
     * Method, which implements the concrete's activity's implementation.
     * 
     * @param instance the instance this activity operates on
     */
    protected abstract void executeIntern(@Nonnull Token instance);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerPlugin(@Nonnull AbstractActivityLifecyclePlugin plugin) {
        addObserver(plugin);
    }
}
