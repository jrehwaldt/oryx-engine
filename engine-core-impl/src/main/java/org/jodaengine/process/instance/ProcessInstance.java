package org.jodaengine.process.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;


/**
 * The Class ProcessInstance.
 */
public class ProcessInstance extends AbstractProcessInstance {

    protected ProcessDefinition definition;
    protected ProcessInstanceContext context;
    protected UUID id;
    protected List<Token> assignedTokens;

    //should be ignored for serialization
    @JsonIgnore
    protected TokenBuilder builder;
    
    protected boolean cancelled;
    
    /**
     * Hidden constructor.
     */
    protected ProcessInstance() { }

    /**
     * Default constructor.
     *
     * @param definition the process definition of this instance
     * @param builder the builder to create specific tokens
     */
    public ProcessInstance(ProcessDefinition definition, @Nonnull TokenBuilder builder) {
        
        this.definition = definition;
        this.id = UUID.randomUUID();
        this.assignedTokens = new ArrayList<Token>();
        this.context = new ProcessInstanceContextImpl();
        this.cancelled = false;

        //if(builder == null) {
        //    throw new JodaEngineRuntimeException("Builder is null.");
        //}
        this.builder = builder;
        this.builder.setInstance(this);
    }

    @Override
    public TokenBuilder getBuilder() {
    
        return builder;
    }

    @Override
    public void setBuilder(TokenBuilder builder) {
    
        this.builder = builder;
    }

    @Override
    public void addToken(Token token) {
        
        this.assignedTokens.add(token);
    }

    @Override
    public ProcessInstanceContext getContext() {
        
        return context;
    }

    @Override
    public List<Token> getAssignedTokens() {
        
        return assignedTokens;
    }

    @Override
    public ProcessDefinition getDefinition() {
        
        return definition;
    }

    @Override
    public UUID getID() {
        
        return id;
    }

    @Override
    public void removeToken(Token token) {

        this.assignedTokens.remove(token);

    }

    @Override
    public boolean hasAssignedTokens() {

        return !assignedTokens.isEmpty();
    }

    @Override
    public void cancel() {

        cancelled = true;
        
        // Cancel all ongoing executions
        synchronized (assignedTokens) {
            for (Token tokenToCancel : assignedTokens) {
                tokenToCancel.cancelExecution();
            }
            assignedTokens.clear();
        }
        

    }

    @Override
    public boolean isCancelled() {

        return cancelled;
    }

    @Override
    public Token createToken(Node startNode) {
        return createToken(startNode, null);
    }
    
    @Override
    public Token createToken(Node startNode,
                             Token parentToken) {
        Token token = builder.create(startNode, parentToken);
        assignedTokens.add(token);
        return token;
    }
    
}
