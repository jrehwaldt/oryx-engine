package org.jodaengine.process.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jodaengine.ext.AbstractPluggable;
import org.jodaengine.ext.exception.InstanceTerminationHandler;
import org.jodaengine.ext.exception.LoggerExceptionHandler;
import org.jodaengine.ext.listener.AbstractExceptionHandler;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;

/**
 * 
 * @author Gery
 * 
 */

public abstract class AbstractToken extends AbstractPluggable<AbstractTokenListener>
implements Token {

    protected UUID id;

    protected Navigator navigator;

    protected AbstractProcessInstance instance;

    protected Node currentNode;
    protected Transition lastTakenTransition;
   
    @JsonIgnore
    protected boolean suspandable;

    protected List<Token> lazySuspendedProcessingTokens;
    private HashMap<String, Object> internalVariables;

    @JsonIgnore
    private AbstractExceptionHandler exceptionHandler;

    /**
     * Instantiates a new process {@link TokenImpl}. This will not register any available extension.
     * 
     * @param startNode
     *            the start node
     * @param instance
     *            the instance
     * @param navigator
     *            the navigator
     */
    public TokenImpl(Node startNode, AbstractProcessInstance instance, Navigator navigator) {

        this(startNode, instance, navigator, null);
    }

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
    public TokenImpl(Node startNode,
                     AbstractProcessInstance instance,
                     Navigator navigator,
                     @Nullable ExtensionService extensionService) {

        super();

        // TODO Jan - use this constructor to register potential extensions - wait for Jannik's refactoring.

        this.currentNode = startNode;
        this.instance = instance;
        this.navigator = navigator;
        this.id = UUID.randomUUID();
        changeActivityState(ActivityState.INIT);

        //
        // register default exception chain;
        // additional extensions may be registered via the ExtensionService
        //
        this.exceptionHandler = new LoggerExceptionHandler();
        this.exceptionHandler.setNext(new InstanceTerminationHandler());

        //
        // load available extensions, if an ExtensionService is provided
        //
        loadExtensions(extensionService);
    }

    /**
     * Hidden constructor.
     */
    protected TokenImpl() {

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

    @Override
    public boolean joinable() {

        return this.instance.getContext().allIncomingTransitionsSignaled(this.currentNode);
    }

    @Override
    public Token performJoin() {

        instance.getContext().removeIncomingTransitions(currentNode);
        return this;
    }

    @Override
    public AbstractProcessInstance getInstance() {

        return instance;
    }

    @Override
    public Transition getLastTakenTransition() {

        return lastTakenTransition;
    }

    @Override
    public void setLastTakenTransition(Transition t) {

        this.lastTakenTransition = t;
    }

    @Override
    public void suspend() {

        changeActivityState(ActivityState.WAITING);
        navigator.addSuspendToken(this);
    }

    @Override
    public void resume(Object resumeObject) {

        navigator.removeSuspendToken(this);

        try {
            resumeAndCompleteExecution(resumeObject);
        } catch (NoValidPathException nvpe) {
            exceptionHandler.processException(nvpe, this);
        }
    }

    /**
     * Gets the lazy suspended processing token.
     * 
     * @return the lazy suspended processing token
     */
    protected List<Token> getLazySuspendedProcessingToken() {

        if (lazySuspendedProcessingTokens == null) {
            lazySuspendedProcessingTokens = new ArrayList<Token>();
        }

        return lazySuspendedProcessingTokens;
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
    
    public Token createNewToken() {
        AbstractToken token = (AbstractToken) instance.createToken();
        token.registerListeners(getListeners());
        return token;
    }

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
}
