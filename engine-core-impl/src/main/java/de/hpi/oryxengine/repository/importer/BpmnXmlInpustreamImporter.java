package de.hpi.oryxengine.repository.importer;

import java.io.InputStream;

import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.repository.importer.bpmn.BpmnXmlParse;
import de.hpi.oryxengine.repository.importer.bpmn.BpmnXmlParser;


/**
 * It is capable of importing an XML file that represents a BPMN process.
 * 
 * In order to parse through the XML we use some classes of the Activiti Project (activiti.org).
 */
public class BpmnXmlInpustreamImporter extends AbstractBpmnXmlImporter {

    InputStream xmlInputStream;
    
    public BpmnXmlInpustreamImporter(String processDefintionName, InputStream inputStream) {
        
        super(processDefintionName);
        this.xmlInputStream = inputStream;
    }

    public BpmnXmlInpustreamImporter(InputStream inputStream) {

        super(null);
        this.xmlInputStream = inputStream;
    }
    
    @Override
    public ProcessDefinition createProcessDefinition() {

        BpmnXmlParser bpmnXmlParser = new BpmnXmlParser();
        BpmnXmlParse bpmnXmlParse = bpmnXmlParser.createParse();
        bpmnXmlParse.name(processDefinitionName);
        bpmnXmlParse.sourceInputStream(xmlInputStream);
        
        bpmnXmlParse.execute();
        
        ProcessDefinition processDefinition = bpmnXmlParse.getFinishedProcessDefinition();
        
        return processDefinition;
    }

}
