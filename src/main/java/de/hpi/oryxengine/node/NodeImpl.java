package de.hpi.oryxengine.node;

import java.util.ArrayList;

import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.navigator.NavigatorInterface;
import de.hpi.oryxengine.navigator.impl.Navigator;
import de.hpi.oryxengine.processInstance.ProcessInstance;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractNode.
 * Which is used for the graph representation of a Process
 */
public class NodeImpl implements NodeInterface {

	/** The activity. */
	protected AbstractActivity activity;
	
	/** The next node. */
	protected NodeImpl nextNode;
	
	protected ProcessInstance processInstance;
	/**
	 * Instantiates a new abstract node.
	 *
	 * @param activity the activity to be executed
	 */
	public NodeImpl(AbstractActivity activity, ProcessInstance instance) {
		this.activity = activity;
		this.processInstance = instance;
	}
	
	/**
	 * Instantiates a new abstract node.
	 *
	 * @param activity the activity to be executed
	 * @param next the next node
	 */
	public NodeImpl(AbstractActivity activity, ProcessInstance instance, NodeImpl next) {
		this(activity, instance);
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
	public NodeImpl getNextNode() {
		return nextNode;
	}

	/**
	 * Sets the next node.
	 *
	 * @param nextNode the new next node
	 */
	public void setNextNode(NodeImpl nextNode) {
		this.nextNode = nextNode;
	}
	
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	/* (non-Javadoc)
	 * @see de.hpi.oryxengine.node.NodeInterface#execute()
	 */
	public void execute(NavigatorInterface navigator) {
		this.activity.execute();
		navigator.signal(this);
		
		//tell navigator that execution is finished
	}
	
	/* (non-Javadoc)
	 * @see de.hpi.oryxengine.node.NodeInterface#next()
	 */
	public ArrayList<NodeImpl> next() {
		ArrayList<NodeImpl> nexts = new ArrayList<NodeImpl>();
		nexts.add(this.nextNode);
		
		return nexts;
	}
}
