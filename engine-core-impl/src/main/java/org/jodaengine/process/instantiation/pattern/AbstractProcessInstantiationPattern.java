package org.jodaengine.process.instantiation.pattern;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instantiation.ProcessInstantiationPattern;
import org.jodaengine.process.instantiation.InstantiationPatternContext;
import org.jodaengine.util.AbstractPatternAppendable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract class for a {@link ProcessInstantiationPattern}. This class provides a method body and basic
 * functionality for all patterns.
 */
public abstract class AbstractProcessInstantiationPattern extends AbstractPatternAppendable<ProcessInstantiationPattern> implements ProcessInstantiationPattern {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public AbstractProcessInstance createProcessInstance(InstantiationPatternContext patternContext,
                                                         AbstractProcessInstance previosProcessInstance) {

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
     * @param patternContext
     *            - a {@link InstantiationPatternContext} that provides information from the pattern invoked before
     * @param currentProcessInstance
     *            - the current {@link AbstractProcessInstance processInstance}
     * @return if there is no following instantiationPattern then the current result is returned, otherwise the current
     *         process instance is passed on to the next pattern
     */
    protected AbstractProcessInstance nextInstantiationPatternResult(InstantiationPatternContext patternContext,
                                                                     AbstractProcessInstance currentProcessInstance) {

        if (getNextPattern() == null) {
            return currentProcessInstance;
        }

        return getNextPattern().createProcessInstance(patternContext, currentProcessInstance);
    }

    /**
     * This abstract method is used for the inherited classes.
     * 
     * @param patternContext
     *            - a {@link InstantiationPatternContext} that provides information from the pattern invoked before
     * @param previosProcessInstance
     *            - the {@link AbstractProcessInstance processInstances} from the previous {@link ProcessInstantiationPattern
     *            patterns}.
     * @return an {@link AbstractProcessInstance}
     * @see ProcessInstantiationPattern#createProcessInstance(AbstractProcessInstance);
     */
    protected abstract AbstractProcessInstance createProcessInstanceIntern(InstantiationPatternContext patternContext,
                                                                           AbstractProcessInstance previosProcessInstance);
}
