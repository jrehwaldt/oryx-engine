package org.jodaengine.exception;

import javax.annotation.Nonnull;

import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * This is an exception stating that a definition is not activated. So that's why the process cannot be instantiated.
 */
public class DefinitionNotActivatedException extends JodaEngineException {

    private static final long serialVersionUID = -513944695461864331L;

    private static final String MESSAGE = "The requested process definition was not activated. Please perform navigatorService.activateProcessDefinition(...) .";

    private final ProcessDefinitionID definitionID;

    /**
     * Default Constructor.
     * 
     * @param definitionID
     *            the id of the resource that is not available
     */
    public DefinitionNotActivatedException(@Nonnull ProcessDefinitionID definitionID) {

        super(MESSAGE);

        this.definitionID = definitionID;
    }

    /**
     * Gets the definition id.
     * 
     * @return the definition id
     */
    public ProcessDefinitionID getProcessDefinitionID() {

        return definitionID;
    }

    @Override
    @Nonnull
    public String toString() {

        return String.format("ProcessDefinition[id: %s]", getProcessDefinitionID());
    }
}
