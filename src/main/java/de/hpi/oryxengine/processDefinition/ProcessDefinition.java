package de.hpi.oryxengine.processDefinition;

import java.util.ArrayList;

import de.hpi.oryxengine.node.NodeImpl;

public interface ProcessDefinition {
	
	//void setTransition(Node start, Node end);
	void setStartNodes(ArrayList<NodeImpl> n);
	
	String getID();
	void setID(String s);

}
