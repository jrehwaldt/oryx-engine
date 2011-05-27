package org.jodaengine.process.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.eventmanagement.subscription.StartEventImpl;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.eventmanagement.subscription.condition.complex.AndEventCondition;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.activation.ProcessDefinitionDeActivationPattern;
import org.jodaengine.process.instantiation.InstantiationPattern;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeBuilder;
import org.jodaengine.process.structure.NodeBuilderImpl;
import org.jodaengine.process.structure.StartNodeBuilderImpl;
import org.jodaengine.process.structure.TransitionBuilder;
import org.jodaengine.process.structure.TransitionBuilderImpl;
import org.jodaengine.util.PatternAppendable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ProcessBuilderImpl. As you would think, only nodes that were created using createStartNode() become
 * actually start nodes.
 */
public class ProcessDefinitionBuilderImpl implements ProcessDefinitionBuilder {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private List<Node> startNodes;
    private ProcessDefinitionID id;
    private String name;
    private String description;
    private Map<ProcessStartEvent, Node> temporaryStartTriggers;
    private Map<String, Object> temporaryAttributeTable;
    private List<InstantiationPattern> temporaryInstantiationPatterns;
    private List<ProcessDefinitionDeActivationPattern> temporaryActivationPatterns;
    private StartInstantiationPattern startInstantiationPattern;

    /**
     * Instantiates some temporary datastructures.
     */
    public ProcessDefinitionBuilderImpl() {

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
        this.temporaryStartTriggers = new HashMap<ProcessStartEvent, Node>();
        this.temporaryAttributeTable = null;
        this.temporaryInstantiationPatterns = new ArrayList<InstantiationPattern>();
        this.temporaryActivationPatterns = new ArrayList<ProcessDefinitionDeActivationPattern>();
        this.startInstantiationPattern = null;
    }

    @Override
    public ProcessDefinitionBuilder setName(String processName) {

        this.name = processName;
        this.id = new ProcessDefinitionID(name);
        return this;
    }

    @Override
    public ProcessDefinitionBuilder setDescription(String description) {

        this.description = description;
        return this;

    }

    @Override
    public ProcessDefinitionBuilder createStartTrigger(EventType eventType,
                                                       AdapterConfiguration adapterConfig,
                                                       List<EventCondition> eventConditions,
                                                       Node startNode) {

        ProcessStartEvent event = new StartEventImpl(eventType, adapterConfig, new AndEventCondition(eventConditions),
            id);
        this.temporaryStartTriggers.put(event, startNode);

        return this;
    }

    @Override
    public ProcessDefinitionBuilder setAttribute(String attributeId, Object attibuteValue) {

        if (this.temporaryAttributeTable == null) {
            this.temporaryAttributeTable = new HashMap<String, Object>();
        }

        this.temporaryAttributeTable.put(attributeId, attibuteValue);

        return this;
    }

    @Override
    public NodeBuilder getNodeBuilder() {

        return new NodeBuilderImpl();
    }

    @Override
    public TransitionBuilder getTransitionBuilder() {

        return new TransitionBuilderImpl();
    }

    @Override
    public NodeBuilder getStartNodeBuilder() {

        return new StartNodeBuilderImpl(this);
    }

    /**
     * Getter for the StartNodes-List.
     * 
     * @return a {@link List} of {@link Node}
     */
    public List<Node> getStartNodes() {

        return startNodes;
    }

    @Override
    public ProcessDefinitionBuilder addInstantiationPattern(InstantiationPattern instantiationPattern) {

        this.temporaryInstantiationPatterns.add(instantiationPattern);
        return this;
    }

    @Override
    public ProcessDefinitionBuilder addStartInstantiationPattern(StartInstantiationPattern startInstantiationPattern) {

        this.startInstantiationPattern = startInstantiationPattern;
        return this;
    }

    @Override
    public ProcessDefinitionBuilder addActivationPattern(ProcessDefinitionDeActivationPattern activationPattern) {

        this.temporaryActivationPatterns.add(activationPattern);
        return this;
    }

    @Override
    public ProcessDefinition buildDefinition()
    throws IllegalStarteventException {

        checkingDefinitionConstraints();

        ProcessDefinitionImpl definition = buildResultDefinition();

        // cleanup
        resetingThisBuilder();

        return definition;
    }

    /**
     * This method encapsulates.
     * 
     * @return the {@link ProcessDefinitionImpl processDefinition} as result of this builder
     * @throws IllegalStarteventException
     */
    private ProcessDefinitionImpl buildResultDefinition()
    throws IllegalStarteventException {

        StartInstantiationPattern startInstantionPattern = appendingInstantiationPatterns();
        ProcessDefinitionDeActivationPattern activationPattern = appendingActivationPatterns();

        ProcessDefinitionImpl definition = new ProcessDefinitionImpl(id, name, description, startNodes,
            startInstantionPattern, activationPattern);

        for (Map.Entry<ProcessStartEvent, Node> entry : temporaryStartTriggers.entrySet()) {
            definition.addStartTrigger(entry.getKey(), entry.getValue());
        }

        return definition;
    }

    private ProcessDefinitionDeActivationPattern appendingActivationPatterns() {

        // We have already assured that there are activationPatterns
        PatternAppendable<ProcessDefinitionDeActivationPattern> lastActivationPattern = null;
        boolean firstActivationPassed = false;
        for (ProcessDefinitionDeActivationPattern activationPattern : temporaryActivationPatterns) {

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
     */
    private StartInstantiationPattern appendingInstantiationPatterns() {

        PatternAppendable<InstantiationPattern> lastInstantiationPattern = this.startInstantiationPattern;
        for (InstantiationPattern instantiationPattern : temporaryInstantiationPatterns) {

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
}
