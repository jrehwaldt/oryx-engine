package de.hpi.oryxengine.process.instantiation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.process.definition.ProcessInstantiationPattern;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;

public class DefaultBpmnInstantiationPattern extends AbstractProcessInstantiationPattern {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected AbstractProcessInstance createProcessInstanceIntern(ProcessInstantiationPattern previousPattern) {

        if (previousPattern.getCurrentProcessInstance() != null) {
            String warnMessage = "The previous pattern already created an ProcessInstance. This one is now overridden.";
            logger.warn(warnMessage);
        }

        AbstractProcessInstance processInstance = new ProcessInstanceImpl(processDefinition);

        for (Node node : processDefinition.getStartNodes()) {
            Token newToken = processInstance.createToken(node, navigator);
            navigator.addWorkToken(newToken);
        }

        return processInstance;
    }
}
