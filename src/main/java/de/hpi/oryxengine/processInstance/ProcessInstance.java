package de.hpi.oryxengine.processInstance;

import de.hpi.oryxengine.processstructure.impl.NodeImpl;

public interface ProcessInstance {

	NodeImpl getCurrentNode();
	void setCurrentNode(NodeImpl node);
	String getID();
	void setID(String s);
	void setVariable(String name, Object value);
	Object getVariable(String name);
	
}
