package org.jodaengine.process.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jodaengine.ext.listener.AbstractExceptionHandler;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.ext.util.TypeSafeInstanceMap;
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
    
    @JsonIgnore
    private TypeSafeInstanceMap extensions;
    
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
        
        this.extensions = new TypeSafeInstanceMap();
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
        
        token.registerListeners(this.extensions.getInstances(AbstractTokenListener.class));
        token.registerExceptionHandlers(this.extensions.getInstances(AbstractExceptionHandler.class));
        
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
    
    /**
     * Registers any available extension suitable for {@link ProcessInstanceImpl} and {@link TokenImpl}.
     * 
     * Those include {@link AbstractExceptionHandler} as well as {@link AbstractTokenListener}.
     * 
     * @param extensionService the {@link ExtensionService}, which provides access to the extensions
     */
    public void loadExtensions(@Nullable ExtensionService extensionService) {
        
        //
        // no ExtensionService = no extensions
        //
        if (extensionService == null) {
            return;
        }
        
        //
        // clear any already registered extension
        //
        this.extensions.clear();
        
        //
        // get fresh listener and handler instances
        //
        List<AbstractExceptionHandler> tokenExHandler = extensionService.getExtensions(AbstractExceptionHandler.class);
        List<AbstractTokenListener> tokenListener = extensionService.getExtensions(AbstractTokenListener.class);
        
        this.extensions.addInstances(AbstractExceptionHandler.class, tokenExHandler);
        this.extensions.addInstances(AbstractTokenListener.class, tokenListener);
    }
    
}
