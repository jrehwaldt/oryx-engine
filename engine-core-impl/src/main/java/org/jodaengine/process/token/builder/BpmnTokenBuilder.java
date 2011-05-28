package org.jodaengine.process.token.builder;

import javax.annotation.Nullable;

import org.jodaengine.ext.service.ExtensionService;
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
    private ExtensionService extensionService;
    private Node node;
    private AbstractProcessInstance instance;
    
    /**
     * Instantiates a new {@link BpmnToken} builder.
     *
     * @param nav the navigator
     * @param extensionService the {@link ExtensionService}
     * @param node the node
     */
    public BpmnTokenBuilder(Navigator nav,
                            @Nullable ExtensionService extensionService,
                            Node node) {
        
        this.nav = nav;
        this.extensionService = extensionService;
        this.node = node;
    }
    
    // TODO Jannik... die hälfte aller Methoden hier drin ist sinnlos oder nur von tests verwendet.
    //      Alle Getter wegschmeißen.
    
    @Override
    public AbstractProcessInstance getInstance() {
    
        return instance;
    }
    
    @Override
    public TokenBuilder setInstance(AbstractProcessInstance instance) {
    
        this.instance = instance;
        return this;
    }
    
    @Override
    public Navigator getNav() {
    
        return nav;
    }
    
    @Override
    public TokenBuilder setNav(Navigator nav) {
    
        this.nav = nav;
        return this;
    }
    
    @Override
    public Node getNode() {
    
        return node;
    }
    
    @Override
    public TokenBuilder setNode(Node node) {
    
        this.node = node;
        return this;
    }
    
    @Override
    public Token create() {
        return new BpmnToken(node, instance, nav, this.extensionService);
    }

}
