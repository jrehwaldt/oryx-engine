package org.jodaengine.process.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.token.Token;


/**
 * The Class ProcessInstanceImpl.
 * 
 * See {@link AbstractProcessInstance}.
 *
 * @param <TokenType> the generic type
 */
public abstract class AbstractProcessInstanceImpl<TokenType extends Token> extends AbstractProcessInstance<TokenType> {

    protected ProcessDefinition definition;
    protected ProcessInstanceContext context;
    protected UUID id;
    protected List<TokenType> assignedTokens;
    
    protected boolean cancelled;
    
    /**
     * Hidden constructor.
     */
    protected AbstractProcessInstanceImpl() { }

    /**
     * Default constructor.
     * 
     * @param definition the process definition of this instance
     */
    public AbstractProcessInstanceImpl(ProcessDefinition definition) {
        
        this.definition = definition;
        this.id = UUID.randomUUID();
        this.assignedTokens = new ArrayList<TokenType>();
        this.context = new ProcessInstanceContextImpl();
        this.cancelled = false;
    }

    @Override
    public void addToken(TokenType token) {
        
        this.assignedTokens.add(token);
    }

    @Override
    public ProcessInstanceContext getContext() {
        
        return context;
    }

    @Override
    public List<TokenType> getAssignedTokens() {
        
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
    public void removeToken(TokenType token) {

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
            for (TokenType tokenToCancel : assignedTokens) {
                tokenToCancel.cancelExecution();
            }
            assignedTokens.clear();
        }
        

    }

    @Override
    public boolean isCancelled() {

        return cancelled;
    }
    
}
