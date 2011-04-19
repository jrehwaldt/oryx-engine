package de.hpi.oryxengine.deployment.importer;

import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * Abstract class for importing BPMN serialized XMl.
 * 
 * It contains the name of the {@link ProcessDefinition} that will be set in case no other name was defined.
 */
public abstract class AbstractBpmnXmlImporter implements ProcessDefinitionImporter {

    protected String processDefinitionName;
    
    protected AbstractBpmnXmlImporter(String processDefinitionName) {
        
        this.processDefinitionName = processDefinitionName;
    }
    
    @Override
    public abstract ProcessDefinition createProcessDefinition();
}
