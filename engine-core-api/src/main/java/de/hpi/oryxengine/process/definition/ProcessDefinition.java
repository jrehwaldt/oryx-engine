package de.hpi.oryxengine.process.definition;

import java.util.ArrayList;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.util.Identifiable;

/**
 * The Interface of the process definition. 
 * The process definition holds the essential information of a process
 * and specifies its list of start nodes. 
 */
public interface ProcessDefinition
extends Identifiable {

    // TODO [Gerardo] die Erstellung einer Prozessinstanz sollte doch die Prozessdefinition selbst übernehmen, oder??

    // void setTransition(Node start, Node end);
    /**
     * Sets the start nodes of the process.
     *
     * @param n the new start nodes
     */
    void setStartNodes(ArrayList<Node> n);

}
