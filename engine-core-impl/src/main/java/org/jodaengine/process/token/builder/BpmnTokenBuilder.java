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
public class BpmnTokenBuilder implements TokenBuilder {
    
    private Navigator nav;
    private Node node;
    private AbstractProcessInstance instance;
    
    /**
     * Instantiates a new bpmn token builder.
     *
     * @param nav the navigator
     * @param node the node
     */
    public BpmnTokenBuilder(Navigator nav, Node node) {

        this.nav = nav;
        this.node = node;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractProcessInstance getInstance() {
    
        return instance;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public TokenBuilder setInstance(AbstractProcessInstance instance) {
    
        this.instance = instance;
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Navigator getNav() {
    
        return nav;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public TokenBuilder setNav(Navigator nav) {
    
        this.nav = nav;
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Node getNode() {
    
        return node;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public TokenBuilder setNode(Node node) {
    
        this.node = node;
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Token create() {
        return new BpmnToken(node, instance, nav);
    }

}
