package de.hpi.oryxengine.processInstanceImpl;

import java.util.ArrayList;

import de.hpi.oryxengine.node.NodeImpl;
import de.hpi.oryxengine.processDefinitionImpl.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.processInstance.ProcessInstance;

public class ProcessInstanceImpl implements ProcessInstance{
	
	private String id;
	private ArrayList<NodeImpl> currentNodes;

	public ProcessInstanceImpl(AbstractProcessDefinitionImpl processDefinition) {
		currentNodes = processDefinition.getStartNodes();
	}
	
	public ProcessInstanceImpl(ArrayList<NodeImpl> startNodes){
		currentNodes = startNodes;
		for (NodeImpl node : startNodes) {
			node.setProcessInstance(this);
		}
	}
	public ArrayList<NodeImpl> getCurrentNodes() {
		return currentNodes;
	}

	public String getID() {
		return id;
	}

	public void setID(String s) {
		id = s;
		
	}

	public void setVariable(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	public String getVariable(String name) {
		// TODO Auto-generated method stub
		return null;
	}
}