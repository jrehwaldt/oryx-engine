package de.hpi.oryxengine.deploy;

import java.util.UUID;

import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * The Interface Deployer. Use this class to deploy processes in the engine, not the repository itself.
 */
public interface Deployer {

    /**
     * Deploys a process: Adds it to the repository and does some registration stuff.
     * 
     * @param def
     *            the def
     */
    void deploy(ProcessDefinition def);

    /**
     * Deactivate.
     * 
     * @param id
     *            the id
     */
    void deactivate(UUID id);

    /**
     * Delete.
     * 
     * @param id
     *            the id
     */
    void delete(UUID id);
}
