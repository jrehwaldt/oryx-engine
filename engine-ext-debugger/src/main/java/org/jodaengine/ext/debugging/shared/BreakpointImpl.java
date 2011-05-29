package org.jodaengine.ext.debugging.shared;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.BreakpointCondition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * This represents a container class for a {@link Breakpoint}, which will be available in the
 * {@link Node} attribute set. Static getter methods are provided within this class.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
public class BreakpointImpl implements Breakpoint {
    
    protected static final String ATTRIBUTE_KEY = "extension-debugger-breakpoint-attribute";
    
    private final UUID id;
    
    private final Node node;
    private BreakpointCondition condition;
    
    private boolean enabled;
    
    /**
     * Default constructor. Creates a new {@link Breakpoint}, which will be enabled by default
     * and bound to a specified {@link Node}.
     * 
     * @param node the node, this breakpoint is bound to
     */
    public BreakpointImpl(@Nonnull Node node) {
        this.id = UUID.randomUUID();
        this.node = node;
        this.condition = null;
        enable();
    }
    
    @Override
    public UUID getID() {
        return this.id;
    }
    
    @Override
    public void disable() {
        this.enabled = false;
    }
    
    @Override
    public void enable() {
        this.enabled = true;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void setCondition(BreakpointCondition condition) {
        this.condition = condition;
    }

    @Override
    public BreakpointCondition getCondition() {
        return this.condition;
    }

    @Override
    public boolean matches(Token token) {
        
        //
        // is this breakpoint enabled?
        //
        if (!this.isEnabled()) {
            return false;
        }
        
        //
        // does the token's position match (which node?)
        //
        if (token.getCurrentNode().equals(this.node)) {
            
            //
            // no condition attached?
            //
            if (this.condition == null) {
                return true;
            }
            
            //
            // condition attached: does it match?
            //
            if (this.condition.evaluate(token)) {
                return true;
            }
        }
        
        return false;
    }

    
    /**
     * Returns a {@link BreakpointImpl} instance related to the provided
     * {@link Node}. If none exists, a new one is created and associated
     * with the definition.
     * 
     * Default breakpoints will be enabled.
     * 
     * @param node the {@link Node}, the attribute is related to
     * @return an attribute instance, null if none provided
     */
    public static @Nonnull BreakpointImpl getAttribute(@Nonnull Node node) {
        
        BreakpointImpl breakpoint = getAttributeIfExists(node);
        
        //
        // register a new instance
        //
        if (breakpoint == null) {
            breakpoint = new BreakpointImpl(node);
            node.setAttribute(ATTRIBUTE_KEY, breakpoint);
        }
        
        return breakpoint;
    }
    
    /**
     * Returns a {@link BreakpointImpl} instance related to the provided
     * {@link Node}. If none exists, null is returned.
     * 
     * @param node the {@link Node}, the attribute is related to
     * @return an attribute instance, null if none provided
     */
    public static @Nullable BreakpointImpl getAttributeIfExists(@Nonnull Node node) {
        
        return (BreakpointImpl) node.getAttribute(ATTRIBUTE_KEY);
    }
}
