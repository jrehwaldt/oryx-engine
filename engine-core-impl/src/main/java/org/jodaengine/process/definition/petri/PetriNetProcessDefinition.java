package org.jodaengine.process.definition.petri;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.process.token.builder.PetriTokenBuilder;

/**
 * The Class PetriNetProcessDefinition. This is the process definition for petri nets.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public class PetriNetProcessDefinition implements ProcessDefinition, ProcessDefinitionInside {

    private Map<String, Object> attributes;
    
    private String name;

    private String description;

    private ProcessDefinitionID id;

    private List<Node> startNodes;
    
    public PetriNetProcessDefinition(String name,
                                      String description,
                                      ProcessDefinitionID id,
                                      List<Node> startNodes) {

        this.attributes = attributes;
        this.name = name;
        this.description = description;
        this.id = id;
        this.startNodes = startNodes;
    }


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
    public Map<String, Object> getAttributes() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getAttribute(String attributeKey) {

        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public Map<ProcessStartEvent, Node> getStartTriggers() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addStartTrigger(ProcessStartEvent event, Node node)
    throws IllegalStarteventException {

        // TODO Auto-generated method stub
        
    }

    @Override
    public AbstractProcessInstance createProcessInstance(NavigatorInside navigator) {

        TokenBuilder builder = new PetriTokenBuilder(navigator, null);
        AbstractProcessInstance processInstance = new ProcessInstance(this, builder);
        for(Node startNode : startNodes) {
            Token token = processInstance.createToken(startNode);   
            navigator.addWorkToken(token);
        }

        return processInstance;
    }

    @Override
    public void activate(EventSubscriptionManager correlationManager) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public void deactivate(EventSubscriptionManager correlationManager) {

        // TODO Auto-generated method stub
        
    }


    @Override
    public ProcessDefinitionID getID() {

        return id;
    }


    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {

        // TODO Auto-generated method stub
        
    }

}
