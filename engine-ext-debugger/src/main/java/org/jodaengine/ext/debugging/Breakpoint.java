package org.jodaengine.ext.debugging;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.Identifiable;

/**
 * This class represents a breakpoint, which is bound to a certain model.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-17
 */
public class Breakpoint implements Identifiable<UUID> {
    
    private final UUID id;
    
    private final @Nonnull Node node;
    private final @Nullable AbstractProcessInstance instance;
    
//    private boolean enabled;
    
    /**
     * Default constructor. Creates a new breakpoint.
     * 
     * @param node the node, this breakpoint is bound to
     * @param instance the instance, this breakpoint is bound to
     */
    protected Breakpoint(@Nonnull Node node,
                         @Nullable AbstractProcessInstance instance) {
        this.id = UUID.randomUUID();
        this.node = node;
        this.instance = instance;
//        this.enabled = true;
    }
    
    @Override
    public UUID getID() {
        return this.id;
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
     * Returns the instance this breakpoint is bound to.
     * 
     * @return a instance
     */
    public @Nullable AbstractProcessInstance getInstance() {
        return instance;
    }
    
//    /**
//     * Deactivates this breakpoint.
//     */
//    protected void disable() {
//        this.enabled = false;
//    }
//    
//    /**
//     * Activates this breakpoint.
//     */
//    protected void enable() {
//        this.enabled = true;
//    }
//    
//    /**
//     * Returns whether this breakpoint is active.
//     * 
//     * @return whether this breakpoint is active
//     */
//    public boolean isEnabled() {
//        return enabled;
//    }
    
    /**
     * Returns whether this has a process instance it is assigned to.
     * 
     * @return whether this has a process instance
     */
    public boolean isBoundToInstance() {
        return this.instance == null;
    }

    /**
     * Checks whether the breakpoint will match the current token state.
     * 
     * @param token the token
     * @return a boolean, whether this token is matched by the corresponding breakpoint
     */
    public boolean matches(@Nonnull Token token) {
        
//        if (!this.enabled) {
//            return false;
//        }
        
        if (token.getCurrentNode().equals(this.node)) {
            if (!isBoundToInstance()) {
                return true;
            } else if (token.getInstance().equals(this.instance)) {
                return true;
            }
        }
        
        return false;
        
    }
    
}
