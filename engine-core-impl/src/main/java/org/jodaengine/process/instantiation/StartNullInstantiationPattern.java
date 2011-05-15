package org.jodaengine.process.instantiation;

import org.jodaengine.process.instance.AbstractProcessInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the null object for the instantiationPattern. In case the previous pattern created a
 * {@link AbstractProcessInstance processInstance}, this keeps unmodified and is returned.
 */
public class StartNullInstantiationPattern extends AbstractProcessInstantiationPattern implements
StartInstantiationPattern {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void logWarnMessage() {

        String warnMessage = "This is a NullInstantiationPattern that does nothing."
            + "You should be aware of the fact that a processDefinition has defined this pattern"
            + "as one of the instantiationPattern.";
        logger.warn(warnMessage);
    }

    @Override
    public AbstractProcessInstance createProcessInstance(InstantiationPatternContext patternContext) {

        logWarnMessage();
        return nextInstantiationPatternResult(patternContext, null);
    }

    @Override
    protected AbstractProcessInstance createProcessInstanceIntern(InstantiationPatternContext patternContext,
                                                                  AbstractProcessInstance previosProcessInstance) {

        // If a ProcessInstance is already defined by the previous pattern then this one is returned
        if (previosProcessInstance != null) {
            return previosProcessInstance;
        }

        // Otherwise null should be retrieved
        return createProcessInstance(patternContext);
    }
}
