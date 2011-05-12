package de.hpi.oryxengine.process.instantiation;

import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.process.definition.ProcessDefinitionInside;
import de.hpi.oryxengine.util.ServiceContext;

public interface InstantiationPatternContext extends ServiceContext {

    ProcessDefinitionInside getProcessDefinition();
    
    StartEvent getThrownStartEvent();
}
