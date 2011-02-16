package de.hpi.oryxengine.transition;

import de.hpi.oryxengine.node.NodeImpl;
import de.hpi.oryxengine.condition.Condition;

public interface Transition {
	
	Condition condition();
	NodeImpl getDestination();
	NodeImpl getSource();
}
