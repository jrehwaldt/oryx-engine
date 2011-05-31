package org.jodaengine.ext.debugging.shared;

import java.util.UUID;

import javax.annotation.Nonnull;

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
    
// CHECKSTYLE:OFF
    @Override
    public int hashCode() {
        return this.id.hashCode()
            + 7  * this.node.hashCode()
            + 11 * (this.condition != null ? this.condition.hashCode() : 0);
    }
// CHECKSTYLE:ON
    
    /**
     * Two breakpoints are equal to each other, if they both implement
     * the {@link Breakpoint} interface, and have equal {@link UUID} and {@link Node}.
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        
        //
        // will never be equal to null
        //
        if (object == null) {
            return false;
        }
        
        //
        // or to a non-Breakpoint instance
        //
        if (object instanceof Breakpoint) {
            Breakpoint breakpoint = (Breakpoint) object;
            
            //
            // same id
            //
            if (!this.getID().equals(breakpoint.getID())) {
                return false;
            }
            
            //
            // same node
            //
            if (!this.getNode().equals(breakpoint.getNode())) {
                return false;
            }
            
            //
            // same condition (both null or equal)
            //
            if (this.getCondition() != null && breakpoint.getCondition() != null) {
                if (this.getCondition().equals(breakpoint.getCondition())) {
                    return true;
                }
            } else if (this.getCondition() == null && breakpoint.getCondition() == null) {
                return true;
            }
        }
        
        return false;
    }
}
