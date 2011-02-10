package de.hpi.oryxengine.processInstance;

import java.util.ArrayList;

import de.hpi.oryxengine.node.NodeImpl;
import de.hpi.oryxengine.processDefinitionImpl.AbstractProcessDefinitionImpl;

public interface ProcessInstance {

	ArrayList<NodeImpl> getCurrentActivities();
	String getID();
	void setID(String s);
	void setVariable(String name, String value);
	String getVariable(String name);
	
}
