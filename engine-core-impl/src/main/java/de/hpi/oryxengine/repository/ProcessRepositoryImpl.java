package de.hpi.oryxengine.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * The Class ProcessRepositoryImpl. The Repository holds the process definitions in the engine. To instantiate these,
 * the repository has to be asked.
 */
public final class ProcessRepositoryImpl implements ProcessRepository {

    private static ProcessRepository instance = null;
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
        }
        return instance;
    }

    @Override
    public ProcessDefinition getDefinition(UUID id) throws DefinitionNotFoundException {

        if (!definitions.containsKey(id)) {
            throw new DefinitionNotFoundException();
        }
        return definitions.get(id);
    }

    @Override
    public void addDefinition(ProcessDefinition definition) {

        UUID id = definition.getID();
        definitions.put(id, definition);
    }

    @Override
    public void deleteDefinition(UUID id) {

        definitions.remove(id);

    }

    @Override
    public Map<UUID, ProcessDefinition> getDefinitions() {

        return definitions;
    }

}
