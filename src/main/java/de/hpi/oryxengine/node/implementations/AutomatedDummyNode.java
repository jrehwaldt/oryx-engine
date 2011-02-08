package de.hpi.oryxengine.node.implementations;

import de.hpi.oryxengine.node.AbstractNode;

// TODO: Auto-generated Javadoc
/**
 * The Class AutomatedDummyNode.
 */
public class AutomatedDummyNode extends AbstractNode {
	
	/** This is the message the node prints out during its execution. */
	private String message;

	/**
	 * Instantiates a new automated dummy node.
	 *
	 * @param s the String which message gets set to.
	 */
	AutomatedDummyNode(String s) {
		super();
		message = s;
	}

	/** 
	 * @see de.hpi.oryxengine.node.AbstractNode#execute()
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
