package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.AbstractActivityImpl;
import de.hpi.oryxengine.processInstance.ProcessInstance;

/**
 * The Class AutomatedDummyNode.
 * It really is dumb. It just prints out whatever message is send to it.
 */
public class AutomatedDummyActivity extends AbstractActivityImpl {

    /** This is the message the node prints out during its execution. */
    private String message;

    /**
     * Instantiates a new automated dummy node.
     *  
     * @param s
     *            the String which message gets set to and which gets printed out.
     */
    public AutomatedDummyActivity(String s) {

        super();
        this.message = s;
    }

    /**
     * @see de.hpi.oryxengine.activity.AbstractActivityImpl#execute()
     * 
     * @param instance the processinstance
     */
    @Override
    // A simple execution
    // all the state setting may be handled by superclass later on
    public void execute(ProcessInstance instance) {

        this.setState(State.RUNNING);
        System.out.println(this.message);
        this.setState(State.TERMINATED);
    }

}
