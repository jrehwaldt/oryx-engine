package org.jodaengine.process.definition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instantiation.InstantiationPatternContext;
import org.jodaengine.process.instantiation.InstantiationPatternContextImpl;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.process.instantiation.StartNullInstantiationPattern;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BPMNToken;



/**
 * The Class ProcessDefinitionImpl. A process definition consists of a list of start nodes that, as we have a tree
 * structure of nodes, reference all the nodes of the definition transitively.
 */
public class ProcessDefinitionImpl implements ProcessDefinition, ProcessDefinitionInside {

    private String name;

    private String description;

    private ProcessDefinitionID id;

    private List<Node<BPMNToken>> startNodes;

    @JsonIgnore
    private StartInstantiationPattern startInstantiationPattern;

    @JsonIgnore
    private Map<ProcessStartEvent, Node<BPMNToken>> startTriggers;

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
    public ProcessDefinitionImpl(ProcessDefinitionID id,
                                 String name,
                                 String description,
                                 List<Node<BPMNToken>> startNodes) {

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
     * @param id - the internal of the {@link ProcessDefinition}
     * @param name - the name of the {@link ProcessDefinition}
     * @param description - the description of the {@link ProcessDefinition}
     * @param startNodes - the initial nodes that refer to the whole node-tree
     * @param startInstantiationPattern the start instantiation pattern
     */
    public ProcessDefinitionImpl(ProcessDefinitionID id,
                                 String name,
                                 String description,
                                 List<Node<BPMNToken>> startNodes,
                                 StartInstantiationPattern startInstantiationPattern) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.startNodes = startNodes;
        this.startTriggers = new HashMap<ProcessStartEvent, Node<BPMNToken>>();
        this.startInstantiationPattern = startInstantiationPattern;
    }

    @Override
    public ProcessDefinitionID getID() {

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
    public List<Node<BPMNToken>> getStartNodes() {

        return startNodes;
    }

    @Override
    public Map<ProcessStartEvent, Node<BPMNToken>> getStartTriggers() {

        return startTriggers;
    }

    @Override
    public void addStartTrigger(ProcessStartEvent event, Node<BPMNToken> node)
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
    public AbstractProcessInstance<BPMNToken> createProcessInstance(NavigatorInside navigator) {

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
    public void activate(EventSubscriptionManager correlationManager) {

        // TODO: Auslagern in eine Strategy
        for (ProcessStartEvent event : this.getStartTriggers().keySet()) {
            correlationManager.registerStartEvent(event);
        }
    }

    @Override
    public void deactivate(EventSubscriptionManager correlationManager) {

        // TODO Auto-generated method stub

    }
}
