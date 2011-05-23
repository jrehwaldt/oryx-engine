package org.jodaengine.process.instantiation;

import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BPMNToken;
import org.jodaengine.process.token.BPMNTokenImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * This pattern encapsulates the instantiation semantic for BPMN models that are with an dedicated {@link ProcessStartEvent}.
 * 
 * It also implements the {@link StartInstantiationPattern StartInstantiationPattern-Interface}, so that it can be used
 * as one of the first instantiationPattern.
 */
public class EventBasedInstanceCreationPattern extends AbstractProcessInstantiationPattern implements
StartInstantiationPattern {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public AbstractProcessInstance<BPMNToken> createProcessInstance(InstantiationPatternContext patternContext) {

        // Extracting the necessary variables from the context
        ProcessDefinitionInside processDefinition = patternContext.getProcessDefinition();
        NavigatorInside navigator = patternContext.getNavigatorService();
        ProcessStartEvent startEvent = patternContext.getThrownStartEvent();

        AbstractProcessInstance<BPMNToken> processInstance = new ProcessInstanceImpl<BPMNToken>(processDefinition);

        // Extract the startNode
        Node<BPMNToken> startNode = processDefinition.getStartTriggers().get(startEvent);
        BPMNToken newToken = new BPMNTokenImpl(startNode, processInstance, navigator);
        processInstance.addToken(newToken);
        navigator.addWorkToken(newToken);

        return processInstance;
    }

    @Override
    protected AbstractProcessInstance<BPMNToken> createProcessInstanceIntern(
        InstantiationPatternContext patternContext,
        AbstractProcessInstance<BPMNToken> previosProcessInstance) {

        if (previosProcessInstance != null) {
            String warnMessage = "The previous pattern already created an ProcessInstance. This one is now overridden.";
            logger.warn(warnMessage);
        }

        // Nevertheless returning the ProcessInstance that would be created originally
        return createProcessInstance(patternContext);
    }
}
