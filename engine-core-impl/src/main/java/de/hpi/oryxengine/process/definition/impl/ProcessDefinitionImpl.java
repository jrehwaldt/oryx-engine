package de.hpi.oryxengine.process.definition.impl;

import java.util.ArrayList;

import de.hpi.oryxengine.process.definition.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.process.structure.Node;

/**
 * The Class ProcessDefinitionImpl. 
 * TODO wtf does this class do (ProcessDefinitionImpl)
 */
public class ProcessDefinitionImpl
extends AbstractProcessDefinitionImpl {

    /**
     * Instantiates a new process definition impl.
     * 
     * @param startNodes
     *            the start nodes
     */
    public ProcessDefinitionImpl(ArrayList<Node> startNodes) {
        this.setStartNodes(startNodes);
    }
}
