package de.hpi.oryxengine.process.definition;

import java.util.List;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.util.Identifiable;

/**
 * The Interface of the process definition. The process definition holds the essential information of a process and
 * specifies its list of start nodes.
 * 
 * @author thorben
 */
public interface ProcessDefinition extends Identifiable {

    /**
     * Gets the description.
     * 
     * @return the description
     */
    String getDescription();

    /**
     * Sets the description.
     * 
     * @param description
     *            the new description
     */
    void setDescription(String description);

    /**
     * Gets the start nodes of the process. Tokens can be placed there. As the process definition consists of a tree
     * structure of nodes, this is enough to reference the whole defnition.
     * 
     * @return the start nodes
     */
    List<Node> getStartNodes();
}
