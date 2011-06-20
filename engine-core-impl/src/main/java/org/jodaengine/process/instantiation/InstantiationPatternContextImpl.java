package org.jodaengine.process.instantiation;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.processevent.incoming.IncomingStartProcessEvent;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.jodaengine.util.ServiceContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Implementation of the {@link ServiceContextImpl ServiceContext-Interface}.
 */
public class InstantiationPatternContextImpl extends ServiceContextImpl implements InstantiationPatternContext {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IncomingStartProcessEvent startEvent;
    private ProcessDefinitionInside processDefinition;

    /**
     * Default constructor.
     * 
     * In order to have a context without an {@link IncomingStartProcessEvent}
     * 
     * @param processDefinition
     *            - the {@link ProcessDefinitionInside processDefinition} that is assigned to this patternContext
     */
    public InstantiationPatternContextImpl(@Nonnull ProcessDefinitionInside processDefinition) {

        super();
        settingProcessDefinition(processDefinition);
    }

    /**
     * An extended Constructor in case a {@link IncomingStartProcessEvent} was thrown.
     * 
     * @param processDefinition
     *            - the {@link ProcessDefinitionInside processDefinition} that is assigned to this patternContext
     * @param startEvent
     *            - the thrown {@link IncomingStartProcessEvent}
     */
    public InstantiationPatternContextImpl(ProcessDefinitionInside processDefinition, IncomingStartProcessEvent startEvent) {

        this(processDefinition);
        this.startEvent = startEvent;
    }

    @Override
    public IncomingStartProcessEvent getThrownStartEvent() {

        return this.startEvent;
    }

    @Override
    public ProcessDefinitionInside getProcessDefinition() {

        return this.processDefinition;
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
