package de.hpi.oryxengine.node;

import java.util.ArrayList;

import de.hpi.oryxengine.activity.AbstractActivityImpl;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.transition.Transition;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractNode.
 * Which is used for the graph representation of a Process
 */
public class NodeImpl implements Node{

	/** The activity. */
	protected AbstractActivityImpl activity;
	
	/** The next node. */
	protected ArrayList<Transition> transitions;
	
	/**
	 * Instantiates a new abstract node.
	 *
	 * @param activity the activity to be executed
	 */
	public NodeImpl(AbstractActivityImpl activity) {
		this.activity = activity;
		this.transitions = new  ArrayList<Transition>();
	}
	
	/**
	 * Instantiates a new abstract node.
	 *
	 * @param activity the activity to be executed
	 * @param next the next node
	 */
	public NodeImpl(AbstractActivityImpl activity, Transition transition) {
		this(activity);
		this.transitionTo(transition);
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
	 * Sets the next node.
	 *
	 * @param nextNode the new next node
	 */
	
	public void transitionTo(Transition transition){
		this.transitions.add(transition);
	}

	/* (non-Javadoc)
	 * @see de.hpi.oryxengine.node.NodeInterface#execute()
	 */
	public void execute(ProcessInstance instance) {
		this.activity.execute();
		
		//save the instance the current node
		instance.setCurrentNode(this);
		
		//Save result in the navigator
		//....(missing yet)
		
	}

	public ArrayList<Transition> getTransitions() {
		// TODO Auto-generated method stub
		return transitions;
	}
	
	/* (non-Javadoc)
	 * @see de.hpi.oryxengine.node.NodeInterface#next()
	 */
}
