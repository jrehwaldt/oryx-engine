package de.hpi.oryxengine.node.factory.bpmn;

import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.instantiation.DefaultBpmnInstantiationPattern;

public class BpmnProcessDefinitionModifier {

    private BpmnProcessDefinitionModifier() {

    }
    
    public static ProcessDefinitionBuilder decorateWithNormalBpmnProcessInstantiation(ProcessDefinitionBuilder builder) {
        
        builder.addInstanciationPattern(new DefaultBpmnInstantiationPattern());
        return builder;
    }
}
