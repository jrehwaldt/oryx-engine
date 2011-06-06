package org.jodaengine.node.factory.petri;

import org.jodaengine.node.factory.TransitionFactory;
import org.jodaengine.process.structure.Node;

/**
 * This Factory is able to create {@link Node Nodes} for specific BPMN constructs like an BPMN-XOR-Gateway or ...
 */
public final class PetriNodeFactory extends TransitionFactory {

    /**
     * Hidden Constructor.
     */
    private PetriNodeFactory() {

    }

}
