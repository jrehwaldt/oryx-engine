package de.hpi.oryxengine.node;

import de.hpi.oryxengine.activity.AbstractActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractNode.
 * Which is used for the graph representation of a Process
 */
public class AbstractNode implements NodeInterface {

	/** The activity. */
	protected AbstractActivity activity;
	
	/** The next node. */
	protected AbstractNode nextNode;
	
	AbstractNode(AbstractActivity activity) {
		this.activity = activity;
	}
	
	/* (non-Javadoc)
	 * @see de.hpi.oryxengine.node.NodeInterface#execute()
	 */
	public void execute() {
		this.activity.execute();
	}
	
	/* (non-Javadoc)
	 * @see de.hpi.oryxengine.node.NodeInterface#next()
	 */
	public Object next() {
		return this.nextNode;
	}

}
