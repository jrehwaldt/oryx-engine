package de.hpi.oryxengine.activity;

import java.util.Observable;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.process.instance.ProcessInstance;


/**
 * The Class AbstractActivityImpl.
 * An activity is the behaviour of a node. So to say what it does.
 */
public abstract class AbstractActivityImpl
extends Observable
implements Activity {
    
    private ExecutionState state;
    
    /**
     * Instantiates a new activity. State is set to INIT.
     */
    protected AbstractActivityImpl() {
        changeState(ExecutionState.INIT);
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
     * @param state the new state
     */
    private void changeState(@Nonnull ExecutionState state) {
        this.state = state;
        setChanged();
        notifyObservers(state);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void execute(@Nonnull ProcessInstance instance) {
        changeState(ExecutionState.ACTIVE);
        executeIntern(instance);
        changeState(ExecutionState.COMPLETED);
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
}
