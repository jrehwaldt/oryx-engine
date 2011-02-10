package de.hpi.oryxengine.processDefinitionImpl;

import java.util.ArrayList;

import de.hpi.oryxengine.node.NodeImpl;

public class ProcessDefinitionImpl extends AbstractProcessDefinitionImpl {
	public ProcessDefinitionImpl(ArrayList<NodeImpl> startNodes){
		this.startNodes = startNodes;
	}
}
