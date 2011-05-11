package de.hpi.oryxengine.process.instantiation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.process.definition.ProcessInstantiationPattern;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;

public class StartNullInstantiationPattern extends AbstractProcessInstantiationPattern {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected AbstractProcessInstance createProcessInstanceIntern(ProcessInstantiationPattern previousPattern) {

        // If a ProcessInstance is already defined by the previous pattern then this one is returned
        if (previousPattern != null) {
            return previousPattern.getCurrentProcessInstance();
        }

        // Otherwise Null
        return null;
    }
}
