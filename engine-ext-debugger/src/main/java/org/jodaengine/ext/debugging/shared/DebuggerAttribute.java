package org.jodaengine.ext.debugging.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.ext.debugging.api.Breakpoint;
import org.jodaengine.ext.debugging.api.Switchable;
import org.jodaengine.util.Attributable;
import org.jodaengine.util.Identifiable;

/**
 * This represents a container class, which will be available in the {@link ProcessDefinition}
 * attribute set. A static getter method is provided within this class.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public class DebuggerAttribute implements Switchable, Identifiable<UUID>, Serializable {
    private static final long serialVersionUID = 8824388848972605551L;
    
    protected static final String ATTRIBUTE_KEY = "extension-debugger-attribute";
    
    private boolean enabled;
    private final UUID id;
    private final List<Breakpoint> breakpoints;
    
    private String svgArtifact;
    
    /**
     * Creates a new instance. Debugging is disabled by default.
     */
    public DebuggerAttribute() {
        this.id = UUID.randomUUID();
        this.breakpoints = new ArrayList<Breakpoint>();
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
    @JsonProperty
    public @Nullable String getSvgArtifact() {
        return this.svgArtifact;
    }
    
    /**
     * Adds a {@link Breakpoint}.
     * 
     * @param breakpoint the {@link Breakpoint}
     */
    public void addBreakpoint(@Nonnull Breakpoint breakpoint) {
        this.breakpoints.add(breakpoint);
    }
    
    /**
     * Removes a {@link Breakpoint}.
     * 
     * @param breakpoint the {@link Breakpoint}
     */
    public void removeBreakpoint(@Nonnull Breakpoint breakpoint) {
        this.breakpoints.remove(breakpoint);
    }
    
    /**
     * Returns the {@link Breakpoint}s.
     * 
     * @return the {@link Breakpoint}s
     */
    @JsonProperty
    public Collection<Breakpoint> getBreakpoints() {
        return this.breakpoints;
    }
    
    
    
    /**
     * Returns a {@link DebuggerAttribute} instance related to the provided
     * {@link ProcessDefinition}. If none exists, a new one is created and associated
     * with the definition.
     * 
     * Default instances will have debugging disabled.
     * 
     * @param definitionAttributes the {@link Attributable}, the attribute is related to
     * @return an attribute instance, creates a new one if none exists
     */
    public static @Nonnull DebuggerAttribute getAttribute(@Nonnull Attributable definitionAttributes) {
        
        DebuggerAttribute attribute = (DebuggerAttribute) definitionAttributes.getAttribute(ATTRIBUTE_KEY);
        
        //
        // register a new instance
        //
        if (attribute == null) {
            attribute = new DebuggerAttribute();
            definitionAttributes.setAttribute(ATTRIBUTE_KEY, attribute);
        }
        
        return attribute;
    }
    
    /**
     * Returns a {@link DebuggerAttribute} instance related to the provided
     * {@link ProcessDefinition}. If none exists, null is returned.
     * 
     * @param definitionAttributes the {@link Attributable}, the attribute is related to
     * @return an attribute instance, null if none provided
     */
    public static @Nullable DebuggerAttribute getAttributeIfExists(@Nonnull Attributable definitionAttributes) {
        
        return (DebuggerAttribute) definitionAttributes.getAttribute(ATTRIBUTE_KEY);
    }
}
