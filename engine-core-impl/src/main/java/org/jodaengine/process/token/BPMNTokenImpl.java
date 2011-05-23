package org.jodaengine.process.token;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.ext.AbstractPluggable;
import org.jodaengine.ext.exception.InstanceTerminationHandler;
import org.jodaengine.ext.exception.LoggerExceptionHandler;
import org.jodaengine.ext.listener.AbstractExceptionHandler;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.ext.listener.token.ActivityLifecycleChangeEvent;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;

/**
 * The implementation of a process token.
 */
public class BPMNTokenImpl extends AbstractPluggable<AbstractTokenListener> implements BPMNToken {

    private UUID id;

    private Node<BPMNToken> currentNode;

    private ActivityState currentActivityState = null;

    private AbstractProcessInstance<BPMNToken> instance;

    private Transition<BPMNToken> lastTakenTransition;

    private Navigator navigator;

    private List<BPMNToken> lazySuspendedProcessingTokens;


    private AbstractExceptionHandler exceptionHandler;

    /**
     * Instantiates a new token impl.
     * 
     * @param startNode
     *            the start node
     * @param instance
     *            the instance
     */
    public BPMNTokenImpl(Node<BPMNToken> startNode, AbstractProcessInstance<BPMNToken> instance) {

        this(startNode, instance, null);
    }

    /**
     * Instantiates a new process token impl.
     * 
     * @param startNode
     *            the start node
     * @param instance
     *            the instance
     * @param navigator
     *            the navigator
     */
    public BPMNTokenImpl(Node<BPMNToken> startNode, AbstractProcessInstance<BPMNToken> instance, Navigator navigator) {

		super();
        this.currentNode = startNode;
        this.instance = instance;
        this.navigator = navigator;
        this.id = UUID.randomUUID();
        changeActivityState(ActivityState.INIT);
        
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
    protected BPMNTokenImpl() { }

    @Override
    public Node<BPMNToken> getCurrentNode() {

        return currentNode;
    }

    @Override
    public void setCurrentNode(Node<BPMNToken> node) {

        currentNode = node;
    }

    @Override
    public UUID getID() {

        return id;
    }

    @Override
    public void executeStep()
    throws JodaEngineException {

        if (instance.isCancelled()) {
            // the following statement was already called, when instance.cancel() was called. Nevertheless, a token
            // currently in execution might have created new tokens during split that were added to the instance.
            instance.getAssignedTokens().clear();
            return;
        }
        try {

            //TODO renaming
            lazySuspendedProcessingTokens = getCurrentNode().getIncomingBehaviour().join(this);
            changeActivityState(ActivityState.ACTIVE);

            Activity currentActivityBehavior = currentNode.getActivityBehaviour();
            currentActivityBehavior.execute(this);

            // Aborting the further execution of the process by the token, because it was suspended
            if (this.currentActivityState == ActivityState.WAITING) {
                return;
            }

            completeExecution();
        } catch (JodaEngineRuntimeException exception) {
            exceptionHandler.processException(exception, this);
        }
    }

    @Override
    public List<BPMNToken> navigateTo(List<Transition<BPMNToken>> transitionList) {

<<<<<<< HEAD:engine-core-impl/src/main/java/org/jodaengine/process/token/BPMNTokenImpl.java
        List<BPMNToken> tokensToNavigate = new ArrayList<BPMNToken>();
        if (transitionList.size() == 1) {
            Transition<BPMNToken> transition = transitionList.get(0);
            Node<BPMNToken> node = transition.getDestination();
=======
        List<Token> tokensToNavigate = new ArrayList<Token>();
        
        
        //
        // zero outgoing transitions
        //
        if (transitionList.size() == 0) {
            
            this.exceptionHandler.processException(new NoValidPathException(), this);
            
        //
        // one outgoing transition
        //
        } else
        if (transitionList.size() == 1) {
            
            Transition transition = transitionList.get(0);
            Node node = transition.getDestination();
>>>>>>> 495ce2e2e372709c87f44a620f16e8490582f161:engine-core-impl/src/main/java/org/jodaengine/process/token/TokenImpl.java
            this.setCurrentNode(node);
            this.lastTakenTransition = transition;
            changeActivityState(ActivityState.INIT);
            tokensToNavigate.add(this);
            
        //
        // multiple outgoing transitions
        //
        } else {
<<<<<<< HEAD:engine-core-impl/src/main/java/org/jodaengine/process/token/BPMNTokenImpl.java
            for (Transition<BPMNToken> transition : transitionList) {
                Node<BPMNToken> node = transition.getDestination();
                BPMNToken newToken = createNewToken(node);
=======
            
            for (Transition transition : transitionList) {
                Node node = transition.getDestination();
                Token newToken = createNewToken(node);
>>>>>>> 495ce2e2e372709c87f44a620f16e8490582f161:engine-core-impl/src/main/java/org/jodaengine/process/token/TokenImpl.java
                newToken.setLastTakenTransition(transition);
                tokensToNavigate.add(newToken);
            }
            
            // this is needed, as the this-token would be left on the node that triggers the split.
            instance.removeToken(this);
        }
        return tokensToNavigate;

    }

    @Override
    public BPMNToken createNewToken(Node<BPMNToken> node) {

        BPMNToken newToken = new BPMNTokenImpl(node, instance, navigator);
        instance.addToken(newToken);
            

        return newToken;
    }

    @Override
    public boolean joinable() {

        return this.instance.getContext().allIncomingTransitionsSignaled(this.currentNode);
    }

    @Override
    public BPMNToken performJoin() {

        instance.getContext().removeSignaledTransitions(currentNode);
        return this;
    }

    @Override
    public AbstractProcessInstance<BPMNToken> getInstance() {

        return instance;
    }

    @Override
    public Transition<BPMNToken> getLastTakenTransition() {

        return lastTakenTransition;
    }

    @Override
    public void setLastTakenTransition(Transition<BPMNToken> t) {

        this.lastTakenTransition = t;
    }

    @Override
    public void suspend() {
        
        changeActivityState(ActivityState.WAITING);
        navigator.addSuspendToken(this);
    }

    @Override
    public void resume() {
        
        navigator.removeSuspendToken(this);
        
        try {
            completeExecution();
        } catch (NoValidPathException nvpe) {
            exceptionHandler.processException(nvpe, this);
        }
    }

    /**
     * Completes the execution of the activity.
     * 
     * @throws NoValidPathException
     *             thrown if there is no valid path to be executed
     */
    private void completeExecution()
    throws NoValidPathException {

        currentNode.getActivityBehaviour().resume(this);
        changeActivityState(ActivityState.COMPLETED);

        List<BPMNToken> splittedTokens = getCurrentNode()
                                           .getOutgoingBehaviour()
                                           .split(getLazySuspendedProcessingToken());

        for (BPMNToken bPMNToken : splittedTokens) {
            navigator.addWorkToken(bPMNToken);
        }

        lazySuspendedProcessingTokens = null;

    }

    /**
     * Gets the lazy suspended processing token.
     * 
     * @return the lazy suspended processing token
     */
    private List<BPMNToken> getLazySuspendedProcessingToken() {

        if (lazySuspendedProcessingTokens == null) {
            lazySuspendedProcessingTokens = new ArrayList<BPMNToken>();
        }

        return lazySuspendedProcessingTokens;
    }

    @Override
    public Navigator getNavigator() {

        return this.navigator;
    }

    @Override
    public void cancelExecution() {

        if (this.currentActivityState == ActivityState.ACTIVE || this.currentActivityState == ActivityState.WAITING) {
            Activity currentActivityBehavior = currentNode.getActivityBehaviour();
            currentActivityBehavior.cancel(this);
        }

    }

    @Override
    public ActivityState getCurrentActivityState() {

        return currentActivityState;
    }

    /**
     * Changes the state of the activity that the token currently points to.
     * 
     * @param newState
     *            the new state
     */
    private void changeActivityState(ActivityState newState) {
        
        final ActivityState prevState = currentActivityState;
        this.currentActivityState = newState;
        setChanged();
        
        notifyObservers(new ActivityLifecycleChangeEvent(currentNode, prevState, newState, this));
    }
    
    /**
     * Registers any number of {@link AbstractExceptionHandler}s.
     * New handlers are added at the beginning of the chain.
     * 
     * @param handlers the handlers to be added
     */
    public void registerExceptionHandlers(@Nonnull List<AbstractExceptionHandler> handlers) {
        
        //
        // add each handler at the beginning
        //
        for (AbstractExceptionHandler handler: handlers) {
            handler.addLast(this.exceptionHandler);
            this.exceptionHandler = handler;
        }
    }
    
    /**
     * Registers any available extension suitable for {@link TokenImpl}.
     * 
     * Those include {@link AbstractExceptionHandler} as well as {@link AbstractTokenListener}.
     * 
     * @param extensionService the {@link ExtensionService}, which provides access to the extensions
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
    
}
