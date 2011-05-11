package de.hpi.oryxengine.process.definition;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.navigator.NavigatorInside;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.util.PatternAppendable;

public interface ProcessInstantiationPattern extends PatternAppendable<ProcessInstantiationPattern> {

    ProcessInstantiationPattern init(CorrelationManager correlationManager,
                                     NavigatorInside navigator,
                                     ProcessDefinitionInside processDefinition);

    AbstractProcessInstance createProcessInstance(ProcessInstantiationPattern previousPattern);
    
    @Nonnull AbstractProcessInstance getCurrentProcessInstance();
}
