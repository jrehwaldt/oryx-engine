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
 */
public class ProcessInstanceImpl<T extends Token<?>> extends AbstractProcessInstance<T> {

    private ProcessDefinition definition;
    private ProcessInstanceContext context;
    private UUID id;
    private List<T> assignedTokens;

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
        this.assignedTokens = new ArrayList<T>();
        this.context = new ProcessInstanceContextImpl();
        this.cancelled = false;
    }

    @Override
    public void addToken(T t) {

        this.assignedTokens.add(t);
    }

    @Override
    public ProcessInstanceContext getContext() {
        
        return context;
    }

    @Override
    public List<T> getAssignedTokens() {

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
    public void removeToken(T t) {

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
            for (T tokenToCancel : assignedTokens) {
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
