package org.jodaengine.ext.debugging.shared;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.ext.debugging.api.DebuggerCommand;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.token.Token;

/**
 * This represents a container class, which will be available in the {@link Token}
 * attribute set. A static getter method is provided within this class.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-14
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public class DebuggerInstanceAttribute {
    
    protected static final String ATTRIBUTE_KEY = "extension-debugger-attribute";
    
    private DebuggerCommand activeCommand;
    private List<PathHistoryEntry> pathHistory;
    
    /**
     * Creates a new instance.
     */
    public DebuggerInstanceAttribute() {
        this.activeCommand = null;
        this.pathHistory = new LinkedList<PathHistoryEntry>();
    }
    
    /**
     * Clears the active {@link DebuggerCommand} and returns it's previous value.
     * 
     * @return the active command (which was cleared)
     */
    @JsonIgnore
    public synchronized @Nullable DebuggerCommand clearActiveCommand() {
        DebuggerCommand command = this.activeCommand;
        this.activeCommand = null;
        return command;
    }
    
    /**
     * Sets the active {@link DebuggerCommand}.
     * 
     * @param activeCommand the active command
     */
    public synchronized void setActiveCommand(@Nullable DebuggerCommand activeCommand) {
        this.activeCommand = activeCommand;
    }
    
    /**
     * Returns the active {@link DebuggerCommand}.
     * 
     * @return the active command
     */
    @JsonProperty
    protected @Nullable DebuggerCommand getActiveCommand() {
        return this.activeCommand;
    }
    
    /**
     * Registers a new {@link ControlFlow} to keep track of the chosen path.
     * 
     * @param path the {@link ControlFlow}, which was executed
     * @param token the processing {@link Token}
     */
    public void addPreviousPath(@Nonnull ControlFlow path,
                                @Nonnull Token token) {
        this.pathHistory.add(new PathHistoryEntry(path, token));
    }
    
    /**
     * Returns a full {@link ControlFlow} history of the chosen paths.
     * 
     * @return a history of chosen paths
     */
    public @Nonnull List<PathHistoryEntry> getFullPath() {
        
        return this.pathHistory;
    }
    
    /**
     * Returns a {@link DebuggerInstanceAttribute} instance related to the provided
     * {@link Token}. If none exists, a new one is created and associated
     * with the token.
     * 
     * @param token the {@link Token}, the attribute is related to
     * @return an attribute instance, creates a new one if none exists
     */
    public static @Nonnull DebuggerInstanceAttribute getAttribute(@Nonnull Token token) {
        
        DebuggerInstanceAttribute attribute = (DebuggerInstanceAttribute) token.getAttribute(ATTRIBUTE_KEY);
        
        //
        // register a new instance
        //
        if (attribute == null) {
            attribute = new DebuggerInstanceAttribute();
            token.setAttribute(ATTRIBUTE_KEY, attribute);
        }
        
        return attribute;
    }
//    
//    /**
//     * Returns a {@link DebuggerInstanceAttribute} instance related to the provided
//     * {@link Token}. If none exists, null is returned.
//     * 
//     * @param token the {@link Token}, the attribute is related to
//     * @return an attribute instance, null if none provided
//     */
//    public static @Nullable DebuggerInstanceAttribute getAttributeIfExists(@Nonnull Token token) {
//        
//        return (DebuggerInstanceAttribute) token.getAttribute(ATTRIBUTE_KEY);
//    }
}

