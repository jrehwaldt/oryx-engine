package de.hpi.oryxengine.node;

import java.util.ArrayList;

import de.hpi.oryxengine.activity.AbstractActivityImpl;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.processInstance.ProcessInstance;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractNode.
 * Which is used for the graph representation of a Process
 */
public class NodeImpl implements Node{

	/** The activity. */
	protected AbstractActivityImpl activity;
	
	/** The next node. */
	protected ArrayList<NodeImpl> nextNodes;
	
	protected ProcessInstance processInstance;
	/**
	 * Instantiates a new abstract node.
	 *
	 * @param activity the activity to be executed
	 */
	public NodeImpl(AbstractActivityImpl activity) {
		this.activity = activity;
		this.nextNodes = new  ArrayList<NodeImpl>();
	}
	
	/**
	 * Instantiates a new abstract node.
	 *
	 * @param activity the activity to be executed
	 * @param next the next node
	 */
	public NodeImpl(AbstractActivityImpl activity, NodeImpl next) {
		this(activity);
		this.addNextNode(next);
	}
	
	public NodeImpl(AbstractActivityImpl activity, ArrayList<NodeImpl> nextNodes){
		// TODO: cooles SWA-Pattern, das Jannik so halb eingefallen ist und die vielen Konstruktoren verhindert
		this(activity);
		this.nextNodes = nextNodes;
	}
	
	/**
	 * Gets the activity.
	 *
	 * @return the activity to be executed
	 */
	public AbstractActivityImpl getActivity() {
		return activity;
	}

	/**
	 * Sets the activity.
	 *
	 * @param activity the new activity
	 */
	public void setActivity(AbstractActivityImpl activity) {
		this.activity = activity;
	}

	/**
	 * Gets the next node.
	 *
	 * @return the next node
	 */
	public ArrayList<NodeImpl> getNextNodes() {
		return this.nextNodes;
	}

	/**
	 * Sets the next node.
	 *
	 * @param nextNode the new next node
	 */
	public void setNextNodes(ArrayList<NodeImpl> nextNodes) {
		this.nextNodes = nextNodes;
	}
	
	public void addNextNode(NodeImpl node){
		this.nextNodes.add(node);
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
	public void execute(Navigator navigator) {
		this.activity.execute();
		navigator.signal(this);
		
		//tell navigator that execution is finished
	}
	
	/* (non-Javadoc)
	 * @see de.hpi.oryxengine.node.NodeInterface#next()
	 */
}
