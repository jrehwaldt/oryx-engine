package org.jodaengine.process.token;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;

/**
 * 
 * @author Jannik
 * 
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
       // We assume to be on a place
       //TODO Aktiviere ausgehende Kanten von dieser Stelle (jedes mal neuberechnen, sonst könnte es Fehler geben)
       for(Transition t : currentNode.getOutgoingTransitions()) {
           //TODO alle alten signalisierten müssen gelöscht werden
           
           //aktiviere Transisitonen
           instance.getContext().setWaitingExecution(t);
       }
        //TODO Waehle zuufaellig eine aktivierte Kante, prüfe auf weitere eingehende Verbindungen
       //und betrete diese dann
       
       currentNode.getOutgoingBehaviour().split(getLazySuspendedProcessingToken());
       
       //TODO entferne waitingExecutions...+token
       //TODO Split in der Transition durchführen
       //TODO Tokens setzen
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
        //Petri Net Semantic: Produce a new Token after a Transition
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

}
