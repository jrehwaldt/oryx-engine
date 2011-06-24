package org.jodaengine.process.token;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;

/**
 * The Interface TokenBuilder. The TokenBuilder is used in the Process Instance, to generate a specific kind of token.
 * With this builder, the instance doesn't know what kind of Token was just created, it just knows it was of type Token.
 * The Builder must be created when creating the process instance.
 */
public interface TokenBuilder {
    
    /**
     * Creates the {@link Token}.
     *
     * @param startNode the starting node
     * @param parentToken the parent token
     * @return the token
     */
    Token create(@Nonnull Node startNode, @Nullable Token parentToken);
    
    /**
     * Sets the nav.
     *
     * @param nav the nav
     * @return the token builder
     */
    TokenBuilder setNav(Navigator nav);
    
    /**
     * Sets the node.
     *
     * @param node the node
     * @return the token builder
     */
    TokenBuilder setNode(Node node);
    
    /**
     * Sets the instance.
     *
     * @param instance the instance
     * @return the token builder
     */
    TokenBuilder setInstance(AbstractProcessInstance instance);
    
    /**
     * Gets the node.
     *
     * @return the node
     */
    Node getNode();
    
    /**
     * Gets the nav.
     *
     * @return the nav
     */
    Navigator getNav();
    
    /**
     * Gets the single instance of TokenBuilder.
     *
     * @return single instance of TokenBuilder
     */
    AbstractProcessInstance getInstance();
    

}
