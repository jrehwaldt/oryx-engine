package de.hpi.oryxengine.processDefinition;

import java.util.ArrayList;

import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.NodeImpl;

public class ProcessDefinitionImpl extends AbstractProcessDefinitionImpl {
    public ProcessDefinitionImpl(ArrayList<Node> startNodes) {

        this.startNodes = startNodes;
    }
}
