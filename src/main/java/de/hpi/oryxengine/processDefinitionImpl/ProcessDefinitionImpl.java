package de.hpi.oryxengine.processDefinitionImpl;

import java.util.ArrayList;

import de.hpi.oryxengine.processstructure.impl.NodeImpl;

public class ProcessDefinitionImpl extends AbstractProcessDefinitionImpl {
    public ProcessDefinitionImpl(ArrayList<NodeImpl> startNodes) {

        this.setStartNodes(startNodes);
    }
}
