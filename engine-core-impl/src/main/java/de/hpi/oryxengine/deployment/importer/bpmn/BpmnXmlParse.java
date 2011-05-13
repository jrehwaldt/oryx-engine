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

package de.hpi.oryxengine.deployment.importer.bpmn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.CreationPattern;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.JodaEngineRuntimeException;
import de.hpi.oryxengine.node.activity.custom.AutomatedDummyActivity;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.node.incomingbehaviour.SimpleJoinBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.process.structure.TransitionBuilder;
import de.hpi.oryxengine.process.structure.condition.CheckVariableTrueCondition;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.allocation.CreationPatternBuilder;
import de.hpi.oryxengine.resource.allocation.CreationPatternBuilderImpl;
import de.hpi.oryxengine.resource.allocation.pattern.AllocateSinglePattern;
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
     * @param streamSource
     *            the stream source
     */
    public BpmnXmlParse(BpmnXmlParser parser, StreamSource streamSource) {

        super(parser, streamSource);
        this.processBuilder = new ProcessDefinitionBuilderImpl();
        this.parseListeners = parser.getParseListeners();
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
    public BpmnXmlParse execute() {

        // At first an object model of the whole BPMN XMl is created. Afterwards we can process it.
        super.execute();

        try {
            // Here we start parsing the process model and creating the ProcessDefintion
            parseRootElement();

        } catch (JodaEngineRuntimeException dalmatinaRuntimeException) {
            throw dalmatinaRuntimeException;
        } catch (Exception e) {

            String errorMessage = "Unknown exception";
            logger.error(errorMessage, e);
            // TODO: @Gerardo Schmeiß die Exception weiter
        } finally {

            if (getProblemLogger().hasWarnings()) {
                getProblemLogger().logWarnings();
            }
            if (getProblemLogger().hasErrors()) {
                getProblemLogger().throwDalmatinaRuntimeExceptionForErrors();
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

        List<XmlElement> processes = rootElement.getElements("process");

        if (processes.size() == 0) {
            String errorMessage = "No process have been defined in the BPMN serialized XMl file.";
            throw new JodaEngineRuntimeException(errorMessage);
        }
        if (processes.size() > 1) {
            String errorMessage = "Joda Engine cannot read more than one process in a BPMN serialized XMl file.";
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
        if (processElement.getAttribute("name") == null || processElement.getAttribute("name").isEmpty()) {

            processName = processElement.getAttribute("id");
        } else {

            processName = processElement.getAttribute("name");
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
    protected void parseElements(XmlElement processElement) {

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
    protected void parseStartEvents(XmlElement parentXmlElement) {

        List<XmlElement> startEventXmlElements = parentXmlElement.getElements("startEvent");

        if (startEventXmlElements.size() > 1) {

            getProblemLogger().addError("Multiple start events are currently not supported", parentXmlElement);
        } else if (startEventXmlElements.size() > 0) {

            XmlElement startEventXmlElement = startEventXmlElements.get(0);

            Node startEventNode = BpmnNodeFactory.createBpmnStartEventNode(processBuilder);

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
    protected void parseActivities(XmlElement parentElement) {

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
            } else if (("userTask").equals(activityElement.getTagName())) {
                parseUserTask(activityElement);
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
                || ("sendTask").equals(activityElement.getTagName())) {
                getProblemLogger().addWarning("Ignoring unsupported activity type", activityElement);
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
    protected void parseExclusiveGateway(XmlElement exclusiveGwElement) {

        Node exclusiveGatewayNode = BpmnNodeFactory.createBpmnXorGatewayNode(processBuilder);

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
    protected void parseParallelGateway(XmlElement parallelGatewayElement) {

        Node parallelGatewayNode = BpmnNodeFactory.createBpmnAndGatewayNode(processBuilder);

        parseGeneralNodeInformation(parallelGatewayElement, parallelGatewayNode);

        getNodeXmlIdTable().put((String) parallelGatewayNode.getAttribute("idXml"), parallelGatewayNode);

        for (BpmnXmlParseListener parseListener : parseListeners) {
            parseListener.parseParallelGateway(parallelGatewayElement, parallelGatewayNode);
        }
    }

    /**
     * Parses a task with no specific type (behaves as passthrough).
     */
    protected void parseTask(XmlElement taskXmlElement) {

        Node taskNode = processBuilder.getNodeBuilder().setIncomingBehaviour(new SimpleJoinBehaviour())
        .setOutgoingBehaviour(new TakeAllSplitBehaviour())
        .setActivityBehavior(new AutomatedDummyActivity("Doing something")).buildNode();

        parseGeneralNodeInformation(taskXmlElement, taskNode);
        getNodeXmlIdTable().put((String) taskNode.getAttribute("idXml"), taskNode);

        for (BpmnXmlParseListener parseListener : parseListeners) {
            parseListener.parseTask(taskXmlElement, taskNode);
        }
    }

    // Here comes specific finals for UserTasks

    protected static final String HUMAN_PERFORMER = "humanPerformer";
    protected static final String POTENTIAL_OWNER = "potentialOwner";

    protected static final String RESOURCE_ASSIGNMENT_EXPR = "resourceAssignmentExpression";
    protected static final String FORMAL_EXPRESSION = "formalExpression";

    protected static final String USER_PREFIX = "participant(";
    protected static final String GROUP_PREFIX = "role(";

    /**
     * Parses a task with no specific type (behaves as passthrough).
     */
    protected void parseUserTask(XmlElement taskXmlElement) {

        CreationPattern creationPattern = parseInformationForUserTask(taskXmlElement);

        Node taskNode = BpmnNodeFactory.createBpmnUserTaskNode(processBuilder, creationPattern,
            new AllocateSinglePattern());

        parseGeneralNodeInformation(taskXmlElement, taskNode);
        getNodeXmlIdTable().put((String) taskNode.getAttribute("idXml"), taskNode);

        for (BpmnXmlParseListener parseListener : parseListeners) {
            parseListener.parseUserTask(taskXmlElement, taskNode);
        }
    }

    protected CreationPattern parseInformationForUserTask(XmlElement taskXmlElement) {

        CreationPatternBuilder patternBuilder = new CreationPatternBuilderImpl();

        patternBuilder.setItemSubject(taskXmlElement.getAttribute("name"));
        patternBuilder.setItemDescription(parseDocumentation(taskXmlElement));

        parseHumanPerformer(taskXmlElement, patternBuilder);

        return patternBuilder.buildConcreteResourcePattern();
    }

    protected void parseHumanPerformer(XmlElement taskXmlElement, CreationPatternBuilder patternBuilder) {

        List<XmlElement> humanPerformerElements = taskXmlElement.getElements(HUMAN_PERFORMER);

        if (humanPerformerElements.size() > 1) {

            String errorMessage = "Invalid task definition: multiple " + HUMAN_PERFORMER + " sub elements defined for "
                + taskXmlElement.getAttribute("name");
            getProblemLogger().addError(errorMessage, taskXmlElement);

        } else if (humanPerformerElements.size() == 1) {

            XmlElement humanPerformerElement = humanPerformerElements.get(0);
            if (humanPerformerElement != null) {
                parseHumanPerformerResourceAssignment(humanPerformerElement, patternBuilder);
            }
        }
    }

    // protected void parsePotentialOwner(XmlElement taskElement, TaskBuilder taskBuilder) {
    //
    // List<XmlElement> potentialOwnerElements = taskElement.elements(POTENTIAL_OWNER);
    // for (XmlElement potentialOwnerElement : potentialOwnerElements) {
    // parsePotentialOwnerResourceAssignment(potentialOwnerElement, taskBuilder);
    // }
    // }

    protected void parseHumanPerformerResourceAssignment(XmlElement performerElement,
                                                         CreationPatternBuilder patternBuilder) {

        // rae := ResourceAssignmentEspression
        XmlElement raeElement = performerElement.getElement(RESOURCE_ASSIGNMENT_EXPR);
        if (raeElement != null) {
            XmlElement feElement = raeElement.getElement(FORMAL_EXPRESSION);
            if (feElement != null) {

                String formalExpression = feElement.getText();
                if (formalExpression.startsWith(USER_PREFIX)) {

                    AbstractParticipant participantAssignedToTask = null;

                    for (AbstractParticipant participant : ServiceFactory.getIdentityService().getParticipants()) {

                        if (participant.getName().equals(
                            formalExpression.substring(USER_PREFIX.length(), formalExpression.length() - 1))) {

                            participantAssignedToTask = participant;
                        }
                    }

                    if (participantAssignedToTask == null) {
                        String errorMessage = "The spedified Performer '" + formalExpression
                            + "' is not available in the IdentityService.";
                        getProblemLogger().addError(errorMessage, performerElement);
                    }

                    patternBuilder.addResourceAssignedToItem(participantAssignedToTask);
                }
            }
        }
    }

    /**
     * Parses the end events of a certain level in the process (process, subprocess or another scope).
     * 
     * @param parentElement
     *            The 'parent' element that contains the end events (process, subprocess).
     */
    protected void parseEndEvents(XmlElement parentElement) {

        for (XmlElement endEventXmlElement : parentElement.getElements("endEvent")) {

            Node endEventNode = BpmnNodeFactory.createBpmnEndEventNode(processBuilder);

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
     *            - The 'process' element wherein the sequence flow are defined.
     */
    protected void parseSequenceFlow(XmlElement processElement) {

        for (XmlElement sequenceFlowElement : processElement.getElements("sequenceFlow")) {

            String id = sequenceFlowElement.getAttribute("id");
            String sourceRef = sequenceFlowElement.getAttribute("sourceRef");
            String destinationRef = sequenceFlowElement.getAttribute("targetRef");

            if (sourceRef == null && destinationRef == null) {
                String errorMessage = "Each SequenceFlow XML tag must have a sourceRef"
                    + " and a destinationRef corresponding to a XML activity."
                    + " One of these attributes are not set correctly. Please do that!!";

                getProblemLogger().addError(errorMessage, sequenceFlowElement);
                return;
            }

            Node sourceNode = getNodeXmlIdTable().get(sourceRef);
            Node destinationNode = getNodeXmlIdTable().get(destinationRef);

            if (sourceNode == null || destinationNode == null) {
                if (sourceNode == null) {
                    String errorMessage = "The source '" + sourceRef + "' is not available in the XML.";
                    getProblemLogger().addError(errorMessage, sequenceFlowElement);
                }
                if (destinationNode == null) {
                    String errorMessage = "The destination '" + destinationRef + "' is not available in the XMl.";
                    getProblemLogger().addError(errorMessage, sequenceFlowElement);
                }
                return;
            }

            Condition transitionCondition = parseSequenceFlowCondition(sequenceFlowElement);

            TransitionBuilder transitionBuilder = processBuilder.getTransitionBuilder().transitionGoesFromTo(
                sourceNode, destinationNode);
            if (transitionCondition != null) {
                transitionBuilder.setCondition(transitionCondition);
            }

            Transition transition = transitionBuilder.buildTransition();

            for (BpmnXmlParseListener parseListener : parseListeners) {
                parseListener.parseSequenceFlow(sequenceFlowElement, transition);
            }
        }
    }

    /**
     * Parses a condition expression on a sequence flow.
     * 
     * @param seqFlowElement
     *            - The 'sequenceFlow' element that can contain a condition.
     */
    protected Condition parseSequenceFlowCondition(XmlElement seqFlowElement) {

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
    protected String parseDocumentation(XmlElement element) {

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
    protected void parseGeneralNodeInformation(XmlElement activityElement, Node node) {

        String id = activityElement.getAttribute("id");
        if (logger.isDebugEnabled()) {
            logger.debug("Parsing activity " + id);
        }

        node.setAttribute("idXml", id);
        node.setAttribute("name", activityElement.getAttribute("name"));
        node.setAttribute("description", parseDocumentation(activityElement));
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
