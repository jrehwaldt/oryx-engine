package org.jodaengine.process.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jodaengine.eventmanagement.adapter.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.registration.EventCondition;
import org.jodaengine.eventmanagement.registration.ProcessStartEvent;
import org.jodaengine.eventmanagement.registration.StartEventImpl;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.JodaEngineRuntimeException;
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
    private UUID id;
    private String name;
    private String description;
    private Map<ProcessStartEvent, Node> temporaryStartTriggers;
    private Map<String, Object> temporaryAttributeTable;
    private List<InstantiationPattern> temporaryInstantiationPatterns;
    private StartInstantiationPattern startInstantiationPattern;

    /**
     * Instantiates some temporary datastructures.
     */
    public ProcessDefinitionBuilderImpl() {

        resetingThisBuilder();
    }

    private void resetingThisBuilder() {

        this.startNodes = new ArrayList<Node>();
        this.id = UUID.randomUUID();
        this.name = null;
        this.description = null;
        this.temporaryStartTriggers = new HashMap<ProcessStartEvent, Node>();
        this.temporaryAttributeTable = null;
        this.temporaryInstantiationPatterns = new ArrayList<InstantiationPattern>();
        this.startInstantiationPattern = null;
    }

    @Override
    public ProcessDefinitionBuilder setName(String processName) {

        this.name = processName;
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

        ProcessStartEvent event = new StartEventImpl(eventType, adapterConfig, eventConditions, id);
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
    public ProcessDefinition buildDefinition()
    throws IllegalStarteventException {

        checkingDefinitionConstraints();

        ProcessDefinitionImpl definition = buildResultDefinition();

        // cleanup
        resetingThisBuilder();

        return definition;
    }

    // TODO @gerarodo Commenting
    private ProcessDefinitionImpl buildResultDefinition()
    throws IllegalStarteventException {

        StartInstantiationPattern startInstantionPattern = appendingInstantiationPatterns();

        ProcessDefinitionImpl definition = new ProcessDefinitionImpl(id, name, description, startNodes,
            startInstantionPattern);

        for (Map.Entry<ProcessStartEvent, Node> entry : temporaryStartTriggers.entrySet()) {
            definition.addStartTrigger(entry.getKey(), entry.getValue());
        }

        return definition;
    }

    private StartInstantiationPattern appendingInstantiationPatterns() {

        PatternAppendable<InstantiationPattern> lastInstantiationPattern = this.startInstantiationPattern;
        for (InstantiationPattern instantiationPattern : temporaryInstantiationPatterns) {

            lastInstantiationPattern.setNextPattern(instantiationPattern);
            lastInstantiationPattern = instantiationPattern;
        }

        // Returning the first Pattern
        return this.startInstantiationPattern;
    }

    private void checkingDefinitionConstraints() {

        if (this.startInstantiationPattern == null) {

            String errorMessage = "The first InstantiationPattern should be a StartInstantiationPattern."
                + "Please perfom 'addStartInstantiationPattern(...)'";
            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }
        
        if (!temporaryInstantiationPatterns.isEmpty()) {
            if (this.startInstantiationPattern instanceof PatternAppendable<?>) {
                
            }
        }
    }
}
