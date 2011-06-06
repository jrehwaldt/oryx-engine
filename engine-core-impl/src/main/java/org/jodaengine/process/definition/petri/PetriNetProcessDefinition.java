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
import org.jodaengine.process.structure.Node;

@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public class PetriNetProcessDefinition implements ProcessDefinition, ProcessDefinitionInside{

    @Override
    public ProcessDefinitionID getID() {

        // TODO Auto-generated method stub
        return null;
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
    public void setAttribute(String attributeKey, Object attributeValue) {

        // TODO Auto-generated method stub
        
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

        // TODO Auto-generated method stub
        return null;
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
    public String getName() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setName(String name) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public String getDescription() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDescription(String description) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Node> getStartNodes() {

        // TODO Auto-generated method stub
        return null;
    }

}
