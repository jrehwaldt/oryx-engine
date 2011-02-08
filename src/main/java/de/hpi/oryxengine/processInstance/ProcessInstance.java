package de.hpi.oryxengine.processInstance;

import java.util.ArrayList;

import de.hpi.oryxengine.node.AbstractNode;
import de.hpi.oryxengine.processDefinitionImpl.AbstractProcessDefinitionImpl;

public interface ProcessInstance {

	ArrayList<AbstractNode> getCurrentActivities();
	String getID();
	void setID(String s);
	void setProcessDefinition(AbstractProcessDefinitionImpl p);
	AbstractProcessDefinitionImpl getProcessDefinition();
	void setVariable(String name, String value);
	String getVariable(String name);
	
}
