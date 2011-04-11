package de.hpi.oryxengine.process.definition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.structure.Node;

/**
 * The Class ProcessDefinitionImpl. A process definition consists of a list of start nodes that, as we have a tree
 * structure of nodes, reference all the nodes of the definition transitively.
 */
public class ProcessDefinitionImpl implements ProcessDefinition {

    
    
    private String description;

    private UUID id;

    private List<Node> startNodes;

    private Map<StartEvent, Node> startTriggers;

    /**
     * Instantiates a new process definition. A UUID is generated randomly.
     * 
     * @param id
     *            the process definition id
     * @param description
     *            the description
     * @param startNodes
     *            the initial nodes that refer to the whole node-tree
     */
    public ProcessDefinitionImpl(UUID id, String description, List<Node> startNodes) {

        this.id = id;
        this.description = description;
        this.startNodes = startNodes;
        this.startTriggers = new HashMap<StartEvent, Node>();
    }

    @Override
    public String getName() {
        
        // TODO @Gerardo Hier muss noch eine Implementierung hin
        return null;
    }

    @Override
    public UUID getID() {

        return id;
    }

    @Override
    public String getDescription() {

        return description;
    }

    @Override
    public void setDescription(String description) {

        this.description = description;
    }

    @Override
    public List<Node> getStartNodes() {

        return startNodes;
    }

    @Override
    public Map<StartEvent, Node> getStartTriggers() {

        return startTriggers;
    }

    @Override
    public void addStartTrigger(StartEvent event, Node node)
    throws IllegalStarteventException {

        if (startNodes.contains(node)) {
            startTriggers.put(event, node);
        } else {
            throw new IllegalStarteventException();
        }

    }


}
