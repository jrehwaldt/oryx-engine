package de.hpi.oryxengine.process.instantiation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.navigator.NavigatorInside;
import de.hpi.oryxengine.process.definition.InstantionPatternInit;
import de.hpi.oryxengine.process.definition.ProcessDefinitionInside;
import de.hpi.oryxengine.process.definition.ProcessInstantiationPattern;
import de.hpi.oryxengine.process.definition.StartInstantiationPattern;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;

public class StartNullInstantiationPattern implements ProcessInstantiationPattern, StartInstantiationPattern {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public InstantionPatternInit init(CorrelationManager correlationManager,
                                      NavigatorInside navigator,
                                      ProcessDefinitionInside processDefinition) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProcessInstantiationPattern setNextPattern(ProcessInstantiationPattern nextPattern) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractProcessInstance createProcessInstance() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractProcessInstance createProcessInstance(AbstractProcessInstance previosProcessInstance) {

        // TODO Auto-generated method stub
        return null;
    }

    

//    @Override
//    protected AbstractProcessInstance createProcessInstanceIntern(InstatiationPatternInit previousPattern) {
//
//        // If a ProcessInstance is already defined by the previous pattern then this one is returned
//        if (previousPattern != null) {
//            return previousPattern.getCurrentProcessInstance();
//        }
//
//        // Otherwise Null
//        return null;
//    }
}
