package de.hpi.oryxengine.processDefinitionImpl;

import java.util.ArrayList;

import de.hpi.oryxengine.node.NodeImpl;
import de.hpi.oryxengine.processDefinition.ProcessDefinition;

public class AbstractProcessDefinitionImpl implements ProcessDefinition {

	protected ArrayList<NodeImpl> startNodes;
	protected String id;
	
	public void setStartNodes(ArrayList<NodeImpl> nodes) {
		// TODO Auto-generated method stub
		this.startNodes = nodes;
	}

	public ArrayList<NodeImpl> getStartNodes(){
		return startNodes;
	}

	public String getID() {
		return id;
	}

	public void setID(String s) {
		this.id = s;
	}

}
