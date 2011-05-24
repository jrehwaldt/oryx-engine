package org.jodaengine.process.instance;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BpmnTokenImpl;

/**
 * The Class BpmnProcessInstance.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public class BpmnProcessInstance extends AbstractProcessInstanceImpl<BpmnTokenImpl> {

    protected BpmnProcessInstance() {
        
    }
    public BpmnProcessInstance(ProcessDefinition definition) {
        super(definition);
    }
    
    @Override
    public BpmnTokenImpl createNewToken(Node node, Navigator nav) {

        BpmnTokenImpl token = new BpmnTokenImpl(node, this, nav);
        this.addToken(token);
        return token;
    }

}
