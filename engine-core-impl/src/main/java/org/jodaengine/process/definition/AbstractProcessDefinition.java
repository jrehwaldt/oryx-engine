package org.jodaengine.process.definition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.exception.DefinitionNotActivatedException;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.activation.ProcessDeActivationPattern;
import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContext;
import org.jodaengine.process.activation.ProcessDefinitionActivationPatternContextImpl;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instantiation.InstantiationPatternContext;
import org.jodaengine.process.instantiation.InstantiationPatternContextImpl;
import org.jodaengine.process.instantiation.ProcessInstantiationPattern;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.process.structure.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This represents the base class for the a {@link ProcessDefinition}. A {@link ProcessDefinition} consists of a list of
 * start nodes that, as we have a tree structure of nodes, reference all the nodes of the definition transitively.
 */
public abstract class AbstractProcessDefinition implements ProcessDefinitionInside {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String name;

    protected String description;

    protected ProcessDefinitionID id;

    protected List<Node> startNodes;

    protected Map<String, Object> attributes;

    @JsonIgnore
    protected StartInstantiationPattern firstInstantiationPattern;

    @JsonIgnore
    protected ProcessDeActivationPattern firstActivationPattern;

    protected boolean activated = false;

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
     *            - the {@link ProcessInstantiationPattern} that is responsilbe for creating a process instance
     * @param startActivationPattern
     *            - the {@link ProcessDeActivationPattern} that is responsible for the activating and
     *            deactivating the {@link ProcessDefinition}
     */
    protected AbstractProcessDefinition(ProcessDefinitionID id,
                                        String name,
                                        String description,
                                        List<Node> startNodes,
                                        StartInstantiationPattern startInstantiationPattern,
                                        ProcessDeActivationPattern startActivationPattern) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.startNodes = startNodes;
        this.firstInstantiationPattern = startInstantiationPattern;
        this.firstActivationPattern = startActivationPattern;
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
    
    @Override
    public boolean isActivated() {

        return activated;
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
    public AbstractProcessInstance createProcessInstance(NavigatorInside navigator) throws DefinitionNotActivatedException {

        if (!activated) {
            String errorMessage = "The processdefintion (id: " + id + "; name: " + name
                + ") is not activated. Please perform navigatorService.activateProcessDefinition(...) .";
            logger.error(errorMessage);
            throw new DefinitionNotActivatedException(getID());
        }
        
        InstantiationPatternContext patternContext = new InstantiationPatternContextImpl(this);
        return firstInstantiationPattern.createProcessInstance(patternContext);
    }
    
    @Override
    public AbstractProcessInstance createProcessInstance(NavigatorInside navigator, ProcessStartEvent firedStartEvent) {
    
        InstantiationPatternContext patternContext = new InstantiationPatternContextImpl(this, firedStartEvent);
        return firstInstantiationPattern.createProcessInstance(patternContext);
    }

    @Override
    public void activate(EventSubscriptionManager correlationManager) {

        ProcessDefinitionActivationPatternContext patternContext = new ProcessDefinitionActivationPatternContextImpl(
            this);
        firstActivationPattern.activateProcessDefinition(patternContext);

        this.activated = true;
    }

    @Override
    public void deactivate(EventSubscriptionManager correlationManager) {

        ProcessDefinitionActivationPatternContext patternContext = new ProcessDefinitionActivationPatternContextImpl(
            this);
        firstActivationPattern.deactivateProcessDefinition(patternContext);

        this.activated = false;
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
