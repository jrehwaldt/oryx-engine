package de.hpi.oryxengine.process.instantiation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;

/**
 * This pattern encapsulates the default instantiation semantic for BPMN models. This
 * {@link ProcessInstantiationPattern instantionPattern} can be used when the {@link ProcessDefinition process
 * definition} has a dedicated start node.
 * 
 * It also implements the {@link StartInstantiationPattern StartInstantiationPattern-Interface}, so that it can be used
 * as one of the first instantiationPattern.
 */
public class DefaultBpmnProcessInstanceCreationPattern extends AbstractProcessInstantiationPattern implements
StartInstantiationPattern {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public AbstractProcessInstance createProcessInstance() {

        AbstractProcessInstance processInstance = new ProcessInstanceImpl(processDefinition);
        for (Node node : processDefinition.getStartNodes()) {
            Token newToken = processInstance.createToken(node, navigator);
            navigator.addWorkToken(newToken);
        }

        return processInstance;
    }

    @Override
    protected AbstractProcessInstance createProcessInstanceIntern(AbstractProcessInstance previosProcessInstance) {

        if (previosProcessInstance != null) {
            String warnMessage = "The previous pattern already created an ProcessInstance. This one is now overridden.";
            logger.warn(warnMessage);
        }

        // Nevertheless returning the ProcessInstance that would be created originally
        return createProcessInstance();
    }
}
