package de.hpi.oryxengine.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.factory.definition.ProcessDefinitionFactory;
import de.hpi.oryxengine.factory.definition.SimpleProcessDefinitionFactory;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionImpl;
import de.hpi.oryxengine.process.definition.StartNodeParameter;
import de.hpi.oryxengine.process.definition.StartNodeParameterImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class ProcessRepositoryImpl. The Repository holds the process definitions in the engine. To instantiate these,
 * the repository has to be asked.
 */
public final class ProcessRepositoryImpl implements ProcessRepository {

    public static final UUID SIMPLE_PROCESS_ID = UUID.randomUUID();

    /** The instance. */
    private static ProcessRepository instance = null;

    /** The definitions. */
    private Map<UUID, ProcessDefinition> definitions;

    /**
     * Instantiates a new process repository impl.
     */
    public ProcessRepositoryImpl() {

        this.definitions = new HashMap<UUID, ProcessDefinition>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessDefinition getDefinition(UUID id)
    throws DefinitionNotFoundException {

        if (!definitions.containsKey(id)) {
            throw new DefinitionNotFoundException();
        }
        return definitions.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDefinition(ProcessDefinition definition) {

        UUID id = definition.getID();
        definitions.put(id, definition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDefinition(UUID id) {

        definitions.remove(id);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<UUID, ProcessDefinition> getDefinitions() {

        return definitions;
    }

    /**
     * Fill with sample processes.
     */
    private static void fillWithSampleProcesses() {

        // Use Process Builder here.

        ProcessBuilder builder = new ProcessBuilderImpl();
        StartNodeParameter param = new StartNodeParameterImpl();
        param.setActivity(new AddNumbersAndStoreActivity("result", 1, 1));
        param.setIncomingBehaviour(new SimpleJoinBehaviour());
        param.setOutgoingBehaviour(new TakeAllSplitBehaviour());

        // Create a mail adapater event here
        param.setStartEvent(null);

        Node node1 = builder.createStartNode(param);

        Node node2 = builder.createNode(param);

        builder.createTransition(node1, node2).setDescription("description").setID(SIMPLE_PROCESS_ID);

        ProcessDefinition def = builder.buildDefinition();

        // instance.addDefinition(def); We don't do this here, because we want to actually deploy this process, not only
        // put it in the repo.
    }

}
