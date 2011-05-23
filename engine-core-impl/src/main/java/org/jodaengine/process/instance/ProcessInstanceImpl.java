package org.jodaengine.process.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenImpl;


/**
 * The Class ProcessInstanceImpl.
 * 
 * See {@link AbstractProcessInstance}.
 */
public class ProcessInstanceImpl extends AbstractProcessInstance {

    private ProcessDefinition definition;
    private ProcessInstanceContext context;
    private UUID id;
    private List<Token> assignedTokens;
    
    private boolean cancelled;
    
    /**
     * Hidden constructor.
     */
    protected ProcessInstanceImpl() { }

    /**
     * Default constructor.
     * 
     * @param definition the process definition of this instance
     */
    public ProcessInstanceImpl(ProcessDefinition definition) {
        
        this.definition = definition;
        this.id = UUID.randomUUID();
        this.assignedTokens = new ArrayList<Token>();
        this.context = new ProcessInstanceContextImpl();
        this.cancelled = false;
    }

    @Override
    public void addToken(Token t) {
        
        this.assignedTokens.add(t);
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
    public Token createToken(Node node, Navigator nav) {
        
        TokenImpl token = new TokenImpl(node, this, nav);
        
        this.assignedTokens.add(token);
        return token;
    }

    @Override
    public void removeToken(Token t) {

        this.assignedTokens.remove(t);

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
    
}
