package de.hpi.oryxengine;

import java.util.UUID;

import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.process.definition.ProcessDefinitionInside;


/**
 * Extends the RepositoryServiceInterface so that it provides more methods for the internal classes.
 * 
 * This Interface is expected to be used by internal classes.
 */
public interface RepositoryServiceInside extends RepositoryService {

    public ProcessDefinitionInside getProcessDefinitionInside(UUID processDefintionID)
    throws DefinitionNotFoundException;
}
