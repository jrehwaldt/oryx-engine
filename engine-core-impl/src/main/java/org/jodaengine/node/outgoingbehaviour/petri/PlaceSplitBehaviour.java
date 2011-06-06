package org.jodaengine.node.outgoingbehaviour.petri;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.Token;

/**
 * The Class PlaceSplitBehaviour. It searches for PetriTransitions which are activated and reachable.
 * Then it chooses randomly one of them and deletes the consumed tokens.
 *
 */
public class PlaceSplitBehaviour implements OutgoingBehaviour {

    @Override
    public List<Token> split(List<Token> tokens)
    throws NoValidPathException {
        
        // We only work if one token, due to petri net semantic
        Token token = tokens.get(0);
        
        // The activated transitions we can reach from this place
        List<Transition> possibleTransitionsToTake = new ArrayList<Transition>();
        
        Node currentNode = token.getCurrentNode();
        
        List<Token> transitionsToNavigate = null;
        Node nextPetriTranisiton = null;
        
        // Now check for reachable transitions
        for (Transition t : currentNode.getOutgoingTransitions()) {
            
            //save all possible Transitions
            nextPetriTranisiton = t.getDestination();
            boolean joinPossible = nextPetriTranisiton.getIncomingBehaviour().joinable(token, nextPetriTranisiton);
            
            if (joinPossible) {
                possibleTransitionsToTake.add(t);
            }
        }
        
        // If there are no available transitions return
        if (possibleTransitionsToTake.size() == 0) {
            return transitionsToNavigate;
        }
        
        // In case there are possible transitions: Randomly choose an activated Transition
        int size = possibleTransitionsToTake.size();
        int randomNumber = new Random().nextInt(size);
        
        List<Transition> chosenTransitionList = new ArrayList<Transition>();
        
        // Now move the token to the next node and return it 
        chosenTransitionList.add(possibleTransitionsToTake.get(randomNumber));
        transitionsToNavigate = token.navigateTo(chosenTransitionList);

        return transitionsToNavigate;
    }




}
