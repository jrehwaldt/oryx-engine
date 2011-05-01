package de.hpi.oryxengine.deployment.importer;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.deployment.importer.bpmn.BpmnXmlParser;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.util.io.FileStreamSource;
import de.hpi.oryxengine.util.xml.XmlParseable;

/**
 * It is capable of importing an XML file that represents a BPMN process.
 * 
 * In order to parse through the XML we use some classes of the Activiti Project (activiti.org).
 */
public class BpmnXmlFileImporter extends AbstractBpmnXmlImporter {

    private String filePath;

    public BpmnXmlFileImporter(String processDefintionName, @Nonnull String filePath) {

        super(processDefintionName);
        this.filePath = filePath;
    }

    public BpmnXmlFileImporter(@Nonnull String filePath) {

        super(null);
        this.filePath = filePath;
    }

    @Override
    public ProcessDefinition createProcessDefinition() {

        BpmnXmlParser bpmnXmlParser = new BpmnXmlParser();
        XmlParseable bpmnXmlParse = bpmnXmlParser.getXmlParseBuilder()
                                                 .defineSourceAsStreamSource(new FileStreamSource(filePath))
                                                 .buildXmlParse();

        bpmnXmlParse.execute();

        ProcessDefinition processDefinition = bpmnXmlParse.getFinishedProcessDefinition();

        return processDefinition;
    }

}
