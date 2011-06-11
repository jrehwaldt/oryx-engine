/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This code is part of the Activiti project under the above license:
 * 
 *                  http://www.activiti.org
 * 
 * We did some modification which are hereby also under the Apache License, Version 2.0.
 */

package org.jodaengine.deployment.importer.definition.petri;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.node.factory.petri.PetriNodeFactory;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.petri.PetriProcessDefinitionBuilder;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.ControlFlowBuilder;
import org.jodaengine.process.structure.Node;
import org.jodaengine.util.Attributable;
import org.jodaengine.util.io.StreamSource;
import org.jodaengine.util.xml.XmlElement;
import org.jodaengine.util.xml.XmlParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specific parsing of one PNML XML file, created by the {@link PetriNetXmlParser}.
 */
public class PetriNetXmlParse extends XmlParse {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private ProcessDefinition finishedProcessDefinition;

    private PetriProcessDefinitionBuilder processBuilder;

    private List<PetriNetXmlParseListener> parseListeners;

    /**
     * Mapping containing values stored during the first phase of parsing since other elements can reference these
     * messages.
     */
    private Map<String, Node> nodeXmlIdTable;

    /**
     * Constructor to be called by the {@link PetriNetXmlParser}.
     * 
     * Note the package modifier here: only the {@link PetriNetXmlParser} is allowed to create instances.
     * 
     * @param petriNetXmlParser
     *            - in order to have general configuration for parsing through the XML
     * @param streamSource
     *            the stream source
     */
    public PetriNetXmlParse(PetriNetXmlParser petriNetXmlParser, StreamSource streamSource) {

        super(petriNetXmlParser, streamSource);
        this.processBuilder = new PetriProcessDefinitionBuilder();
        this.parseListeners = petriNetXmlParser.getParseListeners();
    }

    @Override
    public ProcessDefinition getFinishedProcessDefinition() {

        if (finishedProcessDefinition == null) {

            String errorMessage = "Building the ProcessDefinition does not finish.";
            throw new JodaEngineRuntimeException(errorMessage);
        }

        return this.finishedProcessDefinition;
    }

    @Override
    public PetriNetXmlParse execute() {

        // At first an object model of the whole PetriNet XML is created. Afterwards we can process it.
        super.execute();

        try {
            // Here we start parsing the process model and creating the ProcessDefintion
            parseRootElement();

        } catch (JodaEngineRuntimeException jodaException) {
            throw jodaException;
        } catch (Exception javaException) {

            String errorMessage = "Unknown exception";
            logger.error(errorMessage, javaException);
            // TODO: @Gerardo Schmeiß die Exception weiter
        } finally {

            if (getProblemLogger().hasWarnings()) {
                getProblemLogger().logWarnings();
            }
            if (getProblemLogger().hasErrors()) {
                getProblemLogger().throwJodaEngineRuntimeExceptionForErrors();
            }
        }

        return this;
    }

    /**
     * Parses the 'definitions' root element.
     */
    protected void parseRootElement() {

        // This is the Activiti method body for parsing through the XML object model
        // The Joda Engine needs less information than Activiti. That's why some methods are commented out.
        parseProcessDefinitions();
    }

    /**
     * Parses all the process definitions defined within the 'definitions' root element.
     */
    protected void parseProcessDefinitions() {

        List<XmlElement> processes = rootElement.getElements("net");
        System.out.println(processes);

        if (processes.size() == 0) {
            String errorMessage = "No process have been defined in the PetriNet serialized XMl file.";
            throw new JodaEngineRuntimeException(errorMessage);
        }

        parseProcess(processes.get(0));
    }

    /**
     * Parses one process (e.g. anything inside a &lt;process&gt; element).
     * 
     * @param processElement
     *            The 'process' element.
     */
    protected void parseProcess(XmlElement processElement) {

        // Reset all mappings that are related to one process definition
        // sequenceFlows = new HashMap<String, TransitionImpl>();

        // The name of the ProcessDefinition is the value of 'name' attribute, in case that it is defined.
        String processName;
        processName = processElement.getAttribute("id");
        processBuilder.setName(processName);
        
        XmlElement element = processElement.getElement("name");
        if(element != null) {
            processBuilder.setDescription(element.getElement("text").getText());
        }
        parseElements(processElement.getElement("page"));
        
       
            
        this.finishedProcessDefinition = processBuilder.buildDefinition();
        

        if (this.finishedProcessDefinition != null) {
            parseGeneralInformation(processElement, processBuilder);
        }

    }

    /**
     * Parses a scope: a process, subprocess, etc.
     * 
     * Note that a process definition is a scope on itself.
     * 
     * @param processElement
     *            The XML element defining the process
     */
    protected void parseElements(XmlElement processElement) {

        parsePlaces(processElement);
        parseTransitions(processElement);
        parseSequenceFlow(processElement);
    }

    protected void parsePlaces(XmlElement parentXmlElement) {

        List<XmlElement> placeXmlElements = parentXmlElement.getElements("place");

        for(XmlElement element : placeXmlElements) {

            Node place = PetriNodeFactory.createPlace();
            if(element.getElements("initialMarking").size() > 0) {
                processBuilder.addStartNode(place);
            }

            parseGeneralInformation(element, place);

            getNodeXmlIdTable().put((String) place.getAttribute("idXml"), place);
        }
    }
    
    protected void parseTransitions(XmlElement parentXmlElement) {

        List<XmlElement> placeXmlElements = parentXmlElement.getElements("transition");

        for(XmlElement element : placeXmlElements) {

            Node transition = PetriNodeFactory.createPetriTransition();

            parseGeneralInformation(element, transition);

            getNodeXmlIdTable().put((String) transition.getAttribute("idXml"), transition);
        }
    }

    /**
     * Parses all sequence flow of a scope.
     * 
     * @param processElement
     *            - The 'process' element wherein the sequence flow are defined.
     */
    protected void parseSequenceFlow(XmlElement processElement) {

        for (XmlElement sequenceFlowElement : processElement.getElements("arc")) {

            @SuppressWarnings("unused")
            String id = sequenceFlowElement.getAttribute("id");
            String source = sequenceFlowElement.getAttribute("source");
            String destination = sequenceFlowElement.getAttribute("target");

            if (source == null && destination == null) {
                String errorMessage = "Each SequenceFlow XML tag must have a sourceRef"
                    + " and a destinationRef corresponding to a XML activity."
                    + " One of these attributes are not set correctly. Please do that!!";

                getProblemLogger().addError(errorMessage, sequenceFlowElement);
                return;
            }

            Node sourceNode = getNodeXmlIdTable().get(source);
            Node destinationNode = getNodeXmlIdTable().get(destination);

            if (sourceNode == null || destinationNode == null) {
                if (sourceNode == null) {
                    String errorMessage = "The source '" + source + "' is not available in the XML.";
                    getProblemLogger().addError(errorMessage, sequenceFlowElement);
                }
                if (destinationNode == null) {
                    String errorMessage = "The destination '" + destination + "' is not available in the XMl.";
                    getProblemLogger().addError(errorMessage, sequenceFlowElement);
                }
                return;
            }


            ControlFlowBuilder controlFlowBuilder = processBuilder.getControlFlowBuilder().controlFlowGoesFromTo(
                sourceNode, destinationNode);

            ControlFlow controlFlow = controlFlowBuilder.buildControlFlow();

            for (PetriNetXmlParseListener parseListener : parseListeners) {
                parseListener.parseSequenceFlow(sequenceFlowElement, controlFlow, processBuilder);
            }
        }
    }

    /**
     * Parses the generic information of an  element (id etc.).
     * 
     * @param element
     *            the activity element to parse
     * @param attributable
     *            the attributable
     */
    protected void parseGeneralInformation(XmlElement element, Attributable attributable) {

        String id = element.getAttribute("id");
        if (logger.isDebugEnabled()) {
            logger.debug("Parsing attributable " + id);
        }

        attributable.setAttribute("idXml", id);
        
        if(element.getElement("name") != null){
            attributable.setAttribute("name", element.getElement("name").getElement("text").getText());
        }
        attributable.setAttribute("type", element.getTagName());
        attributable.setAttribute("line", element.getLine());
    }

    /**
     * Returns a lazily initialized xml id map.
     * 
     * @return a map holding node's ids to nodes
     */
    private Map<String, Node> getNodeXmlIdTable() {

        if (this.nodeXmlIdTable == null) {
            this.nodeXmlIdTable = new HashMap<String, Node>();
        }
        return this.nodeXmlIdTable;
    }
    
    
}
