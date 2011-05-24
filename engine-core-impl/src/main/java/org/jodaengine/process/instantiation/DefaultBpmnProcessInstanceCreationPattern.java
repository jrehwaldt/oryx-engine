package org.jodaengine.process.instantiation;

import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.BpmnProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BpmnTokenImpl;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This pattern encapsulates the default instantiation semantic for BPMN models. This
 * {@link InstantiationPattern instantionPattern} can be used when the {@link ProcessDefinitionInside process
 * definition} has a dedicated start node.
 * 
 * It also implements the {@link StartInstantiationPattern StartInstantiationPattern-Interface}, so that it can be used
 * as one of the first instantiationPattern.
 */
public class DefaultBpmnProcessInstanceCreationPattern extends AbstractProcessInstantiationPattern implements
StartInstantiationPattern {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public AbstractProcessInstance<BpmnTokenImpl> createProcessInstance(InstantiationPatternContext patternContext) {

        ProcessDefinitionInside processDefinition = patternContext.getProcessDefinition();
        NavigatorInside navigator = patternContext.getNavigatorService();
        
        AbstractProcessInstance<BpmnTokenImpl> processInstance = new BpmnProcessInstance(processDefinition);
        for (Node node : processDefinition.getStartNodes()) {
            Token newToken = processInstance.createNewToken(node, navigator);
            navigator.addWorkToken(newToken);
        }

        return processInstance;
    }

    @Override
    protected AbstractProcessInstance<?> createProcessInstanceIntern(InstantiationPatternContext patternContext, AbstractProcessInstance<?> previosProcessInstance) {

        if (previosProcessInstance != null) {
            String warnMessage = "The previous pattern already created an ProcessInstance. This one is now overridden.";
            logger.warn(warnMessage);
        }

        // Nevertheless returning the ProcessInstance that would be created originally
        return createProcessInstance(patternContext);
    }
}
