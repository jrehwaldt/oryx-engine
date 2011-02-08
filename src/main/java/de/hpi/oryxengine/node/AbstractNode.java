package de.hpi.oryxengine.node;

import java.util.ArrayList;

import de.hpi.oryxengine.activity.AbstractActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractNode.
 * Which is used for the graph representation of a Process
 */
public abstract class AbstractNode implements NodeInterface {

	/** The activity. */
	protected AbstractActivity activity;
	
	/** The next node. */
	protected AbstractNode nextNode;
	
	/**
	 * Instantiates a new abstract node.
	 *
	 * @param activity the activity to be executed
	 */
	AbstractNode(AbstractActivity activity) {
		this.activity = activity;
	}
	
	/**
	 * Instantiates a new abstract node.
	 *
	 * @param activity the activity to be executed
	 * @param next the next node
	 */
	AbstractNode(AbstractActivity activity, AbstractNode next) {
		this(activity);
		this.nextNode = next;
	}
	
	/**
	 * Gets the activity.
	 *
	 * @return the activity to be executed
	 */
	public AbstractActivity getActivity() {
		return activity;
	}

	/**
	 * Sets the activity.
	 *
	 * @param activity the new activity
	 */
	public void setActivity(AbstractActivity activity) {
		this.activity = activity;
	}

	/**
	 * Gets the next node.
	 *
	 * @return the next node
	 */
	public AbstractNode getNextNode() {
		return nextNode;
	}

	/**
	 * Sets the next node.
	 *
	 * @param nextNode the new next node
	 */
	public void setNextNode(AbstractNode nextNode) {
		this.nextNode = nextNode;
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
	public ArrayList<AbstractNode> next() {
		ArrayList<AbstractNode> nexts = new ArrayList<AbstractNode>();
		nexts.add(this.nextNode);
		
		return nexts;
	}

}
