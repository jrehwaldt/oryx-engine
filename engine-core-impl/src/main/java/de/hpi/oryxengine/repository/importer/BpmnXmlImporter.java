package de.hpi.oryxengine.repository.importer;

import java.io.InputStream;

import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.repository.ProcessDefinitionImporter;
import de.hpi.oryxengine.repository.importer.bpmn.BpmnXmlParse;
import de.hpi.oryxengine.repository.importer.bpmn.BpmnXmlParser;


/**
 * It is capable of importing an XML file that represents a BPMN process.
 * 
 * In order to parse through the XML we use some classes of the Activiti Project (activiti.org).
 */
public class BpmnXmlImporter implements ProcessDefinitionImporter {

    InputStream xmlInputStream;
    
    public BpmnXmlImporter(InputStream inputStream) {

        this.xmlInputStream = inputStream;
    }
    
    @Override
    public ProcessDefinition createProcessDefinition() {

        BpmnXmlParser bpmnParser = new BpmnXmlParser();
        BpmnXmlParse bpmnXmlParse = bpmnParser.createParse();
        bpmnXmlParse.sourceInputStream(xmlInputStream);
        
        bpmnXmlParse.execute();        

        return null;
    }

}
