package de.hpi.oryxengine.routingBehaviour.impl;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

/**
 * @deprecated The Class EmptyRoutingBehaviour. Just in for convenience. 
 * Should be refactored subclassing AbstractRoutingBehaviour.
 */
@Deprecated
public class EmptyRoutingBehaviour implements RoutingBehaviour {

    /** 
     * @see de.hpi.oryxengine.routingBehaviour.RoutingBehaviour#
     *      execute(de.hpi.oryxengine.processInstance.ProcessInstance)
     * @param instance the process instance to be executed
     * @return list of process instances
     */
    public List<ProcessInstance> execute(ProcessInstance instance) {

        return new LinkedList<ProcessInstance>();
    }

}
