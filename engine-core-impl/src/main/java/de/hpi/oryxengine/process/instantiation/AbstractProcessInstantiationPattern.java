package de.hpi.oryxengine.process.instantiation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.exception.JodaEngineRuntimeException;
import de.hpi.oryxengine.navigator.NavigatorInside;
import de.hpi.oryxengine.process.definition.ProcessDefinitionInside;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;

/**
 * An abstract class for a {@link InstantiationPattern}. This class provides a method body and basic
 * functionality for all
 */
public abstract class AbstractProcessInstantiationPattern implements InstantiationPattern {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected CorrelationManager correlationManager;
    protected NavigatorInside navigator;
    protected ProcessDefinitionInside processDefinition;

    private InstantiationPattern nextInstantiationPattern;

    @Override
    public InstantionPatternInit init(CorrelationManager correlationManager,
                                      NavigatorInside navigator,
                                      ProcessDefinitionInside processDefinition) {

        this.correlationManager = correlationManager;
        this.navigator = navigator;
        this.processDefinition = processDefinition;
        return this;
    }

    @Override
    public InstantiationPattern setNextPattern(InstantiationPattern nextPattern) {

        this.nextInstantiationPattern = nextPattern;
        return this.nextInstantiationPattern;
    }

    @Override
    public AbstractProcessInstance createProcessInstance(AbstractProcessInstance previosProcessInstance) {

        AbstractProcessInstance currentProcessInstance;
        try {
            currentProcessInstance = createProcessInstanceIntern(previosProcessInstance);

        } catch (NullPointerException nullPointerException) {

            String errorMessage = "A NullpointerException was thrown. "
                + "Probably the previous InstantiationPattern did not create a ProcessInstance.";
            logger.error(errorMessage, nullPointerException);
            throw new JodaEngineRuntimeException(errorMessage, nullPointerException);

        } catch (Exception anyException) {

            String errorMessage = "An Error occurred.";
            logger.error(errorMessage, anyException);
            throw new JodaEngineRuntimeException(errorMessage, anyException);

        }

        return nextInstantiationPatternResult(currentProcessInstance);
    }

    /**
     * Encapsulates the logic navigating to the next pattern.
     * 
     * @param currentProcessInstance
     *            - the current {@link AbstractProcessInstance process instance}
     * @return if there is no following instantiationPattern then the current result is returned, otherwise the current
     *         process instance is passed on to the next pattern
     */
    protected AbstractProcessInstance nextInstantiationPatternResult(AbstractProcessInstance currentProcessInstance) {

        if (this.nextInstantiationPattern == null) {
            return currentProcessInstance;
        }

        this.nextInstantiationPattern.init(this.correlationManager, this.navigator, this.processDefinition);
        return this.nextInstantiationPattern.createProcessInstance(currentProcessInstance);
    }

    /**
     * This abstract method is used for the inherited classes.
     * 
     * @see ProcessInstantiationPattern#createProcessInstance(AbstractProcessInstance);
     * @param previosProcessInstance
     *            - the {@link AbstractProcessInstance processInstances} from the previous
     *            {@link InstantiationPattern patterns}.
     * 
     * @return an {@link AbstractProcessInstance}
     */
    protected abstract AbstractProcessInstance createProcessInstanceIntern(AbstractProcessInstance previosProcessInstance);
}
