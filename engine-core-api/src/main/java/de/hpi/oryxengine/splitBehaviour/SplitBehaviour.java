package de.hpi.oryxengine.splitBehaviour;

import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;

public interface SplitBehaviour {
    
    List<ProcessInstance> split(List<ProcessInstance> instances);
}
