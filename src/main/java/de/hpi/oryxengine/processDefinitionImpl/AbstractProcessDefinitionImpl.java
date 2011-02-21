package de.hpi.oryxengine.processDefinitionImpl;

import java.util.ArrayList;

import de.hpi.oryxengine.processDefinition.ProcessDefinition;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;

public class AbstractProcessDefinitionImpl implements ProcessDefinition {

  // TODO [Gerardo] Was ist denn hieran abstrakt
  
	protected ArrayList<NodeImpl> startNodes;
	protected String id;
	
	public void setStartNodes(ArrayList<NodeImpl> nodes) {
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
