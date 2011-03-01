package de.hpi.oryxengine.activity;

import java.util.Observable;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.plugin.AbstractActivityLifecyclePlugin;
import de.hpi.oryxengine.plugin.ActivityLifecycleChangeEvent;
import de.hpi.oryxengine.process.instance.ProcessInstance;


/**
 * The Class AbstractActivityImpl.
 * An activity is the behaviour of a node. So to say what it does.
 */
public abstract class AbstractActivity
extends Observable
implements Activity<AbstractActivityLifecyclePlugin> {
    
    // TODO Refactor: move activity state into ProcessInstance
    private ExecutionState state = ExecutionState.INIT;
    
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
    public @Nonnull ExecutionState getState() {
        return state;
    }
    
    /**
     * Changes the state of the node.
     *
     * @param instance the process instance
     * @param state the new state
     */
    private void changeState(@Nonnull ProcessInstance instance,
                             @Nonnull ExecutionState state) {
        final ExecutionState prevState = this.state;
        this.state = state;
        setChanged();
        notifyObservers(new ActivityLifecycleChangeEvent(this, prevState, this.state, instance));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void execute(@Nonnull ProcessInstance instance) {
        changeState(instance, ExecutionState.ACTIVE);
        executeIntern(instance);
        changeState(instance, ExecutionState.COMPLETED);
    }
    
    /**
     * Method, which implements the concrete's activity's implementation.
     * 
     * @param instance the instance this activity operates on
     */
    protected abstract void executeIntern(@Nonnull ProcessInstance instance);
    
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
