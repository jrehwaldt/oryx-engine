package org.jodaengine.process.instantiation.pattern;

import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.instantiation.InstantiationPatternContext;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;



/**
 * This pattern encapsulates the instantiation semantic for BPMN models that are with an dedicated {@link ProcessStartEvent}.
 * 
 * It also implements the {@link StartInstantiationPattern StartInstantiationPattern-Interface}, so that it can be used
 * as one of the first instantiationPattern.
 */
public class EventBasedInstanceCreationPattern extends AbstractProcessInstantiationPattern implements
StartInstantiationPattern {

    @Override
    public AbstractProcessInstance createProcessInstance(InstantiationPatternContext patternContext) {

        // Extracting the necessary variables from the context
        ProcessDefinitionInside processDefinition = patternContext.getProcessDefinition();
        NavigatorInside navigator = patternContext.getNavigatorService();
        ProcessStartEvent startEvent = patternContext.getThrownStartEvent();

        BpmnTokenBuilder tokenBuilder = new BpmnTokenBuilder(navigator, null);
        AbstractProcessInstance processInstance = new ProcessInstance(processDefinition, tokenBuilder);

        // Extract the startNode
        Node startNode = processDefinition.getStartTriggers().get(startEvent);
        tokenBuilder.setNode(startNode);
        Token newToken = processInstance.createToken();
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
