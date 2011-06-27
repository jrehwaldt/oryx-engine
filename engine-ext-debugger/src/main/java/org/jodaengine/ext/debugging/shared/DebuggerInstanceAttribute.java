package org.jodaengine.ext.debugging.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.ext.debugging.api.DebuggerCommand;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.Identifiable;

/**
 * This represents a container class, which will be available in the {@link Token}
 * attribute set. A static getter method is provided within this class.
 * 
 * @author Jan Rehwaldt
 * @since 2011-06-14
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public class DebuggerInstanceAttribute implements Identifiable<UUID>, Serializable {
    private static final long serialVersionUID = 1868155851442693148L;

    protected static final String ATTRIBUTE_KEY = "extension-debugger-attribute";
    
    private UUID id;
    
    private Map<UUID, DebuggerCommand> commands;
    private List<PathHistoryEntry> pathHistory;
    
    /**
     * Creates a new instance.
     */
    public DebuggerInstanceAttribute() {
        this.id = UUID.randomUUID();
        this.commands = new HashMap<UUID, DebuggerCommand>();
        this.pathHistory = new LinkedList<PathHistoryEntry>();
    }
    
    @Override
    public UUID getID() {
        return this.id;
    }
    
    /**
     * Gets the {@link DebuggerCommand} for the current {@link Token}.
     * 
     * If no command for the current token is registered, a parental command is evaluated.
     * 
     * @param token the token, the command is bound to
     * @return the command
     */
    public synchronized @Nullable DebuggerCommand getCommand(Token token) {
        
        //
        // get command for THIS token
        //
        DebuggerCommand command = this.commands.get(token.getID());
//        
//        //
//        // if not provided, get from any parent
//        //
//        while (command == null && token.getParentToken() != null) {
//            token = token.getParentToken();
//            command = this.commands.get(token.getID());
//        }
        
        return command;
    }
    
    /**
     * Sets the {@link DebuggerCommand} for the current {@link Token}.
     * 
     * A command is also set for parental tokens, as it needs to be available after an AND-join.
     * 
     * @param token the token, this command is bound to
     * @param command the command
     */
    public synchronized void setCommand(@Nonnull Token token,
                                        @Nullable DebuggerCommand command) {
        
        this.commands.put(token.getID(), command);
        
//        //
//        // set the command for THIS token and for ALL parents
//        //
//        while (token.getParentToken() != null) {
//            token = token.getParentToken();
//            this.commands.put(token.getID(), command);
//        }
    }
    
    /**
     * Provides access to all commands registered for any token.
     * 
     * @return all registered commands
     */
    @JsonProperty
    public synchronized @Nonnull Map<UUID, DebuggerCommand> getCommands() {
        
        return this.commands;
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
    @JsonProperty
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
//  * 
//  * Important is, that the {@link DebuggerInstanceAttribute} is bound to the most parental token.
    public static @Nonnull DebuggerInstanceAttribute getAttribute(@Nonnull Token token) {
        
//        //
//        // get the most parental token
//        //
//        while (token.getParentToken() != null) {
//            token = token.getParentToken();
//        }
        
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
    
    /**
     * Registered the provided attribute within a token.
     * 
     * @param attribute the attribute to register
     * @param token the token
     */
    public static void setAttribute(@Nonnull DebuggerInstanceAttribute attribute,
                                    @Nonnull Token token) {
        
        token.setAttribute(ATTRIBUTE_KEY, attribute);
    }
    
    /**
     * Returns true if a attribute is already registered for this token.
     * 
     * @param token the token
     * @return true, if attribute is available
     */
    public static boolean hasAttribute(@Nonnull Token token) {
        
        return token.getAttribute(ATTRIBUTE_KEY) != null;
    }
}

