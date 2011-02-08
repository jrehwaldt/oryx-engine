package de.hpi.oryxengine.node;

public abstract class AbstractNode implements NodeInterface {
	
	private enum State {
		INIT, READY, RUNNING, TERMINATED, SKIPPED 
	}
	
	// start execution
	public abstract void execute();
}
