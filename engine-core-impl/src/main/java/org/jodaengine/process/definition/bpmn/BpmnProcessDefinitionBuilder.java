package org.jodaengine.process.definition.bpmn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jodaengine.eventmanagement.processevent.incoming.IncomingStartProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.start.BaseIncomingStartProcessEvent;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.activation.ProcessDeActivationPattern;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.jodaengine.process.instantiation.ProcessInstantiationPattern;
import org.jodaengine.process.instantiation.StartProcessInstantiationPattern;
import org.jodaengine.process.structure.ControlFlowBuilder;
import org.jodaengine.process.structure.ControlFlowBuilderImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;
import org.jodaengine.process.structure.NodeBuilderImpl;
import org.jodaengine.util.Attributable;
import org.jodaengine.util.PatternAppendable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ProcessBuilderImpl. As you would think, only nodes that were created using createStartNode() become
 * actually start nodes.
 */
public final class BpmnProcessDefinitionBuilder implements ProcessDefinitionBuilder, Attributable {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private List<Node> startNodes;
    private ProcessDefinitionID id;
    private String name;
    private String description;
    private Map<IncomingStartProcessEvent, Node> temporaryStartTriggers;
    private Map<String, Object> temporaryAttributeTable;
    private List<ProcessInstantiationPattern> temporaryInstantiationPatterns;
    private List<ProcessDeActivationPattern> temporaryActivationPatterns;
    private StartProcessInstantiationPattern startInstantiationPattern;

    /**
     * Retrieves the {@link BpmnProcessDefinitionBuilder} in order to build a {@link BpmnProcessDefinition}.
     * 
     * @return a {@link BpmnProcessDefinitionBuilder}
     */
    public static BpmnProcessDefinitionBuilder newBuilder() {

        return new BpmnProcessDefinitionBuilder();
    }

    /**
     * Instantiates some temporary datastructures.
     */
    private BpmnProcessDefinitionBuilder() {

        resetingThisBuilder();
    }

    /**
     * Resets this builder.
     */
    private void resetingThisBuilder() {

        this.startNodes = new ArrayList<Node>();

        this.id = new ProcessDefinitionID(UUID.randomUUID().toString());
        this.name = null;
        this.description = null;
        this.temporaryStartTriggers = new HashMap<IncomingStartProcessEvent, Node>();
        this.temporaryAttributeTable = new HashMap<String, Object>();
        this.temporaryInstantiationPatterns = new ArrayList<ProcessInstantiationPattern>();
        this.temporaryActivationPatterns = new ArrayList<ProcessDeActivationPattern>();
        this.startInstantiationPattern = null;
    }

    /**
     * Sets the name of the {@link ProcessDefinition} to build.
     * 
     * @param processName
     *            - the name of the {@link ProcessDefinition}
     * @return the {@link BpmnProcessDefinitionBuilder} in order to keep on building the {@link BpmnProcessDefinition}
     */
    public BpmnProcessDefinitionBuilder setName(String processName) {

        this.name = processName;
        this.id = new ProcessDefinitionID(name);
        return this;
    }

    /**
     * Sets the description.
     * 
     * @param processDescription
     *            - the description of the {@link ProcessDefinition}
     * @return the {@link BpmnProcessDefinitionBuilder} in order to keep on building the {@link BpmnProcessDefinition}
     */
    public BpmnProcessDefinitionBuilder setDescription(String processDescription) {

        this.description = processDescription;
        return this;

    }

    /**
     * This will create a start trigger for the {@link ProcessDefinition}.
     * 
     * @param startProcessEvent
     *            - the {@link IncomingStartProcessEvent} that instantiates this {@link BpmnProcessDefinition}
     * @param startNode
     *            - the {@link Node startNode}
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    public BpmnProcessDefinitionBuilder createStartTrigger(IncomingStartProcessEvent startProcessEvent, Node startNode) {

        IncomingStartProcessEvent event = new BaseIncomingStartProcessEvent(startProcessEvent.getAdapterConfiguration(),
            startProcessEvent.getCondition(), id);
        this.temporaryStartTriggers.put(event, startNode);

        return this;
    }

    @Override
    public void setAttribute(String attributeId, Object attibuteValue) {

        this.temporaryAttributeTable.put(attributeId, attibuteValue);
    }

    @Override
    public NodeBuilder getNodeBuilder() {

        return new NodeBuilderImpl();
    }

    @Override
    public ControlFlowBuilder getControlFlowBuilder() {

        return new ControlFlowBuilderImpl();
    }

    /**
     * Marks the given {@link Node} as possible node for the process instantiation.
     * 
     * @param node
     *            - the {@link Node} that should be marked
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    public BpmnProcessDefinitionBuilder addNodeAsStartNode(Node node) {

        startNodes.add(node);
        return this;
    }

    /**
     * Adds an InstantiationPattern.
     * 
     * @param instantiationPattern
     *            - the {@link ProcessInstantiationPattern} that should be added
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    public BpmnProcessDefinitionBuilder addInstantiationPattern(ProcessInstantiationPattern instantiationPattern) {

        this.temporaryInstantiationPatterns.add(instantiationPattern);
        return this;
    }

    /**
     * Adds an InstantiationPattern that should start the process instantiation.
     * 
     * @param startInstantiationPattern
     *            - the {@link ProcessInstantiationPattern} that should start the process instantiation
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    public BpmnProcessDefinitionBuilder addStartInstantiationPattern(StartProcessInstantiationPattern startInstantiationPattern) {

        this.startInstantiationPattern = startInstantiationPattern;
        return this;
    }

    /**
     * Adds a {@link ProcessDeActivationPattern} that as activates and deactivates the {@link ProcessDefinition}.
     * 
     * @param processDeActivationPattern
     *            - the {@link ProcessDeActivationPattern} that should be added
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    public BpmnProcessDefinitionBuilder addDeActivationPattern(ProcessDeActivationPattern processDeActivationPattern) {

        this.temporaryActivationPatterns.add(processDeActivationPattern);
        return this;
    }

    @Override
    public ProcessDefinition buildDefinition()
    throws IllegalStarteventException {

        checkingDefinitionConstraints();

        BpmnProcessDefinition definition = buildResultDefinition();

        // cleanup
        resetingThisBuilder();

        return definition;
    }

    /**
     * This method encapsulates the creation of the {@link BpmnProcessDefinition}.
     * 
     * @return the {@link BpmnProcessDefinition processDefinition} as result of this builder
     * @throws IllegalStarteventException
     *             no valid start event found
     */
    private BpmnProcessDefinition buildResultDefinition()
    throws IllegalStarteventException {

        StartProcessInstantiationPattern startInstantionPattern = appendingInstantiationPatterns();
        ProcessDeActivationPattern activationPattern = appendingDeActivationPatterns();

        BpmnProcessDefinition definition = new BpmnProcessDefinition(id, name, description, startNodes,
            startInstantionPattern, activationPattern);

        for (Map.Entry<IncomingStartProcessEvent, Node> entry : temporaryStartTriggers.entrySet()) {
            definition.addStartTrigger(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Object> entry : temporaryAttributeTable.entrySet()) {
            definition.setAttribute(entry.getKey(), entry.getValue());
        }

        return definition;
    }

    /**
     * Appends all {@link ProcessDeActivationPattern} assigned to his {@link ProcessDefinition} one after another.
     * 
     * @return the first {@link ProcessDeActivationPattern} in the linked list
     */
    private ProcessDeActivationPattern appendingDeActivationPatterns() {

        // We have already assured that there are activationPatterns
        PatternAppendable<ProcessDeActivationPattern> lastActivationPattern = null;
        boolean firstActivationPassed = false;
        for (ProcessDeActivationPattern activationPattern : temporaryActivationPatterns) {

            if (!firstActivationPassed) {

                lastActivationPattern = activationPattern;
                continue;
            }

            lastActivationPattern.setNextPattern(activationPattern);
            lastActivationPattern = activationPattern;
        }

        // Returning the first Pattern
        return temporaryActivationPatterns.get(0);
    }

    /**
     * Appends all the instantiationPattern and returns the first one.
     * 
     * @return the first {@link ProcessInstantiationPattern} which is the defined {@link StartProcessInstantiationPattern}
     */
    private StartProcessInstantiationPattern appendingInstantiationPatterns() {

        PatternAppendable<ProcessInstantiationPattern> lastInstantiationPattern = this.startInstantiationPattern;
        for (ProcessInstantiationPattern instantiationPattern : temporaryInstantiationPatterns) {

            lastInstantiationPattern.setNextPattern(instantiationPattern);
            lastInstantiationPattern = instantiationPattern;
        }

        // Returning the first Pattern
        return this.startInstantiationPattern;
    }

    /**
     * Checks the constraints for building a {@link ProcessDefinitionInside}. Throws an exception if a constrained was
     * not held.
     */
    private void checkingDefinitionConstraints() {

        if (this.startInstantiationPattern == null) {

            String errorMessage = "The first InstantiationPattern should be a StartInstantiationPattern."
                + "Please perfom 'addStartInstantiationPattern(...)'";
            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }

        if (temporaryActivationPatterns == null || temporaryActivationPatterns.isEmpty()) {

            String errorMessage = "The ProcessDefinitionActivationPattern was set."
                + "Please perfom 'addActivationPattern(...)'";
            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }
    }

    @Override
    public Map<String, Object> getAttributes() {

        return this.temporaryAttributeTable;
    }

    @Override
    public Object getAttribute(String attributeKey) {

        return this.temporaryAttributeTable.get(attributeKey);
    }
}
