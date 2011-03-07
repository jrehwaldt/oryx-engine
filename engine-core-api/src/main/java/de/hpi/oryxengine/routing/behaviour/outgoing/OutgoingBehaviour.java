package de.hpi.oryxengine.routing.behaviour.outgoing;

import java.util.List;

import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Interface SplitBehaviour.
 */
public interface OutgoingBehaviour {

    /**
     * Split.
     * 
     * @param instances
     *            the instances to split/distribute according to outgoing transitions.
     * @return the list of new process instances that point to the destination-nodes of the outgoing transitions.
     */
    List<ProcessInstance> split(List<ProcessInstance> instances);
}
