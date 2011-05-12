package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.util.PatternAppendable;

public interface ProcessInstantiationPattern extends InstantionPatternInit, PatternAppendable<ProcessInstantiationPattern> {

//    ProcessInstantiationPattern init(CorrelationManager correlationManager,
//                                     NavigatorInside navigator,
//                                     ProcessDefinitionInside processDefinition);

    AbstractProcessInstance createProcessInstance(AbstractProcessInstance previosProcessInstance);
}
