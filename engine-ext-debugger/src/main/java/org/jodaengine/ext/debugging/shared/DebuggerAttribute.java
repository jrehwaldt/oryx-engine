package org.jodaengine.ext.debugging.shared;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.ext.debugging.api.Switchable;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.util.Identifiable;

/**
 * This represents a container class, which will be available in the {@link ProcessDefinition}
 * attribute set. A static getter method is provided within this class.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
public class DebuggerAttribute implements Switchable, Identifiable<UUID> {
    
    protected static final String ATTRIBUTE_KEY = "extension-debugger-attribute";
    
    private boolean enabled;
    private final UUID id;
    
    private String svgArtifact;
    
    /**
     * Creates a new instance. Debugging is disabled by default.
     */
    public DebuggerAttribute() {
        this.id = UUID.randomUUID();
        this.svgArtifact = null;
        disable();
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
    
    /**
     * Sets the svg artifact resource name.
     * 
     * @param svgArtifact the svg artifact resource name
     */
    public void setSvgArtifact(@Nullable String svgArtifact) {
        this.svgArtifact = svgArtifact;
    }
    
    /**
     * Returns the svg artifact resource name.
     * 
     * @return the svg artifact resource name
     */
    public @Nullable String getSvgArtifact() {
        return this.svgArtifact;
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
    
    /**
     * Returns a {@link DebuggerAttribute} instance related to the provided
     * {@link ProcessDefinition}. If none exists, null is returned.
     * 
     * @param definition the {@link ProcessDefinition}, the attribute is related to
     * @return an attribute instance, null if none provided
     */
    public static @Nullable DebuggerAttribute getAttributeIfExists(@Nonnull ProcessDefinition definition) {
        
        return (DebuggerAttribute) definition.getAttribute(ATTRIBUTE_KEY);
    }
}
