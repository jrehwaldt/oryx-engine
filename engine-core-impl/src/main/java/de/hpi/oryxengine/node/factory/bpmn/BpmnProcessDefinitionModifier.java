package de.hpi.oryxengine.node.factory.bpmn;

import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.instantiation.DefaultBpmnProcessInstanceCreationPattern;

public class BpmnProcessDefinitionModifier {

    public static ProcessDefinitionBuilder decorateWithNormalBpmnProcessInstantiation(ProcessDefinitionBuilder builder) {
        
        builder.addStartInstantiationPattern(new DefaultBpmnProcessInstanceCreationPattern());
        return builder;
    }

    /**
     * Hidden Constructor.
     */
    private BpmnProcessDefinitionModifier() {
    
    }
}
