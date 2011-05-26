package org.jodaengine.process.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.AbstractToken;
import org.jodaengine.process.token.Token;


/**
 * The Class ProcessInstance.
 */
public class ProcessInstance extends AbstractProcessInstance {

    protected ProcessDefinition definition;
    protected ProcessInstanceContext context;
    protected UUID id;
    protected List<Token> assignedTokens;
    
    protected boolean cancelled;
    
    /**
     * Hidden constructor.
     */
    protected ProcessInstance() { }

    /**
     * Default constructor.
     * 
     * @param definition the process definition of this instance
     */
    public ProcessInstance(ProcessDefinition definition) {
        
        this.definition = definition;
        this.id = UUID.randomUUID();
        this.assignedTokens = new ArrayList<Token>();
        this.context = new ProcessInstanceContextImpl();
        this.cancelled = false;
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
    public AbstractToken createToken(Node node, Navigator nav) {

        // TODO Auto-generated method stub
        return null;
    }
    
}
