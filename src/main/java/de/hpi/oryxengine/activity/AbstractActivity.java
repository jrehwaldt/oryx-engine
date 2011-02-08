package de.hpi.oryxengine.activity;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractNode.
 */
public abstract class AbstractActivity implements ActivityInterface {
	
	/** The state. */
	protected State state;
	
	/**
	 * Instantiates a new abstract node.
	 */
	protected AbstractActivity() {
		this.state = State.INIT;
	}
	
	/**
	 * The Enum State.
	 */
	protected enum State {
		
		/** The INIT. Node is initializing. */
		INIT, 
 /** The READY. The node is initialized and ready to do its work. */
 READY, 
 /** The RUNNING. Node is currently executing.*/
 RUNNING, 
 /** The TERMINATED. Node has finished execution. */
 TERMINATED, 
 /** The SKIPPED. */
 SKIPPED 
	}
	

	
	// start execution
	/**
	 * @see de.hpi.oryxengine.activity.ActivityInterface#execute()
	 */
	public abstract void execute();
}
