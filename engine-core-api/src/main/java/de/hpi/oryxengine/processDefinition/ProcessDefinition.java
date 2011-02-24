package de.hpi.oryxengine.processDefinition;

import java.util.ArrayList;

import de.hpi.oryxengine.processstructure.Node;

/**
 * The Interface of the process definition. 
 * The process definition holds the essential information of a process
 * and specifies its list of start nodes. 
 */
public interface ProcessDefinition {

    // TODO [Gerardo] die Erstellung einer Prozessinstanz sollte doch die Prozessdefinition selbst übernehmen, oder??

    // void setTransition(Node start, Node end);
    /**
     * Sets the start nodes of the process.
     *
     * @param n the new start nodes
     */
    void setStartNodes(ArrayList<Node> n);

    /**
     * Gets the ID of the process definition.
     *
     * @return the ID
     */
    String getID();

    /**
     * Sets the ID of the process definition.
     *
     * @param s the new ID
     */
    void setID(String s);

}
