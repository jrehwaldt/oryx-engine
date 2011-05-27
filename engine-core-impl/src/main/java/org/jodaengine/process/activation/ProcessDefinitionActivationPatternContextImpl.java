package org.jodaengine.process.activation;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.jodaengine.process.instantiation.InstantiationPatternContext;
import org.jodaengine.util.ServiceContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of the {@link ProcessDefinitionActivationPatternContext}.
 */
public class ProcessDefinitionActivationPatternContextImpl extends ServiceContextImpl implements
ProcessDefinitionActivationPatternContext {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ProcessDefinitionInside processDefinition;

    /**
     * Default constructor.
     * 
     * @param processDefinition
     *            - the {@link ProcessDefinitionInside processDefinition} that is assigned to this patternContext
     */
    public ProcessDefinitionActivationPatternContextImpl(ProcessDefinitionInside processDefinition) {

        super();
        settingProcessDefinition(processDefinition);
    }

    @Override
    public ProcessDefinitionInside getProcessDefinition() {

        return processDefinition;
    }

    /**
     * Sets the {@link ProcessDefinitionInside processDefinition} in this {@link InstantiationPatternContext
     * patternContext}.
     */
    private void settingProcessDefinition(ProcessDefinitionInside processDefinition) {

        if (processDefinition == null) {
            String errorMessage = "The processDefinition should not be null. Please provide a real processDefinition.";
            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }

        this.processDefinition = processDefinition;
    }
}
