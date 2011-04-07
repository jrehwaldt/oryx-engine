package de.hpi.oryxengine.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.Service;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * The Class ProcessRepositoryImpl. The Repository holds the process definitions in the engine. To instantiate these,
 * the repository has to be asked.
 */
public final class ProcessRepositoryImpl implements ProcessRepository, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass()); 
    

    public static final UUID SIMPLE_PROCESS_ID = UUID.randomUUID();

    private Map<UUID, ProcessDefinition> definitions;

    /**
     * Instantiates a new process repository impl.
     */
    public ProcessRepositoryImpl() {

        this.definitions = new HashMap<UUID, ProcessDefinition>();
    }

    @Override
    public void start() {
        
        logger.info("Starting the correlation manager");
    }
    
    @Override
    public void stop() {
        
        logger.info("Stopping the correlation manager");
    }

    @Override
    public ProcessDefinition getDefinition(UUID id)
    throws DefinitionNotFoundException {

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
    public List<ProcessDefinition> getDefinitions() {

        return new ArrayList<ProcessDefinition>(definitions.values());
    }
    
    @Override
    public boolean containsDefinition(@Nonnull UUID id) {
        return this.definitions.containsKey(id);
    }

}
