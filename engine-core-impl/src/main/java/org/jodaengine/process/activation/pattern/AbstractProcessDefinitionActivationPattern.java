package org.jodaengine.process.activation.pattern;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.activation.ProcessDefinitionActivationPattern;
import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContext;
import org.jodaengine.util.AbstractPatternAppendable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract class for a {@link ProcessDefinitionActivationPattern}. This class provides a method body and basic
 * functionality for all patterns.
 */
public abstract class AbstractProcessDefinitionActivationPattern extends AbstractPatternAppendable<ProcessDefinitionActivationPattern>
implements ProcessDefinitionActivationPattern {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void activateProcessDefinition(ProcessDefinitionActivationPatternContext patternContext) {

        try {

            activateProcessDefinitionIntern(patternContext);

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

        nextProcessDefinitionActivationPatternResult(patternContext);
    }

    /**
     * Encapsulates the logic navigating to the next pattern.
     * 
     * @param patternContext
     *            - a {@link ProcessDefinitionActivationPatternContext activationPatternContext} that provides
     *            information from the pattern invoked before
     */
    protected void nextProcessDefinitionActivationPatternResult(ProcessDefinitionActivationPatternContext patternContext) {

        if (getNextPattern() == null) {
            return;
        }

        getNextPattern().activateProcessDefinition(patternContext);
    }

    /**
     * This abstract method is used for the inherited classes.
     * 
     * @param patternContext
     *            - a {@link ProcessDefinitionActivationPatternContext activationPatternContext} that provides
     *            information from the pattern invoked before
     * @see ProcessDefinitionActivationPattern#activateProcessDefinition(ProcessDefinitionActivationPatternContext);
     */
    protected abstract void activateProcessDefinitionIntern(ProcessDefinitionActivationPatternContext patternContext);
}
