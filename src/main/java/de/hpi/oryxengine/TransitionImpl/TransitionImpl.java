package de.hpi.oryxengine.TransitionImpl;

import de.hpi.oryxengine.condition.Condition;
import de.hpi.oryxengine.node.NodeImpl;
import de.hpi.oryxengine.transition.Transition;

public class TransitionImpl implements Transition{
	
	NodeImpl destination;
	NodeImpl start;
	Condition condition;
	
	public TransitionImpl(NodeImpl start, NodeImpl destination, Condition c){
		
		this.start = start;
		this.destination = destination;
		this.condition = c;
		
	}

	public Condition getCondition() {
		// TODO Auto-generated method stub
		return this.condition;
	}

	public NodeImpl getDestination() {
		
		return this.destination;
	}

	public NodeImpl getSource() {
		// TODO Auto-generated method stub
		return null;
	}

}
