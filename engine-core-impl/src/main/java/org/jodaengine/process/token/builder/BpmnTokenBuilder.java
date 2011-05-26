package org.jodaengine.process.token.builder;

import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;

/**
 * The Class BpmnTokenBuilder. A specific Token Builder which creates BPMN Tokens.
 */
public class BpmnTokenBuilder implements TokenBuilder{
    
    private Navigator nav;
    private Node node;
    private AbstractProcessInstance instance;
    
    public BpmnTokenBuilder(Navigator nav, Node node) {

        this.nav = nav;
        this.node = node;
    }
    public AbstractProcessInstance getInstance() {
    
        return instance;
    }
    public TokenBuilder setInstance(AbstractProcessInstance instance) {
    
        this.instance = instance;
        return this;
    }
    public Navigator getNav() {
    
        return nav;
    }
    public TokenBuilder setNav(Navigator nav) {
    
        this.nav = nav;
        return this;
    }
    public Node getNode() {
    
        return node;
    }
    public TokenBuilder setNode(Node node) {
    
        this.node = node;
        return this;
    }
    
    public Token create() {
        return new BpmnToken(node, instance, nav);
    }

}
