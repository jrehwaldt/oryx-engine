package org.jodaengine.process.token;

import java.util.ArrayList;
import java.util.List;
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

    protected List<Token> lazySuspendedProcessingTokens;

    @JsonIgnore
    protected AbstractExceptionHandler exceptionHandler;

    /**
     * Instantiates a new process {@link AbstractToken}.
     * 
     * @param startNode
     *            the start node
     * @param instance
     *            the instance
     * @param navigator
     *            the navigator
     */
    public AbstractToken(Node startNode, AbstractProcessInstance instance, Navigator navigator) {

        super();

        this.currentNode = startNode;
        this.instance = instance;
        this.navigator = navigator;
        this.id = UUID.randomUUID();

        //
        // at this point, you can register as much runtime exception handlers as you wish, following the chain of
        // responsibility pattern. The handler is used for runtime errors that occur in process execution.
        //
        this.exceptionHandler = new LoggerExceptionHandler();
        this.exceptionHandler.setNext(new InstanceTerminationHandler());
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

    // @Override
    // public void suspend() {
    //
    // changeActivityState(ActivityState.WAITING);
    // navigator.addSuspendToken(this);
    // }
    //
    // @Override
    // public void resume() {
    //
    // navigator.removeSuspendToken(this);
    //
    // try {
    // completeExecution();
    // } catch (NoValidPathException nvpe) {
    // exceptionHandler.processException(nvpe, this);
    // }
    // }

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
     * Registers any available extension suitable for {@link AbstractToken}.
     * 
     * Those include {@link AbstractExceptionHandler} as well as {@link AbstractTokenListener}.
     * 
     * @param extensionService
     *            the {@link ExtensionService}, which provides access to the extensions
     */
    public void loadExtensions(@Nullable ExtensionService extensionService) {

        // TODO use this method to register available extensions

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
    
    public Token createNewToken(Node node) {
        AbstractToken token = (AbstractToken) instance.createToken(node, navigator);
        token.registerListeners(getListeners());
        return token;
    }

}
