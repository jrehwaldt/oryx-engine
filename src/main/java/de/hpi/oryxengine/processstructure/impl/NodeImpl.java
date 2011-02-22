package de.hpi.oryxengine.processstructure.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.activity.AbstractActivityImpl;
import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processstructure.Condition;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.Transition;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractNode.
 * Which is used for the graph representation of a Process
 */
public class NodeImpl implements Node {
	
	/** The activity. */
	protected Activity activity;
	
	/** The next node. */
	protected ArrayList<Transition> transitions;
	
	private String id; 
	
	/**
	 * Instantiates a new abstract node.
	 *
	 * @param activity the activity to be executed
	 */
	public NodeImpl(Activity activity) {
		this.activity = activity;
		this.transitions = new  ArrayList<Transition>();
	}
	
	/**
	 * Gets the activity.
	 *
	 * @return the activity to be executed
	 */
	public Activity getActivity() {
		return activity;
	}
	
	/**
	 * Sets the activity.
	 *
	 * @param activity the new activity
	 */
	public void setActivity(Activity activity) {
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
		this.activity.execute(instance);
	
		//TODO: Save result in the instance
		//instance.setVariable(...)....(missing yet)
		
	}
	
	public ArrayList<Transition> getTransitions() {
		return transitions;
	}

  public String getId() {
    return this.id;
  }
  
  public void setId(String id) {
	  this.id = id;
  }
  
  public List<ProcessInstance> navigate(ProcessInstance instance) {
	  List<ProcessInstance> instancesToNavigate = new LinkedList<ProcessInstance>();
	  if (transitions.size() == 1) {
		  Transition transition = transitions.get(0);
		  NodeImpl destination = transition.getDestination();
          instance.setCurrentNode(destination);
          instancesToNavigate.add(instance);
	  }
	  else {
		  for (Transition transition : transitions) {
	          // Create new child instances etc.
	      }
	  }
      
      return instancesToNavigate;
  }
	/* (non-Javadoc)
	 * @see de.hpi.oryxengine.node.NodeInterface#next()
	 */
}
