package de.hpi.oryxengine.factory.token;

import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * A factory for creating simple ProcessToken objects. So it creates a Process starting at one node specified.
 */
public class SimpleProcessTokenFactory {
    
    /**
     * Creates the the simple Process Token starting at a given node with a new ProcessInstance.
     *
     * @param startNode the start node
     * @return the process instance
     */
    public Token create(Node startNode) {
        Token p = new TokenImpl(startNode, new ProcessInstanceImpl(null));
        return p;
    }

}
