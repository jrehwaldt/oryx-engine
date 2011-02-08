package de.hpi.oryxengine.activity.implementations;

import de.hpi.oryxengine.activity.AbstractActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class AutomatedDummyNode.
 */
public class AutomatedDummyActivity extends AbstractActivity {
	
	/** This is the message the node prints out during its execution. */
	private String message;

	/**
	 * Instantiates a new automated dummy node.
	 *
	 * @param s the String which message gets set to.
	 */
	AutomatedDummyActivity(String s) {
		super();
		this.message = s;
	}

	/** 
	 * @see de.hpi.oryxengine.activity.AbstractActivity#execute()
	 */
	@Override
	// A simple execution 
	// all the state setting may be handled by superclass later on
	public void execute() {
		this.state = State.RUNNING;
		System.out.println(this.message);
		this.state = State.TERMINATED;
	}

}
