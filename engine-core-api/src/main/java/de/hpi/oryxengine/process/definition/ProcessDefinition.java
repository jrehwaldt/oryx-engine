package de.hpi.oryxengine.process.definition;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.IllegalStarteventException;
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
     * Retrieves the name of the {@link ProcessDefinition}.
     * 
     * @return the name of the {@link ProcessDefinition}
     */
    String getName();
    
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

    /**
     * Gets the start triggers: events pointing to nodes that are tokens put on, if the event is invoked.
     * 
     * @return the start triggers
     */
    @XmlTransient
    Map<StartEvent, Node> getStartTriggers();

    /**
     * Adds the start trigger. If event is invoked, a token will spawn on node.
     *
     * @param event the event
     * @param node the node
     * @throws IllegalStarteventException thrown if the provided node isn't a startnode.
     */
    void addStartTrigger(StartEvent event, Node node) throws IllegalStarteventException;
}
