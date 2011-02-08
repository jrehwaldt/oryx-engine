package de.hpi.oryxengine.node.implementations;

import de.hpi.oryxengine.node.AbstractNode;

public class AutomatedDummyNode extends AbstractNode {
	
	private String message;

	AutomatedDummyNode(String s) {
		super();
		message = s;
	}

	@Override
	// A simple execution 
	// all the state setting may be handled by superclass later on
	public void execute() {
		this.state = State.RUNNING;
		System.out.println(this.message);
		this.state = State.TERMINATED;
	}

}
