package org.jodaengine.process.token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jodaengine.RepositoryServiceInside;
import org.jodaengine.eventmanagement.EventService;
import org.jodaengine.ext.AbstractListenable;
import org.jodaengine.ext.exception.InstanceTerminationHandler;
import org.jodaengine.ext.exception.LoggerExceptionHandler;
import org.jodaengine.ext.handler.AbstractExceptionHandler;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.jodaengine.resource.IdentityService;
import org.jodaengine.resource.worklist.WorklistServiceIntern;
import org.jodaengine.util.ServiceContext;
import org.jodaengine.util.ServiceContextImpl;

/**
 * AbstractToken class, which is used by other specific Token classes like the BPMN Token.
 */
public abstract class AbstractToken extends AbstractListenable<AbstractTokenListener> implements Token, ServiceContext {

    protected UUID id;

    protected Navigator navigator;

    protected AbstractProcessInstance instance;

    protected Node currentNode;
    protected ControlFlow lastTakenControlFlow;

    @JsonIgnore
    private ServiceContext serviceContext;

    @JsonIgnore
    protected boolean suspandable;

    protected List<Token> joinedTokens;
    protected HashMap<String, Object> internalVariables;

    @JsonIgnore
    protected AbstractExceptionHandler exceptionHandler;

    /**
     * Instantiates a new process {@link TokenImpl} and register all available extensions.
     * 
     * @param startNode
     *            the start node
     * @param instance
     *            the instance
     * @param navigator
     *            the navigator
     * @param extensionService
     *            the {@link ExtensionService} to load and register extensions from, may be null
     */
    public AbstractToken(Node startNode,
                         AbstractProcessInstance instance,
                         Navigator navigator,
                         @Nullable ExtensionService extensionService) {

        this.currentNode = startNode;
        this.instance = instance;
        this.navigator = navigator;
        this.id = UUID.randomUUID();

        //
        // register default exception chain;
        // additional extensions may be registered via the ExtensionService
        //
        this.exceptionHandler = new LoggerExceptionHandler();
        this.exceptionHandler.setNext(new InstanceTerminationHandler());

        this.serviceContext = new ServiceContextImpl();

        //
        // load available extensions, if an ExtensionService is provided
        //
        loadExtensions(extensionService);
    }

    /**
     * Hidden constructor.
     */
    protected AbstractToken() {

    }

    @Override
    public Node getCurrentNode() {

        return currentNode;
    }

    @Override
    public void setCurrentNode(Node node) {

        currentNode = node;
    }

    @Override
    public UUID getID() {

        return id;
    }

    // @Override
    // public boolean joinable() {
    //
    // return this.instance.getContext().allIncomingTransitionsSignaled(this.currentNode);
    // }
    //
    // @Override
    // public Token performJoin() {
    //
    // instance.getContext().removeIncomingTransitions(currentNode);
    // return this;
    // }

    @Override
    public AbstractProcessInstance getInstance() {

        return instance;
    }

    @Override
    public ControlFlow getLastTakenControlFlow() {

        return lastTakenControlFlow;
    }

    @Override
    public void setLastTakenControlFlow(ControlFlow t) {

        this.lastTakenControlFlow = t;
    }

    /**
     * Gets the joined tokens. This list is lazily initialized.
     * 
     * @return the {@link Token}s that were produced during the join.
     */
    protected List<Token> getJoinedTokens() {

        if (joinedTokens == null) {
            joinedTokens = new LinkedList<Token>();
        }
        
        return joinedTokens;
    }

    @Override
    public Navigator getNavigator() {

        return this.navigator;
    }

    /**
     * Registers any number of {@link AbstractExceptionHandler}s.
     * New handlers are added at the beginning of the chain.
     * 
     * @param handlers
     *            the handlers to be added
     */
    public void registerExceptionHandlers(@Nonnull List<AbstractExceptionHandler> handlers) {

        //
        // add each handler at the beginning
        //
        for (AbstractExceptionHandler handler : handlers) {
            handler.addLast(this.exceptionHandler);
            this.exceptionHandler = handler;
        }
    }

    /**
     * Registers any available extension suitable for {@link TokenImpl}.
     * 
     * Those include {@link AbstractExceptionHandler} as well as {@link AbstractTokenListener}.
     * 
     * @param extensionService
     *            the {@link ExtensionService}, which provides access to the extensions
     */
    protected void loadExtensions(@Nullable ExtensionService extensionService) {

        //
        // no ExtensionService = no extensions
        //
        if (extensionService == null) {
            return;
        }

        //
        // get fresh listener and handler instances
        //
        List<AbstractExceptionHandler> tokenExHandler = extensionService.getExtensions(AbstractExceptionHandler.class);
        List<AbstractTokenListener> tokenListener = extensionService.getExtensions(AbstractTokenListener.class);

        registerListeners(tokenListener);
        registerExceptionHandlers(tokenExHandler);
    }

    /**
     * Creates a new token and registers this for the listeners.
     *
     * @param startNode the start node
     * @return the token
     */
    public Token createToken(Node startNode) {
        AbstractToken token = (AbstractToken) instance.createToken(startNode);
        token.registerListeners(getListeners());
        return token;
    }

    //
    // ==== Handling internal varibales ====
    //
    /**
     * Gets the internal variables.
     * 
     * @return the internal variables
     */
    @JsonIgnore
    private Map<String, Object> getInternalVariables() {

        if (internalVariables == null) {
            internalVariables = new HashMap<String, Object>();
        }
        return internalVariables;
    }

    @Override
    public Object getInternalVariable(String id) {

        return getInternalVariables().get(id);
    }

    @Override
    public void setInternalVariable(String variableId, Object variableValue) {

        getInternalVariables().put(variableId, variableValue);

    }

    @Override
    public void deleteInternalVariable(String id) {

        getInternalVariables().remove(id);

    }

    @Override
    public Map<String, Object> getAllInternalVariables() {

        return getInternalVariables();
    }

    //
    // ==== ServiceContext implementation ====
    //
    @Override
    @JsonIgnore
    public EventService getEventManagerService() {

        return serviceContext.getEventManagerService();
    }

    @Override
    @JsonIgnore
    public ExtensionService getExtensionService() {

        return serviceContext.getExtensionService();
    }

    @Override
    @JsonIgnore
    public IdentityService getIdentityService() {

        return serviceContext.getIdentityService();
    }

    @Override
    @JsonIgnore
    public NavigatorInside getNavigatorService() {

        return serviceContext.getNavigatorService();
    }

    @Override
    @JsonIgnore
    public RepositoryServiceInside getRepositiory() {

        return serviceContext.getRepositiory();
    }

    @Override
    @JsonIgnore
    public WorklistServiceIntern getWorklistService() {

        return serviceContext.getWorklistService();
    }
}
