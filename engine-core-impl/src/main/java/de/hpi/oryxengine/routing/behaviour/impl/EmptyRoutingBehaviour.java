package de.hpi.oryxengine.routing.behaviour.impl;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.routing.behaviour.RoutingBehaviour;

/**
 * @deprecated The Class EmptyRoutingBehaviour. Just in for convenience. 
 * Should be refactored subclassing AbstractRoutingBehaviour.
 */
@Deprecated
public class EmptyRoutingBehaviour implements RoutingBehaviour {

    /** 
     * @see de.hpi.oryxengine.routing.behaviour.RoutingBehaviour#
     *      execute(de.hpi.oryxengine.process.instance.ProcessInstance)
     * @param instance the process instance to be executed
     * @return list of process instances
     */
    public List<ProcessInstance> execute(ProcessInstance instance) {

        return new LinkedList<ProcessInstance>();
    }

}
