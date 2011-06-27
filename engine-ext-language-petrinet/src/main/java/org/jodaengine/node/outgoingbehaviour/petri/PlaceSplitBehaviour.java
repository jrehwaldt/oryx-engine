package org.jodaengine.node.outgoingbehaviour.petri;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * The Class PlaceSplitBehaviour. It searches for PetriTransitions which are activated and reachable.
 * Then it chooses randomly one of them and deletes the consumed tokens.
 *
 */
public class PlaceSplitBehaviour implements OutgoingBehaviour {

    @Override
    public Collection<Token> split(Collection<Token> tokens)
    throws NoValidPathException {
        
        // We only work if one token, due to petri net semantic
        Token token = tokens.iterator().next();
        
        // The activated transitions we can reach from this place
        List<ControlFlow> possibleControlFlowsToTake = new ArrayList<ControlFlow>();
        
        Node currentNode = token.getCurrentNode();
        
        Collection<Token> transitionsToNavigate = null;
        Node nextPetriTranisiton = null;
        
        // Now check for reachable transitions
        for (ControlFlow t : currentNode.getOutgoingControlFlows()) {
            
            //save all possible Transitions
            nextPetriTranisiton = t.getDestination();
            boolean joinPossible = nextPetriTranisiton.getIncomingBehaviour().joinable(token, nextPetriTranisiton);
            
            if (joinPossible) {
                possibleControlFlowsToTake.add(t);
            }
        }
        
        // If there are no available transitions return
        if (possibleControlFlowsToTake.size() == 0) {
            return transitionsToNavigate;
        }
        
        // In case there are possible transitions: Randomly choose an activated Transition
        int size = possibleControlFlowsToTake.size();
        int randomNumber = new Random().nextInt(size);
        
        List<ControlFlow> chosenControlFlowList = new ArrayList<ControlFlow>();
        
        // Now move the token to the next node and return it 
        chosenControlFlowList.add(possibleControlFlowsToTake.get(randomNumber));
        transitionsToNavigate = token.navigateTo(chosenControlFlowList);
        
        return transitionsToNavigate;
    }




}
