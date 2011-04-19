package de.hpi.oryxengine.deployment.importer;

import java.io.InputStream;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.repository.importer.bpmn.BpmnXmlParser;
import de.hpi.oryxengine.util.xml.XmlParseable;

/**
 * It is capable of importing an XML file that represents a BPMN process.
 * 
 * In order to parse through the XML we use some classes of the Activiti Project (activiti.org).
 */
public class BpmnXmlInpustreamImporter extends AbstractBpmnXmlImporter {

    private InputStream xmlInputStream;

    public BpmnXmlInpustreamImporter(String processDefintionName, @Nonnull InputStream inputStream) {

        super(processDefintionName);
        this.xmlInputStream = inputStream;
    }

    public BpmnXmlInpustreamImporter(@Nonnull InputStream inputStream) {

        super(null);
        this.xmlInputStream = inputStream;
    }

    @Override
    public ProcessDefinition createProcessDefinition() {

        BpmnXmlParser bpmnXmlParser = new BpmnXmlParser();
        XmlParseable bpmnXmlParse = bpmnXmlParser.getXmlParseBuilder()
                                                 .defineSourceAsInputStream(xmlInputStream)
                                                 .buildXmlParse();

        bpmnXmlParse.execute();

        ProcessDefinition processDefinition = bpmnXmlParse.getFinishedProcessDefinition();

        return processDefinition;
    }

}
