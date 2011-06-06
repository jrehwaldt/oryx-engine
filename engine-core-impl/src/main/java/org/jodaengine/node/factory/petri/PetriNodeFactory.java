package org.jodaengine.node.factory.petri;


import org.jodaengine.node.factory.TransitionFactory;
import org.jodaengine.node.incomingbehaviour.petri.TransitionJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.petri.PlaceSplitBehaviour;
import org.jodaengine.node.outgoingbehaviour.petri.TransitionSplitBehaviour;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;

/**
 * This Factory is able to create {@link Node Nodes} for petri nets. To be more specific places and transitions.
 */
public final class PetriNodeFactory extends TransitionFactory {

    /**
     * Hidden Constructor.
     */
    private PetriNodeFactory() {

    }
    
    public static Node createPlace() {
        // No Incoming Behaviour needed, it is not used by the PetriToken for the place.
        return new NodeImpl(null, null, new PlaceSplitBehaviour());
        
    }
    
    public static Node createPetriTransition() {
        return new NodeImpl(null, new TransitionJoinBehaviour(),
            new TransitionSplitBehaviour());
        
    }
    
    

}
