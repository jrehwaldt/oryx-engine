package de.hpi.oryxengine.activity;

import de.hpi.oryxengine.processInstance.ProcessInstance;


/**
 * The Class AbstractActivityImpl.
 * An activity is the behaviour of a node. So to say what it does.
 */
public abstract class AbstractActivityImpl implements Activity {

    /** The state. */
    private State state;

    /**
     * Instantiates a new abstract activity implementation.
     * It already sets the State to INIT.
     */
    protected AbstractActivityImpl() {

        this.state = State.INIT;
    }

    /**
     * Gets the state of the node.
     *
     * @return the state
     */
    public State getState() {

        return state;
    }

    /**
     * Sets the state of the node.
     *
     * @param state
     *            the new state
     */
    public void setState(State state) {

        this.state = state;
    }

    /**
     * The Enum State.
     * It contains the different states defined in the context of our process engine.
     */
    protected enum State {

        /** The INIT. Node is initializing. */
        INIT,
        /** The READY. The node is initialized and ready to do its work. */
        READY,
        /** The RUNNING. Node is currently executing. */
        RUNNING,
        /** The TERMINATED. Node has finished execution. */
        TERMINATED,
        /** The SKIPPED. We decided not to continue execution. */
        SKIPPED
    }

    // start execution
    /**
     * Execute.
     * Execute what the activity does. 
     * Default behaviour is doing nothing.
     *
     * @param instance the processinstance - this is needed because there might be valuable data in there.
     * @see de.hpi.oryxengine.activity.ActivityInterface#execute()
     */
    public void execute(ProcessInstance instance) {

        // Doing nothing is the default behavior
    }
}
