package org.jodaengine.ext.debugging;

import javax.annotation.Nonnull;

import org.jodaengine.process.definition.ProcessDefinition;

/**
 * This represents a container class, which will be available in the {@link ProcessDefinition}
 * attribute set. Static getter methods are provided within this class.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
public final class DebuggerDefinitionAttribute {
    
    public static final String ATTRIBUTE_KEY = "extension-debugger-attribute";
    
    private boolean debuggingEnabled;
    
    /**
     * Creates a new instance. Debugging is disabled by default.
     */
    public DebuggerDefinitionAttribute() {
        this.debuggingEnabled = false;
    }
    
    /**
     * Disable debugging for this {@link ProcessDefinition}.
     */
    public void disable() {
        this.debuggingEnabled = false;
    }
    
    /**
     * Enable debugging for this {@link ProcessDefinition}.
     */
    public void enable() {
        this.debuggingEnabled = true;
    }
    
    /**
     * Returns true if debugging for the provided {@link ProcessDefinition} is enabled.
     * 
     * @return true, if debugging is enabled
     */
    public boolean isDebuggingEnabled() {
        return debuggingEnabled;
    }
    
    
    
    /**
     * Returns a {@link DebuggerDefinitionAttribute} instance related to the provided
     * {@link ProcessDefinition}. If none exists, a new one is created and associated with the
     * definition.
     * 
     * Default instances will have debugging disabled.
     * 
     * @param definition the definition, the attribute is related to
     * @return an attribute instance, creates a new one if none exists
     */
    public static DebuggerDefinitionAttribute getAttribute(@Nonnull ProcessDefinition definition) {
        
        DebuggerDefinitionAttribute attribute = (DebuggerDefinitionAttribute) definition.getAttribute(ATTRIBUTE_KEY);
        
        //
        // register a new instance
        //
        if (attribute == null) {
            attribute = new DebuggerDefinitionAttribute();
            definition.setAttribute(ATTRIBUTE_KEY, attribute);
        }
        
        return attribute;
    }
}
