package de.hpi.oryxengine.routingBehaviour;

import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;

public abstract class AbstractRoutingBehaviour implements RoutingBehaviour {

    protected JoinBehaviour joinBehaviour;
    protected SplitBehaviour splitBehaviour;

    public List<ProcessInstance> execute(ProcessInstance instance) {

        List<ProcessInstance> joinedInstances = joinBehaviour.join(instance);
        return splitBehaviour.split(joinedInstances);
    }
}
