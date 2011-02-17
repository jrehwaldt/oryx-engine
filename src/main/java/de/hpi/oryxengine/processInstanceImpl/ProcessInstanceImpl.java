package de.hpi.oryxengine.processInstanceImpl;

import java.util.ArrayList;

import de.hpi.oryxengine.node.NodeImpl;
import de.hpi.oryxengine.processDefinitionImpl.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.processInstance.ProcessInstance;

public class ProcessInstanceImpl implements ProcessInstance{
	
	private String id;
	//private ArrayList<NodeImpl> currentNodes;
	NodeImpl currentNode;
	private ArrayList<ProcessInstanceImpl> childInstances;

	public ProcessInstanceImpl(AbstractProcessDefinitionImpl processDefinition, Integer startNumber) {
		
		//choose a start Node from the possible List of Nodes
		ArrayList<NodeImpl> startNodes = processDefinition.getStartNodes();
		currentNode = startNodes.get(startNumber);

	}
	
	public ProcessInstanceImpl(NodeImpl startNodes){
		currentNode = startNodes;
	}
	public NodeImpl getCurrentNode() {
		return currentNode;
	}
	
    

	public ArrayList<ProcessInstanceImpl> getChildInstances() {
		return childInstances;
	}

	public void setChildInstances(ArrayList<ProcessInstanceImpl> childInstances) {
		this.childInstances = childInstances;
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

	public void setCurrentNode(NodeImpl node) {
		currentNode = node;
		
	}
}