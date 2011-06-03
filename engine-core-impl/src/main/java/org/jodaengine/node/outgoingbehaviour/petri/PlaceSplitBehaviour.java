package org.jodaengine.node.outgoingbehaviour.petri;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenUtil;

/**
 * The Class TakeAllSplitBehaviour. Will signal the first outgoing transition, of which the condition evaluates to true.
 */
public class PlaceSplitBehaviour implements OutgoingBehaviour {
    
    private AbstractProcessInstance instance = null;

    /**
     * Split according to the transitions.
     * 
     * @param tokens
     *            the instances
     * @return the list
     * @throws NoValidPathException
     *             the no valid path exception {@inheritDoc}
     */
    @Override
    public List<Token> split(List<Token> tokens)
    throws NoValidPathException {
        
        TokenUtil util = new TokenUtil();
        
        // We only work if one token, due to petri net semantic
        Token token = tokens.get(0);
        
        List<Transition> possibleTransitionsToTake = new ArrayList<Transition>();
        Node currentNode = token.getCurrentNode();
        instance = token.getInstance();
        List<Token> transitionsToNavigate = null;
        Node nextPetriTranisiton = null;
        
        for(Transition t : currentNode.getOutgoingTransitions()) {

            //TODO activate possible Transitions ?? is this necessary?
            //instance.getContext().setWaitingExecution(t);
            
            //save all possible Transitions
            nextPetriTranisiton = t.getDestination();
            boolean joinPossible = nextPetriTranisiton.getIncomingBehaviour().joinable(token, nextPetriTranisiton);
            
            if(joinPossible) {
                possibleTransitionsToTake.add(t);
            }
        }
         //Randomly choose an activated Transition
        int size = possibleTransitionsToTake.size();
        int randomNumber = new Random().nextInt(size);
        
        List<Transition> chosenTransitionList = new ArrayList<Transition>();
        
        //Now create the new token, move it to the next node and return it 
        chosenTransitionList.add(possibleTransitionsToTake.get(randomNumber));
        transitionsToNavigate = token.navigateTo(chosenTransitionList);
        
        //Delete the activated paths if no more other tokens there
        //Delete tokens in the places before the Transition
        
        List<Token> oldTokens;
        //nextPetriTranisiton.getIncomingTransitions()
        for(Transition t : chosenTransitionList.get(0).getDestination().getIncomingTransitions()) {
            Node placeBeforeTransition = t.getSource();
            //Count Tokens, which are still there
            oldTokens = util.getTokensWhichAreOnPlace(placeBeforeTransition, instance);
            
            // Remove the signaled transition, because there are no tokens left.
            //if(oldTokens.size() == 1) {
            //    instance.getContext().removeIncomingTransition(t, currentNode);
            //}
            // One token of the place should be deleted.
            // Because these are ordinary petri nets's all tokens are equal and therefore we can delete just the first.
            instance.removeToken(oldTokens.get(0));
            //TODO Token auch aus dem Navigator löschen...das ist aber nicht so einfach wegen
            // Cuncurrency issues...hier muss beim herausnehmen miot Locks aquire/release gearbeitet werden
            
        }

        return transitionsToNavigate;
    }


}
