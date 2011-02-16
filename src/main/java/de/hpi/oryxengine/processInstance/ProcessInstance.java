package de.hpi.oryxengine.processInstance;

import de.hpi.oryxengine.node.NodeImpl;

public interface ProcessInstance {

	NodeImpl getCurrentNode();
	String getID();
	void setID(String s);
	void setVariable(String name, String value);
	String getVariable(String name);
	
}
