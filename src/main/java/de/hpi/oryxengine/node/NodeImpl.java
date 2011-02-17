package de.hpi.oryxengine.node;

import java.util.ArrayList;

import de.hpi.oryxengine.TransitionImpl.TransitionImpl;
import de.hpi.oryxengine.activity.AbstractActivityImpl;
import de.hpi.oryxengine.condition.Condition;
import de.hpi.oryxengine.conditionImpl.ConditionImpl;
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
	
	public void transitionTo(NodeImpl node){
		Condition c = new ConditionImpl();
		Transition t = new TransitionImpl(this, node, c);
		this.transitions.add(t);
	}

	/* (non-Javadoc)
	 * @see de.hpi.oryxengine.node.NodeInterface#execute()
	 */
	public void execute(ProcessInstance instance) {
		this.activity.execute();

		//TODO: Save result in the instance
		//instance.setVariable(...)....(missing yet)
		
	}

	public ArrayList<Transition> getTransitions() {
		// TODO Auto-generated method stub
		return transitions;
	}
	
	/* (non-Javadoc)
	 * @see de.hpi.oryxengine.node.NodeInterface#next()
	 */
}
