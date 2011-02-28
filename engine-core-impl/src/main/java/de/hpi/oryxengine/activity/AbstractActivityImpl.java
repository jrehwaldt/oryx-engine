package de.hpi.oryxengine.activity;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.process.instance.ProcessInstance;


/**
 * The Class AbstractActivityImpl.
 * An activity is the behaviour of a node. So to say what it does.
 */
public abstract class AbstractActivityImpl
implements Activity {
    
    private ExecutionState state;
    
    /**
     * Instantiates a new activity. State is set to INIT.
     */
    protected AbstractActivityImpl() {
        this.state = ExecutionState.INIT;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull ExecutionState getState() {
        return state;
    }
    
    /**
     * Sets the state of the node.
     *
     * @param state the new state
     */
    protected void setState(@Nonnull ExecutionState state) {
        this.state = state;
    }

    // start execution
    /**
     * {@inheritDoc}
     */
    @Override
    public final void execute(@Nonnull ProcessInstance instance) {
        setState(ExecutionState.ACTIVE);
        executeIntern(instance);
        setState(ExecutionState.COMPLETED);
    }
    
    /**
     * Method, which implements the concrete's activity's implementation.
     * 
     * @param instance the instance this activity operates on
     */
    protected abstract void executeIntern(@Nonnull ProcessInstance instance);
}
