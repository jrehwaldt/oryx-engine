package org.jodaengine.process.token;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.ext.AbstractPluggable;
import org.jodaengine.ext.exception.InstanceTerminationHandler;
import org.jodaengine.ext.exception.LoggerExceptionHandler;
import org.jodaengine.ext.listener.AbstractExceptionHandler;
import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.plugin.activity.ActivityLifecycleChangeEvent;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;

/**
 * The implementation of a process token.
 */
public class TokenImpl extends AbstractPluggable<AbstractTokenListener> implements Token {

    private UUID id;

    private Node currentNode;

    private ActivityState currentActivityState = null;

    private AbstractProcessInstance instance;

    private Transition lastTakenTransition;

    private Navigator navigator;

    private List<Token> lazySuspendedProcessingTokens;

    private List<AbstractTokenListener> plugins;

    private AbstractExceptionHandler exceptionHandler;

    /**
     * Instantiates a new token impl.
     * 
     * @param startNode
     *            the start node
     * @param instance
     *            the instance
     */
    public TokenImpl(Node startNode, AbstractProcessInstance instance) {

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
    public TokenImpl(Node startNode, AbstractProcessInstance instance, Navigator navigator) {

        this.currentNode = startNode;
        this.instance = instance;
        this.navigator = navigator;
        this.id = UUID.randomUUID();
        changeActivityState(ActivityState.INIT);
        this.plugins = new ArrayList<AbstractTokenListener>();
        
        //
        // at this point, you can register as much runtime exception handlers as you wish, following the chain of
        // responsibility pattern. The handler is used for runtime errors that occur in process execution.
        //
        this.exceptionHandler = new LoggerExceptionHandler();
        this.exceptionHandler.setNext(new InstanceTerminationHandler());
    }

    /**
     * Instantiates a new token impl.
     * 
     * @param startNode
     *            the start node
     */
    public TokenImpl(Node startNode) {

        this(startNode, new ProcessInstanceImpl(null), null);
    }
    
    /**
     * Hidden constructor.
     */
    protected TokenImpl() { }

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
    public void executeStep()
    throws JodaEngineException {

        if (instance.isCancelled()) {
            // the following statement was already called, when instance.cancel() was called. Nevertheless, a token
            // currently in execution might have created new tokens during split that were added to the instance.
            instance.getAssignedTokens().clear();
            return;
        }
        try {

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
    public List<Token> navigateTo(List<Transition> transitionList) {

        List<Token> tokensToNavigate = new ArrayList<Token>();
        if (transitionList.size() == 1) {
            Transition transition = transitionList.get(0);
            Node node = transition.getDestination();
            this.setCurrentNode(node);
            this.lastTakenTransition = transition;
            changeActivityState(ActivityState.INIT);
            tokensToNavigate.add(this);
        } else {
            for (Transition transition : transitionList) {
                Node node = transition.getDestination();
                Token newToken = createNewToken(node);
                newToken.setLastTakenTransition(transition);
                tokensToNavigate.add(newToken);
            }

            // this is needed, as the this-token would be left on the node that triggers the split.
            instance.removeToken(this);
        }
        return tokensToNavigate;

    }

    /**
     * Creates a new token in the same context.
     * 
     * @param node
     *            the node
     * @return the token {@inheritDoc}
     */
    @Override
    public Token createNewToken(Node node) {

        Token newToken = instance.createToken(node, navigator);
        // give all of this token's observers to the newly created ones.
        for (AbstractTokenListener plugin : plugins) {
            ((TokenImpl) newToken).registerPlugin(plugin);
        }

        return newToken;
    }

    @Override
    public boolean joinable() {

        return this.instance.getContext().allIncomingTransitionsSignaled(this.currentNode);
    }

    @Override
    public Token performJoin() {

        instance.getContext().removeSignaledTransitions(currentNode);
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

    /**
     * {@inheritDoc}
     */
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

        List<Token> splittedTokens = getCurrentNode().getOutgoingBehaviour().split(getLazySuspendedProcessingToken());

        for (Token token : splittedTokens) {
            navigator.addWorkToken(token);
        }

        lazySuspendedProcessingTokens = null;

    }

    /**
     * Gets the lazy suspended processing token.
     * 
     * @return the lazy suspended processing token
     */
    private List<Token> getLazySuspendedProcessingToken() {

        if (lazySuspendedProcessingTokens == null) {
            lazySuspendedProcessingTokens = new ArrayList<Token>();
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

    @Override
    public void registerPlugin(@Nonnull AbstractTokenListener plugin) {

        this.plugins.add(plugin);
        addObserver(plugin);
    }

    @Override
    public void deregisterPlugin(@Nonnull AbstractTokenListener plugin) {

        this.plugins.remove(plugin);
        deleteObserver(plugin);
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
}
