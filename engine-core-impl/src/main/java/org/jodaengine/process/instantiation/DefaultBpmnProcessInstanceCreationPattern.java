package org.jodaengine.process.instantiation;

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
    public AbstractProcessInstance<BPMNToken> createProcessInstance(InstantiationPatternContext patternContext) {

        ProcessDefinitionInside processDefinition = patternContext.getProcessDefinition();
        NavigatorInside navigator = patternContext.getNavigatorService();
        
        AbstractProcessInstance<BPMNToken> processInstance = new ProcessInstanceImpl<BPMNToken>(processDefinition);
        for (Node node : processDefinition.getStartNodes()) {
            BPMNToken newToken = new BPMNTokenImpl(node, processInstance, navigator);
            processInstance.addToken(newToken);
            navigator.addWorkToken(newToken);
        }

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
