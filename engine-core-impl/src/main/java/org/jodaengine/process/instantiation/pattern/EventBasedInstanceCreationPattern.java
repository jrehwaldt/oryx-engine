package org.jodaengine.process.instantiation.pattern;

import org.jodaengine.eventmanagement.processevent.incoming.StartProcessEvent;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.instantiation.InstantiationPatternContext;
import org.jodaengine.process.instantiation.StartProcessInstantiationPattern;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;

/**
 * This pattern encapsulates the instantiation semantic for BPMN models that are with an
 * dedicated {@link StartProcessEvent}.
 * 
 * It also implements the {@link StartProcessInstantiationPattern StartInstantiationPattern-Interface}, so that it can be used
 * as one of the first instantiationPattern.
 */
public class EventBasedInstanceCreationPattern extends AbstractProcessInstantiationPattern implements
StartProcessInstantiationPattern {

    @Override
    public AbstractProcessInstance createProcessInstance(InstantiationPatternContext patternContext) {

        // Extracting the necessary variables from the context
        ProcessDefinitionInside processDefinition = patternContext.getProcessDefinition();
        NavigatorInside navigator = patternContext.getNavigatorService();
        ExtensionService extensions = patternContext.getExtensionService();
        StartProcessEvent startEvent = patternContext.getThrownStartEvent();

        if (startEvent == null) {
            String errorMessage = "This pattern requires that a start event was thrown, but it is null. "
                + "Please do not try to create that processInstance through the navigator. "
                + "Leave the work to the eventManager";
            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }

        BpmnTokenBuilder tokenBuilder = new BpmnTokenBuilder(navigator, extensions);
        AbstractProcessInstance processInstance = new ProcessInstance(processDefinition, tokenBuilder);

        // Extract the startNode
        Node startNode = processDefinition.getStartTriggers().get(startEvent);
        Token newToken = processInstance.createToken(startNode);
        navigator.addWorkToken(newToken);

        return processInstance;
    }

    @Override
    protected AbstractProcessInstance createProcessInstanceIntern(InstantiationPatternContext patternContext,
                                                                  AbstractProcessInstance previosProcessInstance) {

        if (previosProcessInstance != null) {
            String warnMessage = "The previous pattern already created an ProcessInstance. This one is now overridden.";
            logger.warn(warnMessage);
        }

        // Nevertheless returning the ProcessInstance that would be created originally
        return createProcessInstance(patternContext);
    }
}
