package org.jodaengine.process.activation.pattern;

import org.jodaengine.process.activation.ProcessDefinitionActivationPattern;
import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContext;

/**
 * This is the null object for the {@link ProcessDefinitionActivationPattern}. This pattern actually does nothing.
 */
public class NullProcessDefinitionActivationPattern extends AbstractProcessDefinitionActivationPattern {

    @Override
    protected void activateProcessDefinitionIntern(ProcessDefinitionActivationPatternContext patternContext) {

        logWarnMessage();
        nextProcessDefinitionActivationPatternResult(patternContext);
    }

    /**
     * Logs a default warn message.
     */
    private void logWarnMessage() {

        String warnMessage = "This is a NullProcessDefinitionActivationPattern that does nothing."
            + "You should be aware of the fact that a processDefinition has defined this pattern"
            + "as one of the processDefinitionActivationPattern.";
        logger.warn(warnMessage);
    }
}
