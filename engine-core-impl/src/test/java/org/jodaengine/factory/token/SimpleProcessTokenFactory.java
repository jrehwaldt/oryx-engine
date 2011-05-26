package org.jodaengine.factory.token;

import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;


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
        Token p = new BpmnToken(startNode, new ProcessInstance(null), null);
        return p;
    }

}
