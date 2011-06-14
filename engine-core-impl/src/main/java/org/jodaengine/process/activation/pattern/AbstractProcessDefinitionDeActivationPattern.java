package org.jodaengine.process.activation.pattern;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContext;
import org.jodaengine.process.activation.ProcessDeActivationPattern;
import org.jodaengine.util.AbstractPatternAppendable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract class for a {@link ProcessDefinitionActivationPattern} and for a
 * {@link ProcessDefinitionDeactivationPattern}. This class provides a method body and basic functionality for all
 * patterns.
 */
public abstract class AbstractProcessDefinitionDeActivationPattern extends AbstractPatternAppendable<ProcessDeActivationPattern>
implements ProcessDeActivationPattern {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void activateProcessDefinition(ProcessDefinitionActivationPatternContext patternContext) {

        try {

            activateProcessDefinitionIntern(patternContext);

        } catch (NullPointerException nullPointerException) {

            handleNullPointer(patternContext, nullPointerException, true);

        } catch (Exception anyException) {

            handleAnyException(patternContext, anyException, true);
        }

        nextProcessDefinitionActivationPatternResult(patternContext);
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

    @Override
    public void deactivateProcessDefinition(ProcessDefinitionActivationPatternContext patternContext) {

        try {

            deactivateProcessDefinitionIntern(patternContext);

        } catch (NullPointerException nullPointerException) {

            handleNullPointer(patternContext, nullPointerException, false);

        } catch (Exception anyException) {

            handleAnyException(patternContext, anyException, false);
        }

        nextProcessDefinitionActivationPatternResult(patternContext);

    }

    /**
     * This abstract method is used for the inherited classes.
     * 
     * @param patternContext
     *            - a {@link ProcessDefinitionActivationPatternContext activationPatternContext} that provides
     *            information from the pattern invoked before
     * @see ProcessDefinitionActivationPattern#activateProcessDefinition(ProcessDefinitionActivationPatternContext);
     */
    protected abstract void deactivateProcessDefinitionIntern(ProcessDefinitionActivationPatternContext patternContext);

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
     * Handles an {@link Exception}.
     * 
     * @param patternContext
     *            - the pattern context
     * @param anyException
     *            - the thrown {@link Exception}
     * @param forActivationPattern
     *            - defines whether the exception occurred while activating or deactivating the processDefinition
     */
    private void handleAnyException(ProcessDefinitionActivationPatternContext patternContext,
                                    Exception anyException,
                                    boolean forActivationPattern) {

        StringBuilder errorMessageBuilder = new StringBuilder();
        errorMessageBuilder.append("A Error was thrown in the ProcessDefinitionActivatorPattern '");
        errorMessageBuilder.append(getClass().getName()).append("'. ");

        errorMessageBuilder.append("This happens while ");
        if (forActivationPattern) {
            errorMessageBuilder.append("activating");
        } else {
            errorMessageBuilder.append("deactivating");
        }
        errorMessageBuilder.append("the ProcessDefinition '");
        errorMessageBuilder.append(patternContext.getProcessDefinition().getName()).append("'.");

        logger.error(errorMessageBuilder.toString(), anyException);
        throw new JodaEngineRuntimeException(errorMessageBuilder.toString(), anyException);
    }

    /**
     * Handles a {@link NullPointerException}.
     * 
     * @param patternContext
     *            - the pattern context
     * @param nullPointerException
     *            - the thrown {@link NullPointerException}
     * @param forActivationPattern
     *            - defines whether the exception occurred while activating or deactivating the processDefinition
     */
    private void handleNullPointer(ProcessDefinitionActivationPatternContext patternContext,
                                   NullPointerException nullPointerException,
                                   boolean forActivationPattern) {

        StringBuilder errorMessageBuilder = new StringBuilder();
        errorMessageBuilder.append("A NullpointerException was thrown in the ProcessDefinitionActivatorPattern '");
        errorMessageBuilder.append(getClass().getName()).append("'. ");

        errorMessageBuilder.append("This happens while ");
        if (forActivationPattern) {
            errorMessageBuilder.append("activating ");
        } else {
            errorMessageBuilder.append("deactivating ");
        }
        errorMessageBuilder.append("the ProcessDefinition '");
        errorMessageBuilder.append(patternContext.getProcessDefinition().getName()).append("'.");

        logger.error(errorMessageBuilder.toString(), nullPointerException);
        throw new JodaEngineRuntimeException(errorMessageBuilder.toString(), nullPointerException);
    }
}
