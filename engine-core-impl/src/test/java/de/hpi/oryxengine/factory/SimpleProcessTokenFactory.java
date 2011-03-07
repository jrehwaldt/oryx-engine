package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * A factory for creating simple ProcessInstance objects. So it creates a Process starting at one node spefified.
 */
public class SimpleProcessTokenFactory {
    
    /**
     * Creates the the simple Process Instance starting at a given node.
     *
     * @param startNode the start node
     * @return the process instance
     */
    public Token create(Node startNode) {
        Token p = new TokenImpl(startNode);
        return p;
    }

}
