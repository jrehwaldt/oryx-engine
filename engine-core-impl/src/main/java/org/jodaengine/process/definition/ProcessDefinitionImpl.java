package org.jodaengine.process.definition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import org.jodaengine.correlation.CorrelationManager;
import org.jodaengine.correlation.registration.StartEvent;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instantiation.InstantiationPatternContext;
import org.jodaengine.process.instantiation.InstantiationPatternContextImpl;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.process.instantiation.StartNullInstantiationPattern;
import org.jodaengine.process.structure.Node;


/**
 * The Class ProcessDefinitionImpl. A process definition consists of a list of start nodes that, as we have a tree
 * structure of nodes, reference all the nodes of the definition transitively.
 */
public class ProcessDefinitionImpl implements ProcessDefinition, ProcessDefinitionInside {

    private String name;

    private String description;

    private UUID id;

    private List<Node> startNodes;

    @JsonIgnore
    private StartInstantiationPattern startInstantiationPattern;

    @JsonIgnore
    private Map<StartEvent, Node> startTriggers;

    private Map<String, Object> attributes;

    /**
     * Instantiates a new {@link ProcessDefinition}. The name is the ID of the {@link ProcessDefinition}.
     * 
     * @param id
     *            - the internal of the {@link ProcessDefinition}
     * @param name
     *            - the name of the {@link ProcessDefinition}
     * @param description
     *            - the description of the {@link ProcessDefinition}
     * @param startNodes
     *            - the initial nodes that refer to the whole node-tree
     */
    public ProcessDefinitionImpl(UUID id, String name, String description, List<Node> startNodes) {

        this(id, name, description, startNodes, new StartNullInstantiationPattern());
    }

    /**
     * Hidden constructor.
     */
    protected ProcessDefinitionImpl() {

    }

    /**
     * Instantiates a new process definition. A UUID is generated randomly.
     * 
     * @param id
     *            - the internal of the {@link ProcessDefinition}
     * @param name
     *            - the name of the {@link ProcessDefinition}
     * @param description
     *            - the description of the {@link ProcessDefinition}
     * @param startNodes
     *            - the initial nodes that refer to the whole node-tree
     */
    public ProcessDefinitionImpl(UUID id,
                                 String name,
                                 String description,
                                 List<Node> startNodes,
                                 StartInstantiationPattern startInstantiationPattern) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.startNodes = startNodes;
        this.startTriggers = new HashMap<StartEvent, Node>();
        this.startInstantiationPattern = startInstantiationPattern;
    }

    @Override
    public UUID getID() {

        return id;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public void setName(String name) {

        this.name = name;

    }

    @Override
    public String getDescription() {

        return description;
    }

    @Override
    public void setDescription(String description) {

        this.description = description;
    }

    @Override
    public List<Node> getStartNodes() {

        return startNodes;
    }

    @Override
    public Map<StartEvent, Node> getStartTriggers() {

        return startTriggers;
    }

    @Override
    public void addStartTrigger(StartEvent event, Node node)
    throws IllegalStarteventException {

        if (startNodes.contains(node)) {
            startTriggers.put(event, node);
        } else {
            throw new IllegalStarteventException();
        }

    }

    @JsonProperty
    @Override
    public Map<String, Object> getAttributes() {

        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>();
        }
        return this.attributes;
    }

    @Override
    public Object getAttribute(String attributeKey) {

        return getAttributes().get(attributeKey);
    }

    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {

        getAttributes().put(attributeKey, attributeValue);
    }

    @Override
    public AbstractProcessInstance createProcessInstance(NavigatorInside navigator) {

        InstantiationPatternContext patternContext = new InstantiationPatternContextImpl(this);
        return startInstantiationPattern.createProcessInstance(patternContext);

        // AbstractProcessInstance instance = new ProcessInstanceImpl(this);
        //
        // for (Node node : this.getStartNodes()) {
        // Token newToken = instance.createToken(node, navigator);
        // navigator.addWorkToken(newToken);
        // }
        // return instance;
    }

    @Override
    public void activate(CorrelationManager correlationManager) {

        // TODO: Auslagern in eine Strategy
        for (StartEvent event : this.getStartTriggers().keySet()) {
            correlationManager.registerStartEvent(event);
        }
    }

    @Override
    public void deactivate(CorrelationManager correlationManager) {

        // TODO Auto-generated method stub

    }
}
