package org.jodaengine.process.definition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContext;
import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContextImpl;
import org.jodaengine.process.activation.ProcessDefinitionDeActivationPattern;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instantiation.InstantiationPattern;
import org.jodaengine.process.instantiation.InstantiationPatternContext;
import org.jodaengine.process.instantiation.InstantiationPatternContextImpl;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.process.structure.Node;

/**
 * This represents the base class for the a {@link ProcessDefinition}. A {@link ProcessDefinition} consists of a list of
 * start nodes that, as we have a tree structure of nodes, reference all the nodes of the definition transitively.
 */
public abstract class AbstractProcessDefinition implements ProcessDefinitionInside {

    protected String name;

    protected String description;

    protected ProcessDefinitionID id;

    protected List<Node> startNodes;

    protected Map<String, Object> attributes;

    @JsonIgnore
    protected StartInstantiationPattern startInstantiationPattern;

    @JsonIgnore
    protected ProcessDefinitionDeActivationPattern startActivationPattern;

    /**
     * Default instantiation.
     * 
     * @param id
     *            - the internal of the {@link ProcessDefinition}
     * @param name
     *            - the name of the {@link ProcessDefinition}
     * @param description
     *            - the description of the {@link ProcessDefinition}
     * @param startNodes
     *            - the initial nodes that refer to the whole node-tree
     * @param startInstantiationPattern
     *            - the {@link InstantiationPattern} that is responsilbe for creating a process instance
     * @param startActivationPattern
     *            - the {@link ProcessDefinitionDeActivationPattern} that is responsible for the activating and
     *            deactivating the {@link ProcessDefinition}
     */
    protected AbstractProcessDefinition(ProcessDefinitionID id,
                                        String name,
                                        String description,
                                        List<Node> startNodes,
                                        StartInstantiationPattern startInstantiationPattern,
                                        ProcessDefinitionDeActivationPattern startActivationPattern) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.startNodes = startNodes;
        this.startInstantiationPattern = startInstantiationPattern;
        this.startActivationPattern = startActivationPattern;
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
    public List<Node> getStartNodes() {

        return startNodes;
    }

    // @Override
    // public void addStartTrigger(ProcessStartEvent event, Node node)
    // throws IllegalStarteventException {
    //
    // if (startNodes.contains(node)) {
    // startTriggers.put(event, node);
    // } else {
    // throw new IllegalStarteventException();
    // }
    //
    // }

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
    }

    @Override
    public void activate(EventSubscriptionManager correlationManager) {

        ProcessDefinitionActivationPatternContext patternContext = new ProcessDefinitionActivationPatternContextImpl(
            this);
        startActivationPattern.activateProcessDefinition(patternContext);
    }

    @Override
    public void deactivate(EventSubscriptionManager correlationManager) {

        ProcessDefinitionActivationPatternContext patternContext = new ProcessDefinitionActivationPatternContextImpl(
            this);
        startActivationPattern.deactivateProcessDefinition(patternContext);
    }

    @Override
    public int hashCode() {

        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object object) {

        //
        // will never be equal to null
        //
        if (object == null) {
            return false;
        }

        //
        // or to a non-ProcessDefinition instance
        //
        if (object instanceof ProcessDefinition) {
            ProcessDefinition definition = (ProcessDefinition) object;

            //
            // same id
            //
            if (!this.getID().equals(definition.getID())) {
                return false;
            }

            return true;
        }

        return false;
    }
}
