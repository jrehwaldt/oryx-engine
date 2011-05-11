package de.hpi.oryxengine.process.instantiation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.exception.JodaEngineRuntimeException;
import de.hpi.oryxengine.navigator.NavigatorInside;
import de.hpi.oryxengine.process.definition.ProcessDefinitionInside;
import de.hpi.oryxengine.process.definition.ProcessInstantiationPattern;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;

public abstract class AbstractProcessInstantiationPattern implements ProcessInstantiationPattern {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected CorrelationManager correlationManager;
    protected NavigatorInside navigator;
    protected ProcessDefinitionInside processDefinition;

    private AbstractProcessInstance currentProcessInstance;

    private ProcessInstantiationPattern nextInstantiationPattern;

    @Override
    public ProcessInstantiationPattern init(CorrelationManager correlationManager,
                                            NavigatorInside navigator,
                                            ProcessDefinitionInside processDefinition) {

        this.correlationManager = correlationManager;
        this.navigator = navigator;
        this.processDefinition = processDefinition;
        return this;
    }

    @Override
    public AbstractProcessInstance createProcessInstance(ProcessInstantiationPattern previousPattern) {

        try {
            // Because it can be used in the next
            currentProcessInstance = previousPattern.getCurrentProcessInstance();
            currentProcessInstance = createProcessInstanceIntern(previousPattern);
        } catch (NullPointerException nullPointerException) {
            String errorMessage = "A NullpointerException was thrown. "
                + "Probably the previous InstantiationPattern did not create a ProcessInstance.";
            logger.error(errorMessage, nullPointerException);
            throw new JodaEngineRuntimeException(errorMessage, nullPointerException);
        }

        return nextInstanciationPatternResult(previousPattern);
    }

    protected AbstractProcessInstance nextInstanciationPatternResult(ProcessInstantiationPattern previousPattern) {

        if (this.nextInstantiationPattern == null) {
            return previousPattern.getCurrentProcessInstance();
        }

        this.nextInstantiationPattern.init(this.correlationManager, this.navigator, this.processDefinition);
        return this.nextInstantiationPattern.createProcessInstance(this);
    }

    protected abstract AbstractProcessInstance createProcessInstanceIntern(ProcessInstantiationPattern previousPattern);

    @Override
    public AbstractProcessInstance getCurrentProcessInstance() {

        return currentProcessInstance;
    }

    @Override
    public ProcessInstantiationPattern setNextPattern(ProcessInstantiationPattern nextPattern) {

        this.nextInstantiationPattern = nextPattern;
        return this;
    }
}
