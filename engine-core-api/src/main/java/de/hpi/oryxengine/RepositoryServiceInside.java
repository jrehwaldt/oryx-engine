package de.hpi.oryxengine;

import java.util.UUID;

import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.process.definition.ProcessDefinitionInside;

public interface RepositoryServiceInside extends RepositoryService {

    public ProcessDefinitionInside getProcessDefinitionInside(UUID processDefintionID)
    throws DefinitionNotFoundException;
}
