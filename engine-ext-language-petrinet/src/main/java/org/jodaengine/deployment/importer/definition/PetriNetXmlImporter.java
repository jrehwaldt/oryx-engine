package org.jodaengine.deployment.importer.definition;

import java.io.InputStream;
import java.util.Arrays;

import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.deployment.importer.definition.petri.PetriNetXmlParseListener;
import org.jodaengine.deployment.importer.definition.petri.PetriNetXmlParser;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.util.io.FileStreamSource;
import org.jodaengine.util.xml.XmlParseable;


/**
 * It is capable of importing an XML file that represents a PetriNet process.
 * 
 * In order to parse through the XML we use some classes of the Activiti Project (activiti.org).
 * Those are represented accordingly and mark with a license header.
 */
public class PetriNetXmlImporter implements ProcessDefinitionImporter {


    private XmlParseable petriXmlParse;
    
    /**
     * Instantiates a new petrinet xml importer using a xml string representation as source.
     *
     * @param xmlString the xml string
     * @param listeners any number of listeners
     */
    public PetriNetXmlImporter(String xmlString,
                           PetriNetXmlParseListener ... listeners) {
        
        PetriNetXmlParser petriXmlParser = new PetriNetXmlParser();
        petriXmlParser.getParseListeners().addAll(Arrays.asList(listeners));
        petriXmlParse = petriXmlParser.getXmlParseBuilder()
                                    .defineSourceAsString(xmlString)
                                    .buildXmlParse();
    }
    
    /**
     * Instantiates a new petri xml importer using a file stream as the source.
     *
     * @param fileStreamSource the file stream source
     * @param listeners any number of listeners
     */
    public PetriNetXmlImporter(FileStreamSource fileStreamSource,
                           PetriNetXmlParseListener ... listeners) {
        
        PetriNetXmlParser petriXmlParser = new PetriNetXmlParser();
        petriXmlParser.getParseListeners().addAll(Arrays.asList(listeners));
        petriXmlParse = petriXmlParser.getXmlParseBuilder()
                                    .defineSourceAsStreamSource(fileStreamSource)
                                    .buildXmlParse();
    }
    
    /**
     * Instantiates a new petri xml importer using an input stream as the source.
     *
     * @param inputStream the input stream
     * @param listeners any number of listeners
     */
    public PetriNetXmlImporter(InputStream inputStream,
                           PetriNetXmlParseListener ... listeners) {
        
        PetriNetXmlParser petriXmlParser = new PetriNetXmlParser();
        petriXmlParser.getParseListeners().addAll(Arrays.asList(listeners));
        
        petriXmlParse = petriXmlParser.getXmlParseBuilder()
                                    .defineSourceAsInputStream(inputStream)
                                    .buildXmlParse();
    }
    
    @Override
    public ProcessDefinition createProcessDefinition() {
        petriXmlParse.execute();
        ProcessDefinition processDefinition = petriXmlParse.getFinishedProcessDefinition();
        return processDefinition;
    }
}
