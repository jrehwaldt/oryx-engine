package de.hpi.oryxengine.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.factory.definition.ProcessDefinitionFactory;
import de.hpi.oryxengine.factory.definition.SimpleProcessDefinitionFactory;
import de.hpi.oryxengine.factory.process.ExampleProcessTokenFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinition;

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
    private ProcessRepositoryImpl() {

        this.definitions = new HashMap<UUID, ProcessDefinition>();
    }

    /**
     * Gets the single instance of ProcessRepositoryImpl.
     *
     * @return single instance of ProcessRepositoryImpl
     */
    public static ProcessRepository getInstance() {

        if (instance == null) {
            instance = new ProcessRepositoryImpl();
            fillWithSampleProcesses();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessDefinition getDefinition(UUID id) throws DefinitionNotFoundException {

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
        ProcessDefinitionFactory factory = new SimpleProcessDefinitionFactory();
        ProcessDefinition def = factory.create(SIMPLE_PROCESS_ID);
        instance.addDefinition(def);
    }

}
