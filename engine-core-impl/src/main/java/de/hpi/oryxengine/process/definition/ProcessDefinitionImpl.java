package de.hpi.oryxengine.process.definition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.structure.Node;

/**
 * The Class ProcessDefinitionImpl. A process definition consists of a list of start nodes that, as we have a tree
 * structure of nodes, reference all the nodes of the definition transitively.
 */
public class ProcessDefinitionImpl implements ProcessDefinition {

    private String name;

    private String description;

    private UUID id;

    private List<Node> startNodes;

    @JsonIgnore
    private Map<StartEvent, Node> startTriggers;
    
    private Map<String, Object> attributes;

    /**
     * Instantiates a new {@link ProcessDefinition}. The name is the ID of the {@link ProcessDefinition}.
     * 
     * @param id
     *            - the internal of the {@link ProcessDefinition}
     * @param description
     *            - the description of the {@link ProcessDefinition}
     * @param startNodes
     *            - the initial nodes that refer to the whole node-tree
     */
    public ProcessDefinitionImpl(UUID id, String description, List<Node> startNodes) {

        this(id, id.toString(), description, startNodes);
    }
    
    /**
     * Hidden constructor.
     */
    protected ProcessDefinitionImpl() { }
    
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
    public ProcessDefinitionImpl(UUID id, String name, String description, List<Node> startNodes) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.startNodes = startNodes;
        this.startTriggers = new HashMap<StartEvent, Node>();
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

}
