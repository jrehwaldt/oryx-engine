package de.hpi.oryxengine.process.instantiation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.navigator.NavigatorInside;
import de.hpi.oryxengine.process.definition.ProcessDefinitionInside;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;

/**
 * This is the null object for the instantiationPattern. In case the previous pattern created a
 * {@link AbstractProcessInstance processInstance}, this keeps unmodified and is returned.
 */
public class StartNullInstantiationPattern extends AbstractProcessInstantiationPattern implements
StartInstantiationPattern {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public InstantionPatternInit init(CorrelationManager correlationManager,
                                      NavigatorInside navigator,
                                      ProcessDefinitionInside processDefinition) {

        logWarnMessage();
        return super.init(correlationManager, navigator, processDefinition);
    }

    public void logWarnMessage() {

        String warnMessage = "This is a NullInstantiationPattern that does nothing."
            + "You should be aware of the fact that a processDefinition has defined this pattern"
            + "as one of the instantiationPattern.";
        logger.warn(warnMessage);
    }

    @Override
    public AbstractProcessInstance createProcessInstance() {

        logWarnMessage();
        return nextInstantiationPatternResult(null);
    }

    @Override
    protected AbstractProcessInstance createProcessInstanceIntern(AbstractProcessInstance previosProcessInstance) {

        // If a ProcessInstance is already defined by the previous pattern then this one is returned
        if (previosProcessInstance != null) {
            return previosProcessInstance;
        }

        // Otherwise null should be retrieved
        return createProcessInstance();
    }
}
