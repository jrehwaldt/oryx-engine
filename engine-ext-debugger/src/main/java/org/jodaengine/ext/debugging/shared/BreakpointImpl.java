package org.jodaengine.ext.debugging.shared;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.jodaengine.ext.debugging.api.BreakpointCondition;
import org.jodaengine.ext.debugging.api.NodeBreakpoint;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * This represents a container class for a {@link NodeBreakpoint}, which will be available in the
 * {@link Node} attribute set. Static getter methods are provided within this class.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
public class BreakpointImpl implements NodeBreakpoint {
    
    protected static final String ATTRIBUTE_KEY = "extension-debugger-breakpoint-attribute";
    
    private final UUID id;
    
    private final Node node;
    private final ActivityState state;
    private BreakpointCondition condition;
    
    private boolean enabled;
    
    /**
     * Default constructor. Creates a new {@link Breakpoint}, which will be enabled by default
     * and bound to a specified {@link Node}.
     * 
     * @param node the node, this breakpoint is bound to
     * @param state the state, this breakpoint is bound to
     */
    public BreakpointImpl(@Nonnull Node node,
                          @Nonnull ActivityState state) {
        this.id = UUID.randomUUID();
        this.node = node;
        this.state = state;
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
    public ActivityState getState() {
        return state;
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
        // does the token's position match the breakpoint's node
        //
        if (token.getCurrentNode().equals(this.node)) {
            
            //
            // does the activity state match
            //
            if (!this.state.equals(token.getCurrentActivityState())) {
                return false;
            }
            
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
        if (object instanceof NodeBreakpoint) {
            NodeBreakpoint breakpoint = (NodeBreakpoint) object;
            
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

    @Override
    public String toString() {
        return String.format(
            "BreakpointImpl [%s, State: %s, Node: %s, Condition: %s]",
            getID(),
            getState(),
            getNode(),
            getCondition());
    }
}
