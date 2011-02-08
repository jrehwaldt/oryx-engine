package de.hpi.oryxengine.processDefinitionImpl;

import java.util.ArrayList;

import de.hpi.oryxengine.node.AbstractNode;
import de.hpi.oryxengine.processDefinition.ProcessDefinition;

public class AbstractProcessDefinitionImpl implements ProcessDefinition {

	protected ArrayList<AbstractNode> startNodes;
	protected String id;
	
	public void setStartNodes(ArrayList<AbstractNode> nodes) {
		// TODO Auto-generated method stub
		this.startNodes = nodes;
	}

	public ArrayList<AbstractNode> getStartNodes(){
		return startNodes;
	}

	public String getID() {
		return id;
	}

	public void setID(String s) {
		this.id = s;
	}

}
