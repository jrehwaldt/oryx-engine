package org.jodaengine.process.definition.petri;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.eventmanagement.processevent.incoming.StartProcessEvent;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.activation.pattern.NullProcessDefinitionActivationPattern;
import org.jodaengine.process.definition.AbstractProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.instantiation.pattern.StartNullInstantiationPattern;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.process.token.builder.PetriTokenBuilder;

/**
 * The Class PetriNetProcessDefinition. This is the process definition for petri nets.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public class PetriNetProcessDefinition extends AbstractProcessDefinition {

    /**
     * Instantiates a new {@link ProcessDefinition} for PetriNets. The name is the ID of the {@link ProcessDefinition}.
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
    public PetriNetProcessDefinition(ProcessDefinitionID id, String name, String description, List<Node> startNodes) {

        super(id, name, description, startNodes, new StartNullInstantiationPattern(),
            new NullProcessDefinitionActivationPattern());
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

    // TODO Bleiben erstmal un implementiert werden nach der BA aus dem Interface entfernt
    @Override
    public Map<StartProcessEvent, Node> getStartTriggers() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void addStartTrigger(StartProcessEvent event, Node node)
        throws IllegalStarteventException {
        // TODO Auto-generated method stub
        
    }
}
