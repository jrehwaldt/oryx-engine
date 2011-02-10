package de.hpi.oryxengine.processInstance;

import java.util.ArrayList;

import de.hpi.oryxengine.node.NodeImpl;

public interface ProcessInstance {

	ArrayList<NodeImpl> getCurrentNodes();
	String getID();
	void setID(String s);
	void setVariable(String name, String value);
	String getVariable(String name);
	
}
