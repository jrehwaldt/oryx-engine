package org.jodaengine.process.token;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.ext.listener.token.ActivityLifecycleChangeEvent;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;

/**
 * 
 * @author Gery
 * 
 */
public class BpmnToken extends AbstractToken {

    private ActivityState currentActivityState = null;
    
    /**
     * Hidden Constructor
     */
    protected BpmnToken() {
        
    }
    
    /**
     * Instantiates a new process {@link TokenImpl}. This will not register any available extension.
     *
     * @param startNode the start node
     * @param instance the instance
     * @param navigator the navigator
     */
    public BpmnToken(Node startNode, AbstractProcessInstance instance, Navigator navigator) {

        this(startNode, instance, navigator, null);
    }
    
    /**
     * Instantiates a new process {@link TokenImpl} and register all available extensions.
     *
     * @param startNode the start node
     * @param instance the instance
     * @param navigator the navigator
     * @param extensionService the extension service
     */
    public BpmnToken(Node startNode,
                     AbstractProcessInstance instance,
                     Navigator navigator,
                     @Nullable ExtensionService extensionService) {

        super(startNode, instance, navigator, extensionService);
        changeActivityState(ActivityState.INIT);
    }

    @Override
    public ActivityState getCurrentActivityState() {

        return currentActivityState;
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
     * Completes the execution of the activity.
     *
     * @throws NoValidPathException
     *             thrown if there is no valid path to be executed
     */
    private void completeExecution()
    throws NoValidPathException {

        changeActivityState(ActivityState.COMPLETED);

        List<Token> splittedTokens = getCurrentNode().getOutgoingBehaviour().split(getLazySuspendedProcessingToken());

        for (Token token : splittedTokens) {
            navigator.addWorkToken(token);
        }

        lazySuspendedProcessingTokens = null;
        internalVariables = null;
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
    public void cancelExecution() {

        if (this.currentActivityState == ActivityState.ACTIVE || this.currentActivityState == ActivityState.WAITING) {
            Activity currentActivityBehavior = currentNode.getActivityBehaviour();
            currentActivityBehavior.cancel(this);
        }

    }

    /**
     * Changes the state of the activity that the token currently points to.
     * 
     * @param newState
     *            the new state
     */
    protected void changeActivityState(ActivityState newState) {

        final ActivityState prevState = currentActivityState;
        this.currentActivityState = newState;
        setChanged();

        notifyObservers(new ActivityLifecycleChangeEvent(currentNode, prevState, newState, this));
    }
    
    @Override
    public List<Token> navigateTo(List<Transition> transitionList) {

        List<Token> tokensToNavigate = new ArrayList<Token>();

        //
        // zero outgoing transitions
        //
        if (transitionList.size() == 0) {

            this.exceptionHandler.processException(new NoValidPathException(), this);

            //
            // one outgoing transition
            //
        } else if (transitionList.size() == 1) {

            Transition transition = transitionList.get(0);
            Node node = transition.getDestination();
            this.setCurrentNode(node);
            this.lastTakenTransition = transition;
            changeActivityState(ActivityState.INIT);
            tokensToNavigate.add(this);

            //
            // multiple outgoing transitions
            //
        } else {

            for (Transition transition : transitionList) {
                Node node = transition.getDestination();
                instance.getBuilder().setNode(node);
                Token newToken = createNewToken();
                newToken.setLastTakenTransition(transition);
                tokensToNavigate.add(newToken);
            }

            // this is needed, as the this-token would be left on the node that triggers the split.
            instance.removeToken(this);
        }
        return tokensToNavigate;

    }

    @Override
    public boolean isSuspandable() {

        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * Resumes the execution of the activity and completes it.
     *
     * @param resumeObject
     *            - an object that is passed from class that resumes the Token
     * @throws NoValidPathException
     *             thrown if there is no valid path to be executed
     */
    private void resumeAndCompleteExecution(Object resumeObject)
    throws NoValidPathException {

        currentNode.getActivityBehaviour().resume(this, resumeObject);

        completeExecution();
    }

}
