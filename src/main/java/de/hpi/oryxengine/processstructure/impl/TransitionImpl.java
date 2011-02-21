package de.hpi.oryxengine.processstructure.impl;

import de.hpi.oryxengine.processstructure.Condition;
import de.hpi.oryxengine.processstructure.Transition;


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
