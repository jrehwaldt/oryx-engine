package org.jodaengine.process.token;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.node.outgoingbehaviour.petri.TransitionSplitBehaviour;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.token.AbstractToken;
import org.jodaengine.process.token.Token;

/**
 * The Class PetriToken. The token is used for petri nets.
 *
 * @author Jannik
 */
public class PetriToken extends AbstractToken {
    
    /**
     * Hidden Constructor.
     */
    protected PetriToken() {
        
    }
    
    /**
     * Instantiates a new process {@link Token}. This will not register any available extension.
     *
     * @param startNode the start node
     * @param instance the instance
     * @param navigator the navigator
     */
    public PetriToken(Node startNode, AbstractProcessInstance instance, Navigator navigator) {

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
    public PetriToken(Node startNode,
                     AbstractProcessInstance instance,
                     Navigator navigator,
                     @Nullable ExtensionService extensionService) {

        super(startNode, instance, navigator, extensionService);
    }

    @Override
    public void suspend() {

        navigator.addSuspendToken(this);
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
       // We assume to be on a place and start with the split behaviour of the place.
       List<Token> tokens = new ArrayList<Token>();
       tokens.add(this);
       
       // Put the token on the transition after the place
       List<Token> newTokens = currentNode.getOutgoingBehaviour().split(tokens);
       
       // If there are no possible options, the token execution has to be skipped.
       if (newTokens == null) {
           this.navigator.getScheduler().releaseLock(this);
           // The old token is not put in the navigator again, we assume that an another token will 
           // trigger the following PetriTransition later on.
           return;
       }
       
       // During the split the token was moved to the next node...there we join, to consume other used tokens.
       currentNode.getIncomingBehaviour().join(newTokens.get(0));
       
       // Now split at the Transition and put tokens on the following places.
       lazySuspendedProcessingTokens = currentNode.getOutgoingBehaviour().split(newTokens);
       
       for (Token token : lazySuspendedProcessingTokens) {
           navigator.addWorkToken(token);
       }

       lazySuspendedProcessingTokens = null;
       internalVariables = null;
       
    }
    
    @Override
    public List<Token> navigateTo(List<ControlFlow> controlFlowList) {

        List<Token> tokensToNavigate = new ArrayList<Token>();

        if (controlFlowList.size() == 0) {
            this.exceptionHandler.processException(new NoValidPathException(), this);

        // petri net semantic: Produce a new token after a transition
        } else {

            for (ControlFlow controlFlow : controlFlowList) {
                Node node = controlFlow.getDestination();
                Token newToken;
                // Only create a new token, if a PetriTransition was before 
                if (controlFlow.getSource().getOutgoingBehaviour() instanceof TransitionSplitBehaviour) {
                    newToken = createToken(node);
                } else {
                    newToken = this;
                }
                newToken.setCurrentNode(node);
                //newToken.setLastTakenTransition(transition);
                tokensToNavigate.add(newToken);
            }
        }
        return tokensToNavigate;

    }

    @Override
    public boolean isSuspandable() {

        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ActivityState getCurrentActivityState() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void resume(Object resumeObject) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public void cancelExecution() {

        // TODO Auto-generated method stub
        
    }

    @Override
    public Map<String, Object> getAttributes() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getAttribute(String attributeKey) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {

        // TODO Auto-generated method stub
        
    }

}
