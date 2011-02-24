package de.hpi.oryxengine.routingBehaviour;

import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;

public interface RoutingBehaviour {

    List<ProcessInstance> execute(ProcessInstance instance);
}
