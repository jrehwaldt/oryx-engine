package org.jodaengine.node.factory.bpmn;

import org.jodaengine.process.activation.pattern.RegisterAllStartEventPattern;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.process.instantiation.pattern.DefaultBpmnProcessInstanceCreationPattern;


/**
 * A static Modifier that is able to modify and to decorate {@link ProcessDefinition processDefinitions}.
 */
public final class BpmnProcessDefinitionModifier {

    /**
     * Decorates the ProcessDefinition with the default instantiationPattern for BPMN.
     * 
     * @param builder
     *            - a {@link ProcessDefinitionBuilder} that builds the {@link ProcessDefinition}
     * @return the modified {@link ProcessDefinitionBuilder}
     */
    public static BpmnProcessDefinitionBuilder decorateWithDefaultBpmnInstantiationPattern(BpmnProcessDefinitionBuilder builder) {

        builder.addStartInstantiationPattern(new DefaultBpmnProcessInstanceCreationPattern()).addDeActivationPattern(new RegisterAllStartEventPattern());
        return builder;
    }

    /**
     * Hidden Constructor.
     */
    private BpmnProcessDefinitionModifier() {

    }
}
