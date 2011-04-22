package de.hpi.oryxengine.repository.importer.bpmn;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.activity.impl.BpmnStartEvent;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.NodeParameterBuilder;
import de.hpi.oryxengine.process.definition.NodeParameterBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.condition.CheckVariableTrueCondition;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.AndJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.EmptyOutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.XORSplitBehaviour;
import de.hpi.oryxengine.util.io.StreamSource;
import de.hpi.oryxengine.util.xml.XmlElement;
import de.hpi.oryxengine.util.xml.XmlParse;

/**
 * Specific parsing of one BPMN 2.0 XML file, created by the {@link BpmnXmlParser}.
 */
public class BpmnXmlParse extends XmlParse {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private ProcessDefinition finishedProcessDefinition;

    private ProcessDefinitionBuilder processBuilder;

    private List<BpmnXmlParseListener> parseListeners;

    /**
     * Mapping containing values stored during the first phase of parsing since other elements can reference these
     * messages.
     */
    private Map<String, Node> nodeXmlIdTable;

    /**
     * Constructor to be called by the {@link BpmnXmlParser}.
     * 
     * Note the package modifier here: only the {@link BpmnXmlParser} is allowed to create instances.
     * 
     * @param parser
     *            - in order to have general configuration for parsing through the XML
     */
    public BpmnXmlParse(BpmnXmlParser parser, StreamSource streamSource) {

        super(parser, streamSource);
        this.processBuilder = new ProcessBuilderImpl();
        this.parseListeners = parser.getParseListeners();
    }

    @Override
    public ProcessDefinition getFinishedProcessDefinition() {

        if (finishedProcessDefinition == null) {

            String errorMessage = "Building the ProcessDefinition does not finished.";
            throw new DalmatinaRuntimeException(errorMessage);
        }

        return this.finishedProcessDefinition;

    }

    @Override
    public BpmnXmlParse execute() {

        // At first an object model of the whole BPMN XMl is created. Afterwards we can process it.
        super.execute();

        try {
            // Here we start parsing the process model and creating the ProcessDefintion
            parseRootElement();

        } catch (DalmatinaRuntimeException dalmatinaRuntimeException) {
            throw dalmatinaRuntimeException;
        } catch (Exception e) {

            String errorMessage = "Unknown exception";
            logger.error(errorMessage, e);
        } finally {

            if (hasWarnings()) {
                logWarnings();
            }
            if (hasErrors()) {
                throwDalmatinaRuntimeExceptionForErrors();
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
    public void parseProcessDefinitions() {

        List<XmlElement> processes = rootElement.getElements("process");

        if (processes.size() == 0) {
            String errorMessage = "No process have been defined in the BPMN serialized XMl file.";
            throw new DalmatinaRuntimeException(errorMessage);
        }
        if (processes.size() > 1) {
            String errorMessage = "Joda Engine cannot read more than one process in a BPMN serialized XMl file.";
            throw new DalmatinaRuntimeException(errorMessage);
        }

        parseProcess(processes.get(0));
    }

    /**
     * Parses one process (e.g. anything inside a &lt;process&gt; element).
     * 
     * @param processElement
     *            The 'process' element.
     */
    public void parseProcess(XmlElement processElement) {

        // Reset all mappings that are related to one process definition
        // sequenceFlows = new HashMap<String, TransitionImpl>();

        // The name of the ProcessDefinition is the value of 'name' attribute, in case that it is defined.
        String processName;
        if (processElement.getAttribute("name") != null && processElement.getAttribute("name").isEmpty()) {

            processName = processElement.getAttribute("name");
        } else {

            processName = processElement.getAttribute("id");
        }
        processBuilder.setName(processName);

        processBuilder.setDescription(parseDocumentation(processElement));
        processBuilder.setAttribute("idInXml", processElement.getAttribute("id"));
        processBuilder.setAttribute("targetNamespace", rootElement.getAttribute("targetNamespace"));

        parseElements(processElement);

        try {

            this.finishedProcessDefinition = processBuilder.buildDefinition();
        } catch (IllegalStarteventException buildingDefinitionException) {

            String errorMessage = "The processDefintion could be built.";
            logger.error(errorMessage, buildingDefinitionException);
        }

        // Doing the afterwork
        for (BpmnXmlParseListener parseListener : parseListeners) {
            parseListener.parseProcess(processElement, finishedProcessDefinition);
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
    public void parseElements(XmlElement processElement) {

        parseStartEvents(processElement);
        parseActivities(processElement);
        parseEndEvents(processElement);
        parseSequenceFlow(processElement);
    }

    /**
     * Parses the start events of a certain level in the process (process, subprocess or another scope).
     * 
     * @param parentXmlElement
     *            The 'parent' element that contains the start events (process, subprocess).
     */
    public void parseStartEvents(XmlElement parentXmlElement) {

        List<XmlElement> startEventXmlElements = parentXmlElement.getElements("startEvent");

        if (startEventXmlElements.size() > 1) {

            addError("Multiple start events are currently not supported", parentXmlElement);
        } else if (startEventXmlElements.size() > 0) {

            XmlElement startEventXmlElement = startEventXmlElements.get(0);

            NodeParameterBuilder nodeParameterBuilder = new NodeParameterBuilderImpl();
            nodeParameterBuilder.setActivityBlueprintFor(BpmnStartEvent.class);

            Node startEventNode = processBuilder.createStartNode(nodeParameterBuilder.buildNodeParameter());

            parseGeneralNodeInformation(startEventXmlElement, startEventNode);

            getNodeXmlIdTable().put((String) startEventNode.getAttribute("idXml"), startEventNode);

            // We should think about forms for the startEvent
            // Maybe we can implement that as startTrigger
            // Example:
            // StartFormHandler startFormHandler = new StartFormHandler();
            // String startFormHandlerClassName = startEventElement.attributeNS(BpmnXmlParser.JODA_ENGINE_EXTENSIONS_NS,
            // "formHandlerClass");

            // Doing some afterwork
            for (BpmnXmlParseListener parseListener : parseListeners) {
                parseListener.parseStartEvent(startEventXmlElement, startEventNode);
            }
        }
    }

    /**
     * Parses the activities of a certain level in the process (process, subprocess or another scope).
     * 
     * @param parentElement
     *            The 'parent' element that contains the activities (process, subprocess).
     */
    public void parseActivities(XmlElement parentElement) {

        for (XmlElement activityElement : parentElement.getElements()) {
            if (("exclusiveGateway").equals(activityElement.getTagName())) {
                parseExclusiveGateway(activityElement);

            } else if (("parallelGateway").equals(activityElement.getTagName())) {
                parseParallelGateway(activityElement);

                // } else if (activityElement.getTagName().equals("scriptTask")) {
                // parseScriptTask(activityElement);
                //
                // } else if (activityElement.getTagName().equals("serviceTask")) {
                // parseServiceTask(activityElement);
                //
                // } else if (activityElement.getTagName().equals("businessRuleTask")) {
                // parseBusinessRuleTask(activityElement);
                //
            } else if (("task").equals(activityElement.getTagName())) {
                parseTask(activityElement);
                //
                // } else if (activityElement.getTagName().equals("manualTask")) {
                // parseManualTask(activityElement);
                //
                // } else if (("userTask").equals(activityElement.getTagName())) {
                // parseUserTask(activityElement);
                //
                // } else if (activityElement.getTagName().equals("sendTask")) {
                // parseSendTask(activityElement);
                //
                // } else if (activityElement.getTagName().equals("receiveTask")) {
                // parseReceiveTask(activityElement);
                //
                // } else if (activityElement.getTagName().equals("subProcess")) {
                // parseSubProcess(activityElement);
                //
                // } else if (activityElement.getTagName().equals("callActivity")) {
                // parseCallActivity(activityElement);
                //
                // } else if (activityElement.getTagName().equals("intermediateCatchEvent")) {
                // parseIntermediateCatchEvent(activityElement);

            } else if (("adHocSubProcess").equals(activityElement.getTagName())
                || ("complexGateway").equals(activityElement.getTagName())
                || ("eventBasedGateway").equals(activityElement.getTagName())
                || ("transaction").equals(activityElement.getTagName())
                || ("callActivity").equals(activityElement.getTagName())
                || ("intermediateCatchEvent").equals(activityElement.getTagName())
                || ("subProcess").equals(activityElement.getTagName())
                || ("receiveTask").equals(activityElement.getTagName())
                || ("userTask").equals(activityElement.getTagName())
                || ("sendTask").equals(activityElement.getTagName())) {
                addWarning("Ignoring unsupported activity type", activityElement);
            }
        }

        // Parse stuff common to activities above, e.g. something like markers
        // if (activity != null) {
        // parseMultiInstanceLoopCharacteristics(activityElement, activity);
        // }
    }

    /**
     * Parses an exclusive gateway declaration.
     * 
     * @param exclusiveGwElement
     *            the exclusive gateway element
     */
    public void parseExclusiveGateway(XmlElement exclusiveGwElement) {

        NodeParameterBuilder nodeParameterBuilder = new NodeParameterBuilderImpl(new SimpleJoinBehaviour(),
            new XORSplitBehaviour());
        nodeParameterBuilder.setActivityBlueprintFor(NullActivity.class);

        Node exclusiveGatewayNode = processBuilder.createNode(nodeParameterBuilder.buildNodeParameter());

        parseGeneralNodeInformation(exclusiveGwElement, exclusiveGatewayNode);

        getNodeXmlIdTable().put((String) exclusiveGatewayNode.getAttribute("idXml"), exclusiveGatewayNode);

        for (BpmnXmlParseListener parseListener : parseListeners) {
            parseListener.parseExclusiveGateway(exclusiveGwElement, exclusiveGatewayNode);
        }
    }

    /**
     * Parses a parallel gateway declaration.
     * 
     * @param parallelGatewayElement
     *            the parallel gateway element
     */
    public void parseParallelGateway(XmlElement parallelGatewayElement) {

        NodeParameterBuilder nodeParameterBuilder = new NodeParameterBuilderImpl(new AndJoinBehaviour(),
            new TakeAllSplitBehaviour());
        nodeParameterBuilder.setActivityBlueprintFor(NullActivity.class);

        Node parallelGatewayNode = processBuilder.createNode(nodeParameterBuilder.buildNodeParameter());

        parseGeneralNodeInformation(parallelGatewayElement, parallelGatewayNode);

        getNodeXmlIdTable().put((String) parallelGatewayNode.getAttribute("idXml"), parallelGatewayNode);

        for (BpmnXmlParseListener parseListener : parseListeners) {
            parseListener.parseParallelGateway(parallelGatewayElement, parallelGatewayNode);
        }
    }

    /**
     * Parses a task with no specific type (behaves as passthrough).
     */
    public void parseTask(XmlElement taskXmlElement) {

        NodeParameterBuilder nodeParameterBuilder = new NodeParameterBuilderImpl();
        nodeParameterBuilder.setActivityBlueprintFor(AutomatedDummyActivity.class).addConstructorParameter(
            String.class, "Doing something");

        Node taskNode = processBuilder.createNode(nodeParameterBuilder.buildNodeParameter());

        parseGeneralNodeInformation(taskXmlElement, taskNode);
        getNodeXmlIdTable().put((String) taskNode.getAttribute("idXml"), taskNode);

        for (BpmnXmlParseListener parseListener : parseListeners) {
            parseListener.parseTask(taskXmlElement, taskNode);
        }
    }

    /**
     * Parses the end events of a certain level in the process (process, subprocess or another scope).
     * 
     * @param parentElement
     *            The 'parent' element that contains the end events (process, subprocess).
     */
    public void parseEndEvents(XmlElement parentElement) {

        for (XmlElement endEventXmlElement : parentElement.getElements("endEvent")) {

            // EndActvities does not need a specific OutgoingBehaviour
            NodeParameterBuilder nodeParameterBuilder = new NodeParameterBuilderImpl(new SimpleJoinBehaviour(),
                new EmptyOutgoingBehaviour());
            nodeParameterBuilder.setActivityBlueprintFor(EndActivity.class);

            Node endEventNode = processBuilder.createNode(nodeParameterBuilder.buildNodeParameter());

            parseGeneralNodeInformation(endEventXmlElement, endEventNode);

            getNodeXmlIdTable().put((String) endEventNode.getAttribute("idXml"), endEventNode);

            // Doing some afterwork
            for (BpmnXmlParseListener parseListener : parseListeners) {
                parseListener.parseEndEvent(endEventXmlElement, endEventNode);
            }
        }
    }

    /**
     * Parses all sequence flow of a scope.
     * 
     * @param processElement
     *            The 'process' element wherein the sequence flow are defined.
     * @param scope
     *            The scope to which the sequence flow must be added.
     */
    public void parseSequenceFlow(XmlElement processElement) {

        for (XmlElement sequenceFlowElement : processElement.getElements("sequenceFlow")) {

            String id = sequenceFlowElement.getAttribute("id");
            String sourceRef = sequenceFlowElement.getAttribute("sourceRef");
            String destinationRef = sequenceFlowElement.getAttribute("targetRef");

            if (sourceRef == null && destinationRef == null) {
                String errorMessage = "Each SequenceFlow XML tag must have a sourceRef and a destinationRef corresponding to a XML activity. One of these attributes are not set correctly. Please do that!!";
                addError(errorMessage, sequenceFlowElement);
                return;
            }

            Node sourceNode = getNodeXmlIdTable().get(sourceRef);
            Node destinationNode = getNodeXmlIdTable().get(destinationRef);

            if (sourceNode == null || destinationNode == null) {
                if (sourceNode == null) {
                    String errorMessage = "The source '" + sourceRef + "' is not available in the XML.";
                    addError(errorMessage, sequenceFlowElement);
                }
                if (destinationNode == null) {
                    String errorMessage = "The destination '" + destinationRef + "' is not available in the XMl.";
                    addError(errorMessage, sequenceFlowElement);
                }
                return;
            }

            Condition transitionCondition = parseSequenceFlowCondition(sequenceFlowElement);

            if (transitionCondition != null) {
                processBuilder.createTransition(sourceNode, destinationNode, transitionCondition);
            } else {
                processBuilder.createTransition(sourceNode, destinationNode);
            }

            // TODO @Gerardo&PairprogrammingPartner umstellen das mit dem Builder
            // for (BpmnXmlParseListener parseListener : parseListeners) {
            // parseListener.parseSequenceFlow(sequenceFlowElement, transition);
            // }
        }
    }

    /**
     * Parses a condition expression on a sequence flow.
     * 
     * @param seqFlowElement
     *            - The 'sequenceFlow' element that can contain a condition.
     */
    public Condition parseSequenceFlowCondition(XmlElement seqFlowElement) {

        XmlElement conditionExprElement = seqFlowElement.getElement("conditionExpression");

        if (conditionExprElement == null) {
            return null;
        }

        String expression = conditionExprElement.getText().trim();
        Condition expressionCondition = new CheckVariableTrueCondition(expression);
        return expressionCondition;
    }

    /**
     * Extracting the documentation Attribute in the {@link XmlElement}.
     */
    public String parseDocumentation(XmlElement element) {

        XmlElement docElement = element.getElement("documentation");
        if (docElement != null) {
            return docElement.getText().trim();
        }
        return null;
    }

    /**
     * Parses the generic information of an activity element (id, name, documentation, etc.), and creates a new
     * {@link ActivityImpl} on the given scope element.
     */
    public void parseGeneralNodeInformation(XmlElement activityElement, Node node) {

        String id = activityElement.getAttribute("id");
        if (logger.isDebugEnabled()) {
            logger.debug("Parsing activity " + id);
        }

        node.setAttribute("idXml", id);
        node.setAttribute("name", activityElement.getAttribute("name"));
        node.setAttribute("descrption", parseDocumentation(activityElement));
        node.setAttribute("default", activityElement.getAttribute("default"));
        node.setAttribute("type", activityElement.getTagName());
        node.setAttribute("line", activityElement.getLine());
    }

    private Map<String, Node> getNodeXmlIdTable() {

        if (this.nodeXmlIdTable == null) {
            this.nodeXmlIdTable = new HashMap<String, Node>();
        }
        return this.nodeXmlIdTable;
    }
}
