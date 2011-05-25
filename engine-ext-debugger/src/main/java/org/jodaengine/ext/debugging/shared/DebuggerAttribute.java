package org.jodaengine.ext.debugging.shared;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.util.Identifiable;

/**
 * This represents a container class, which will be available in the {@link ProcessDefinition}
 * attribute set. A static getter method is provided within this class.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
public class DebuggerAttribute implements Identifiable<UUID> {
    
    protected static final String ATTRIBUTE_KEY = "extension-debugger-attribute";
    
    private boolean enabled;
    private final UUID id;
    
    /**
     * Creates a new instance. Debugging is disabled by default.
     */
    public DebuggerAttribute() {
        this.id = UUID.randomUUID();
        disable();
    }
    
    @Override
    public UUID getID() {
        return this.id;
    }
    
    /**
     * Disable debugging for this debugging element.
     */
    public void disable() {
        this.enabled = false;
    }
    
    /**
     * Enable debugging for this debugging element.
     */
    public void enable() {
        this.enabled = true;
    }
    
    /**
     * Returns true if this debugging element is enabled.
     * 
     * @return true, if debugging is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    
    
    /**
     * Returns a {@link DebuggerAttribute} instance related to the provided
     * {@link ProcessDefinition}. If none exists, a new one is created and associated
     * with the definition.
     * 
     * Default instances will have debugging disabled.
     * 
     * @param definition the {@link ProcessDefinition}, the attribute is related to
     * @return an attribute instance, creates a new one if none exists
     */
    public static @Nonnull DebuggerAttribute getAttribute(@Nonnull ProcessDefinition definition) {
        
        DebuggerAttribute attribute = (DebuggerAttribute) definition.getAttribute(ATTRIBUTE_KEY);
        
        //
        // register a new instance
        //
        if (attribute == null) {
            attribute = new DebuggerAttribute();
            definition.setAttribute(ATTRIBUTE_KEY, attribute);
        }
        
        return attribute;
    }
}
