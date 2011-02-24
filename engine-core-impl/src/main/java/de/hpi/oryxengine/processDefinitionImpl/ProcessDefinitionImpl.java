package de.hpi.oryxengine.processDefinitionImpl;

import java.util.ArrayList;

import de.hpi.oryxengine.processDefinition.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.processstructure.Node;

public class ProcessDefinitionImpl extends AbstractProcessDefinitionImpl {
    public ProcessDefinitionImpl(ArrayList<Node> startNodes) {

        this.setStartNodes(startNodes);
    }
}
