package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.util.PatternAppendable;

public interface StartInstantiationPattern extends InstantionPatternInit, PatternAppendable<ProcessInstantiationPattern> {

//    StartInstantiationPattern init(CorrelationManager correlationManager,
//                                   NavigatorInside navigator,
//                                   ProcessDefinitionInside processDefinition);

    AbstractProcessInstance createProcessInstance();
}
