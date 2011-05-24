package org.jodaengine.ext.debugging;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * This represents a container class for a breakpoint, which will be available in the
 * {@link Node} attribute set. Static getter methods are provided within this class.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
public class Breakpoint extends DebuggerAttribute {
    
    private final Node node;
    
    /**
     * Default constructor. Creates a new breakpoint, which will be enabled by default.
     * 
     * @param node the node, this breakpoint is bound to
     */
    protected Breakpoint(@Nonnull Node node) {
        super();
        this.node = node;
        enable();
    }
    
    /**
     * Returns the node this breakpoint is bound to.
     * 
     * @return a node
     */
    public @Nonnull Node getNode() {
        return node;
    }

    /**
     * Checks whether the breakpoint will match the current token state.
     * 
     * @param token the token
     * @return a boolean, whether this token is matched by the corresponding breakpoint
     */
    public boolean matches(@Nonnull Token token) {
        
        if (!this.isEnabled()) {
            return false;
        }
        
        if (token.getCurrentNode().equals(this.node)) {
            return true;
        }
        
        return false;
    }

    
    /**
     * Returns a {@link Breakpoint} instance related to the provided
     * {@link Node}. If none exists, a new one is created and associated
     * with the definition.
     * 
     * Default breakpoints will be enabled.
     * 
     * @param node the {@link Node}, the attribute is related to
     * @return an attribute instance, null if none provided
     */
    public static @Nonnull Breakpoint getAttribute(@Nonnull Node node) {
        
        Breakpoint breakpoint = getAttributeIfExists(node);
        
        //
        // register a new instance
        //
        if (breakpoint == null) {
            breakpoint = new Breakpoint(node);
            node.setAttribute(ATTRIBUTE_KEY, breakpoint);
        }
        
        return breakpoint;
    }
    
    /**
     * Returns a {@link Breakpoint} instance related to the provided
     * {@link Node}. If none exists, null is returned.
     * 
     * @param node the {@link Node}, the attribute is related to
     * @return an attribute instance, null if none provided
     */
    public static @Nullable Breakpoint getAttributeIfExists(@Nonnull Node node) {
        
        return (Breakpoint) node.getAttribute(ATTRIBUTE_KEY);
    }
}
