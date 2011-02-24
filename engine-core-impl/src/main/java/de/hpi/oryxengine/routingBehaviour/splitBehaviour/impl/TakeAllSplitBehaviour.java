package de.hpi.oryxengine.routingBehaviour.splitBehaviour.impl;

import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.splitBehaviour.SplitBehaviour;

/**
 * The Class TakeAllSplitBehaviour. Will signalize all outgoing transitions.
 */
public class TakeAllSplitBehaviour implements SplitBehaviour {

    /* (non-Javadoc)
     * @see de.hpi.oryxengine.splitBehaviour.SplitBehaviour#split(java.util.List)
     */
    public List<ProcessInstance> split(List<ProcessInstance> instances) {

        if (instances.size() == 0) {
            return instances;
        }
        return instances.get(0).takeAllTransitions();
    }

}
