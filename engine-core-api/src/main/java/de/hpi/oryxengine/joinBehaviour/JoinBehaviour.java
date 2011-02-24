package de.hpi.oryxengine.joinBehaviour;

import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;

public interface JoinBehaviour {

    List<ProcessInstance> join(ProcessInstance instance);
}