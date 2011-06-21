package org.jodaengine.process.instantiation.pattern;

import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instantiation.InstantiationPatternContext;
import org.jodaengine.process.instantiation.StartProcessInstantiationPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the null object for the instantiationPattern. In case the previous pattern created a
 * {@link AbstractProcessInstance processInstance}, this keeps unmodified and is returned.
 */
public class StartNullInstantiationPattern extends AbstractProcessInstantiationPattern implements
StartProcessInstantiationPattern {

    private final Logger logger = LoggerFactory.getLogger(getClass());

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

    /**
     * Logs a default warn message.
     */
    private void logWarnMessage() {
    
        String warnMessage = "This is a NullInstantiationPattern that does nothing."
            + "You should be aware of the fact that a processDefinition has defined this pattern"
            + "as one of the instantiationPattern.";
        logger.warn(warnMessage);
    }
}
