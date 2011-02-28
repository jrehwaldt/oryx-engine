package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;

/**
 * A factory for creating simple ProcessInstance objects. So it creates a Process starting at one node spefified.
 */
public class SimpleProcessInstanceFactory {
    
    /**
     * Creates the the simple Process Instance starting at a given node.
     *
     * @param startNode the start node
     * @return the process instance
     */
    public ProcessInstance create(Node startNode) {
        ProcessInstance p = new ProcessInstanceImpl(startNode);
        return p;
    }

}
