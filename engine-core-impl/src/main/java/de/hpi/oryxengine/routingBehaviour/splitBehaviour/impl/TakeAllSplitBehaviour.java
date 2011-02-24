package de.hpi.oryxengine.routingBehaviour.splitBehaviour.impl;

import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.splitBehaviour.SplitBehaviour;

public class TakeAllSplitBehaviour implements SplitBehaviour {

    public List<ProcessInstance> split(List<ProcessInstance> instances) {

        if(instances.size() == 0) {
            return instances;
        }
        return instances.get(0).takeAllTransitions();
    }

}
