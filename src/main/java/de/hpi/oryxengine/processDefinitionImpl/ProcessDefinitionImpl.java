package de.hpi.oryxengine.processDefinitionImpl;

import java.util.ArrayList;

import de.hpi.oryxengine.node.AbstractNode;

public class ProcessDefinitionImpl extends AbstractProcessDefinitionImpl {
	public ProcessDefinitionImpl(ArrayList<AbstractNode> startNodes){
		this.startNodes = startNodes;
	}
}
