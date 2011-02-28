package de.hpi.oryxengine.routing.behaviour.split.impl;

import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.splitBehaviour.SplitBehaviour;

/**
 * The Class TakeAllSplitBehaviour. Will signalize all outgoing transitions.
 */
public class TakeAllSplitBehaviour implements SplitBehaviour {

    /**
     * Split Behaviour.
     * It takes all transitions so a classic AND-Split behaviour.
     *
     * @param instances the instances
     * @return the list
     * @see de.hpi.oryxengine.splitBehaviour.SplitBehaviour#split(java.util.List)
     * TODO Discuss communication issue with thorben
     */
    public List<ProcessInstance> split(List<ProcessInstance> instances) {

        if (instances.size() == 0) {
            return instances;
        }
        return instances.get(0).takeAllTransitions();
    }

}
