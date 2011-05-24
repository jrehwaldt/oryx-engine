package org.jodaengine.process.activation.pattern;

import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContext;
import org.jodaengine.process.activation.ProcessDefinitionActivatorPattern;

/**
 * This is the null object for the {@link ProcessDefinitionActivatorPattern}. This pattern actually does nothing.
 */
public class NullProcessDefinitionActivationPattern extends AbstractProcessDefinitionActivatorPattern {

    @Override
    protected void activateProcessDefinitionIntern(ProcessDefinitionActivationPatternContext patternContext) {

        logWarnMessage();
        nextProcessDefinitionActivationPatternResult(patternContext);
    }

    @Override
    protected void deactivateProcessDefinitionIntern(ProcessDefinitionActivationPatternContext patternContext) {

        logWarnMessage();
        nextProcessDefinitionActivationPatternResult(patternContext);
    }

    /**
     * Logs a default warn message.
     */
    private void logWarnMessage() {

        StringBuilder warnMessageBuilder = new StringBuilder();
        warnMessageBuilder.append("This is a NullProcessDefinitionActivationPattern that does nothing.")
        .append("You should be aware of the fact that a processDefinition has defined this pattern")
        .append("as one of the processDefinitionActivationPattern.");
        
        logger.warn(warnMessageBuilder.toString());
    }
}
