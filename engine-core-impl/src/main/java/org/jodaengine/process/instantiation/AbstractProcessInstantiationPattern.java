package org.jodaengine.process.instantiation;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.instance.AbstractProcessInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * An abstract class for a {@link InstantiationPattern}. This class provides a method body and basic
 * functionality for all
 */
public abstract class AbstractProcessInstantiationPattern implements InstantiationPattern {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private InstantiationPattern nextInstantiationPattern;

    @Override
    public InstantiationPattern setNextPattern(InstantiationPattern nextPattern) {

        this.nextInstantiationPattern = nextPattern;
        return this.nextInstantiationPattern;
    }

    @Override
    public AbstractProcessInstance createProcessInstance(InstantiationPatternContext patternContext, AbstractProcessInstance previosProcessInstance) {

        AbstractProcessInstance currentProcessInstance;
        try {
            currentProcessInstance = createProcessInstanceIntern(patternContext, previosProcessInstance);

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

        return nextInstantiationPatternResult(patternContext, currentProcessInstance);
    }

    /**
     * Encapsulates the logic navigating to the next pattern.
     * 
     * @param currentProcessInstance
     *            - the current {@link AbstractProcessInstance process instance}
     * @return if there is no following instantiationPattern then the current result is returned, otherwise the current
     *         process instance is passed on to the next pattern
     */
    protected AbstractProcessInstance nextInstantiationPatternResult(InstantiationPatternContext patternContext, AbstractProcessInstance currentProcessInstance) {

        if (this.nextInstantiationPattern == null) {
            return currentProcessInstance;
        }

        return this.nextInstantiationPattern.createProcessInstance(patternContext, currentProcessInstance);
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
    protected abstract AbstractProcessInstance createProcessInstanceIntern(InstantiationPatternContext patternContext, AbstractProcessInstance previosProcessInstance);
}
