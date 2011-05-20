package org.jodaengine.deployment.importer.definition;

import java.io.InputStream;
import java.util.Arrays;

import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParser;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.util.io.FileStreamSource;
import org.jodaengine.util.xml.XmlParseable;


/**
 * It is capable of importing an XML file that represents a BPMN process.
 * 
 * In order to parse through the XML we use some classes of the Activiti Project (activiti.org).
 * Those are represented accordingly and mark with a license header.
 */
public class BpmnXmlImporter implements ProcessDefinitionImporter {


    private XmlParseable bpmnXmlParse;
    
    /**
     * Instantiates a new bpmn xml importer using a xml string representation as source.
     *
     * @param xmlString the xml string
     * @param listeners any number of listeners
     */
    public BpmnXmlImporter(String xmlString,
                           BpmnXmlParseListener ... listeners) {
        
        BpmnXmlParser bpmnXmlParser = new BpmnXmlParser();
        bpmnXmlParser.getParseListeners().addAll(Arrays.asList(listeners));
        bpmnXmlParse = bpmnXmlParser.getXmlParseBuilder()
                                    .defineSourceAsString(xmlString)
                                    .buildXmlParse();
    }
    
    /**
     * Instantiates a new bpmn xml importer using a file stream as the source.
     *
     * @param fileStreamSource the file stream source
     * @param listeners any number of listeners
     */
    public BpmnXmlImporter(FileStreamSource fileStreamSource,
                           BpmnXmlParseListener ... listeners) {
        
        BpmnXmlParser bpmnXmlParser = new BpmnXmlParser();
        bpmnXmlParser.getParseListeners().addAll(Arrays.asList(listeners));
        bpmnXmlParse = bpmnXmlParser.getXmlParseBuilder()
                                    .defineSourceAsStreamSource(fileStreamSource)
                                    .buildXmlParse();
    }
    
    /**
     * Instantiates a new bpmn xml importer using an input stream as the source.
     *
     * @param inputStream the input stream
     * @param listeners any number of listeners
     */
    public BpmnXmlImporter(InputStream inputStream,
                           BpmnXmlParseListener ... listeners) {
        
        BpmnXmlParser bpmnXmlParser = new BpmnXmlParser();
        bpmnXmlParser.getParseListeners().addAll(Arrays.asList(listeners));
        
        bpmnXmlParse = bpmnXmlParser.getXmlParseBuilder()
                                    .defineSourceAsInputStream(inputStream)
                                    .buildXmlParse();
    }
    
    @Override
    public ProcessDefinition createProcessDefinition() {
        bpmnXmlParse.execute();
        ProcessDefinition processDefinition = bpmnXmlParse.getFinishedProcessDefinition();
        return processDefinition;
    }
}
