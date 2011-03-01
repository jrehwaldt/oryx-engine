package de.hpi.oryxengine.process.definition;

import java.util.ArrayList;

import de.hpi.oryxengine.process.structure.Node;

// TODO Really implement this, it is just a summary of getters and setters right now
/**
 * The Class AbstractProcessDefinitionImpl. Our Implementation of a Process Definition it is not really ready yet.
 */
public abstract class AbstractProcessDefinitionImpl implements ProcessDefinition {

    // TODO [Gerardo] Was ist denn hieran abstrakt

    /** The start nodes. */
    private ArrayList<Node> startNodes;

    /** The id. */
    private String id;

    /**
     * sets the start nodes.
     *
     * @param nodes the new start nodes
     * @see de.hpi.oryxengine.process.definition.ProcessDefinition#setStartNodes(java.util.ArrayList)
     */
    public void setStartNodes(ArrayList<Node> nodes) {

        this.startNodes = nodes;
    }

    /**
     * Gets the start nodes.
     * 
     * @return the start nodes
     */
    public ArrayList<Node> getStartNodes() {

        return startNodes;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {

        return id;
    }

    /**
     * Sets the id.
     *
     * @param s the new id
     */
    public void setId(String s) {

        this.id = s;
    }

}
