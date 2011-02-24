package de.hpi.oryxengine.routingBehaviour.impl;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

/**
 * @deprecated
 * The Class EmptyRoutingBehaviour. Just in for convenience. Should be refactored subclassing AbstractRoutingBehaviour.
 */
public class EmptyRoutingBehaviour implements RoutingBehaviour {

    /* (non-Javadoc)
     * @see de.hpi.oryxengine.routingBehaviour.RoutingBehaviour#execute(de.hpi.oryxengine.processInstance.ProcessInstance)
     */
    public List<ProcessInstance> execute(ProcessInstance instance) {

        return new LinkedList<ProcessInstance>();
    }

}
