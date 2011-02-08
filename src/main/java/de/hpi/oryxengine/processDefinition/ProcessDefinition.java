package de.hpi.oryxengine.processDefinition;

import java.util.ArrayList;

import de.hpi.oryxengine.node.AbstractNode;

public interface ProcessDefinition {
	
	//void setTransition(Node start, Node end);
	void setStartNodes(ArrayList<AbstractNode> n);

}
